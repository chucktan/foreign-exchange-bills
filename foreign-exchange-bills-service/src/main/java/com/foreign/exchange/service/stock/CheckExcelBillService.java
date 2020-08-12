package com.foreign.exchange.service.stock;

import com.foreign.exchange.pojo.TransactionInfo;
import com.foreign.exchange.pojo.Vo.StockPairInfoVo;
import com.foreign.exchange.pojo.Vo.StockTransactionInfoVo;
import com.foreign.exchange.service.AbstractParseExcelService;
import com.foreign.exchange.utils.OptionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author excel加载测试类
 * @create 2020-08-11-15:43
 */
public class CheckExcelBillService extends AbstractParseExcelService {

    private Logger loggger = LoggerFactory.getLogger(CheckExcelBillService.class);

    /**
     * 解析Excel文件
     * @param excelFile
     * @throws IOException
     */
    public  void parseExcel(File excelFile) {
        this.loggger.debug("解析excel文件"+excelFile.getAbsolutePath());
        FileInputStream input = null;
        try {
            input = new FileInputStream(excelFile);
            XSSFWorkbook wb = new XSSFWorkbook(input);
            List<StockTransactionInfoVo> transactionList = new ArrayList<>();
            this.loadTransactionSheet(wb,transactionList);
        }catch (IOException ex){
            throw  new RuntimeException(ex);
        }finally {
            IOUtils.closeQuietly(input);
        }
    }


    /**
     * 加载excel
     * @param wb
     * @param transactionList
     */
    private void loadTransactionSheet(XSSFWorkbook wb,List<StockTransactionInfoVo> transactionList){
        int sheetNumber = wb.getNumberOfSheets();

        //遍历
        for (int i=0;i<sheetNumber;i++){
            XSSFSheet oneSheet = wb.getSheetAt(i);
            String sheetName = oneSheet.getSheetName();
            if (!"关注股票".equals(sheetName)){
                int rowIndex = 0;
                while (true){
                    ++rowIndex;
                    XSSFRow row = oneSheet.getRow(rowIndex);

                    if (row ==null){
                        break;
                    }

                    String date = this.getStringValue(row.getCell(0));
                    if (date == null){
                        break;
                    }

                    StockTransactionInfoVo info = new StockTransactionInfoVo();
                    info.setDate(date);
                    info.setStockName(this.getStringValue(row.getCell(1)));
                    info.setStockCode(this.getStringValue(row.getCell(2)));
                    info.setBuyOrSell(this.getStringValue(row.getCell(3)));
                    info.setStockNumber(this.getIntValue(row.getCell(4)));
                    info.setPrice(this.getNumericValue(row.getCell(5)));
                    info.setPair(this.getStringValue(row.getCell(6)));
                    info.setDiffPrice(this.getNumericValue(row.getCell(7)));
                    info.setSheetName(sheetName);
                    info.setRowIndex(rowIndex);
                    transactionList.add(info);

                }
            }
        }
    }

    /**
     * 校验交易信息
     * @param list
     */
    private void checkTransactionInfo(List<StockTransactionInfoVo> list){
        Map<Integer,List<StockPairInfoVo>> pairMap = new HashMap<>();
        Iterator transactionIterator = list.iterator();
        Iterator pairIterator;

        while (transactionIterator.hasNext()){
            StockTransactionInfoVo transactionInfo = (StockTransactionInfoVo) transactionIterator.next();
            try {
                List<StockPairInfoVo> pairList = transactionInfo.loadPairList();
                StockPairInfoVo onePair = null;
                List<StockPairInfoVo> samePairs;
                if(pairList !=null){
                    for (pairIterator=pairList.iterator();pairIterator.hasNext();((List)samePairs).add(onePair)){
                        onePair = (StockPairInfoVo) pairIterator.next();
                        Integer key = Integer.parseInt(onePair.getTransactionInfo().getStockCode())*100000+onePair.getPairCode();
                        samePairs = pairMap.get(key);
                        if (samePairs == null){
                            samePairs = new ArrayList<>();
                            pairMap.put(key,samePairs);
                        }

                    }
                }


            }catch (Exception ex){
                this.loggger.error("sheetName="+transactionInfo.getSheetName()+",rowIndex="+transactionInfo.getRowIndex(),ex);

            }

            List<Integer> pairKeyList = new ArrayList<>();
            pairKeyList.addAll(pairMap.keySet());
            pairKeyList.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1-o2;
                }
            });

            double totalDiffAmount = 0.0D;

            double pairDiffAmount;

            for (pairIterator = pairKeyList.iterator();pairIterator.hasNext();totalDiffAmount +=pairDiffAmount){

                Integer pairKey = (Integer) pairIterator.next();
                List<StockPairInfoVo> pairRecordList = pairMap.get(pairKey);
                pairDiffAmount = this.checkPairList(pairRecordList);
            }
            System.out.println("totalDiffAmount:"+totalDiffAmount);
        }



    }

    private  double checkPairList(List<StockPairInfoVo> pairRecordList){
        if (pairRecordList.isEmpty()){
            return  0.0D;
        }else {
            StockPairInfoVo lastRecord = pairRecordList.get(pairRecordList.size()-1);
            if (pairRecordList.size() < 2){
                this.outLogMessage(lastRecord, "配对记录只有一个，" + lastRecord.getTransactionInfo().getPair());
                return 0.0D;
            }else {

                double calcAmount = 0.0D;
                double diffAmount = 0.0D;
                Iterator pairIterator = pairRecordList.iterator();

                while (pairIterator.hasNext()){
                    StockPairInfoVo pi= (StockPairInfoVo) pairIterator.next();
                    StockTransactionInfoVo transactionInfo = pi.getTransactionInfo();
                    if (!transactionInfo.getStockCode().equals(lastRecord.getTransactionInfo().getStockCode())){
                        this.outLogMessage(pi,"证券编码不一样");
                    }

                    if (transactionInfo.getDiffPrice() !=null){
                        diffAmount = OptionUtils.add(diffAmount,transactionInfo.getDiffPrice());
                    }
                    if (transactionInfo.getBuyOrSell().equals("买")){
                        calcAmount = OptionUtils.substract(calcAmount,OptionUtils.multi(transactionInfo.getPrice(),(double)pi.getPairNumber()));
                    }else {
                        if (!transactionInfo.getBuyOrSell().equals("卖")) {
                            throw new RuntimeException("不支持：" + transactionInfo.getBuyOrSell());
                        }

                        calcAmount = OptionUtils.add(calcAmount, OptionUtils.multi(transactionInfo.getPrice(), (double)pi.getPairNumber()));
                    }

                }

                if (calcAmount != diffAmount) {
                    this.outLogMessage(lastRecord, "价差不一致, calcAmount=" + calcAmount);
                    return 0.0D;
                } else {
                    this.outLogMessage(lastRecord, "价差一致, " + diffAmount);
                    return diffAmount;
                }
            }
        }
    }

    private void outLogMessage(StockPairInfoVo pairInfo,String msg){
        StringBuffer sb = new StringBuffer();
        sb.append("sheetName=").append(pairInfo.getTransactionInfo().getSheetName());
        sb.append(",rowIndex=").append(pairInfo.getTransactionInfo().getRowIndex());
        sb.append(",tradeDate=").append(pairInfo.getTransactionInfo().getDate());
        sb.append(",").append(msg);
        this.loggger.warn(sb.toString());
    }
}
