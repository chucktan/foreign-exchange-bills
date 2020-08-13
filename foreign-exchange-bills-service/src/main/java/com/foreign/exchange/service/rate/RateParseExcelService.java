package com.foreign.exchange.service.rate;

import com.foreign.exchange.pojo.Bo.RateInfoBo;
import com.foreign.exchange.pojo.Bo.StockInfoBo;
import com.foreign.exchange.pojo.Vo.TransactionInfoVo;
import com.foreign.exchange.service.AbstractParseExcelService;
import com.foreign.exchange.service.BillUtils;
import com.foreign.exchange.service.CalcContext;
import org.apache.poi.ss.formula.functions.Rate;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * @author
 * @create 2020-08-06-10:18
 */
public class RateParseExcelService extends AbstractParseExcelService {

    private Logger logger = LoggerFactory.getLogger(RateParseExcelService.class);


    /**
     *加载excel,构建交易信息，交易对，并计算交易利润
     * @return
     */
    public  List<RateInfoBo> parseFile(File excelFile){
        this.logger.debug("解析Excel文件："+excelFile.getAbsolutePath());
        FileInputStream input = null;

        try {
            input = new FileInputStream(excelFile);
            XSSFWorkbook wb = new XSSFWorkbook(input);
            List<RateInfoBo> rateList = new ArrayList<>();
            //加载首页基本信息
            this.loadStockSheet(wb,rateList);
            Iterator iterator = rateList.iterator();

            while (iterator.hasNext()) {
                RateInfoBo rateInfo = (RateInfoBo)iterator.next();
                this.logger.debug(rateInfo.getRateName()+"--"+rateInfo.getRateCode()+"--"+rateInfo.getRemark());
            }

            List<TransactionInfoVo> transactionList = new ArrayList<>();
            //加载次页后的交易信息
            this.loadTransactionSheet(wb,rateList,transactionList);
            //计算交易信息，构建交易对
            this.processStockInfo(rateList);
            return  rateList;

        }catch (Exception ex){
            throw  new RuntimeException(ex);
        }finally {
            IOUtils.closeQuietly(input);
        }

    }


    /**
     * 加载excel首页初始信息：外汇名称，外汇代码，备注
     * @param wb
     * @param rateList
     */
    private  void loadStockSheet(XSSFWorkbook wb, List<RateInfoBo> rateList){
        XSSFSheet stockSheet = wb.getSheet("关注外汇");
        int rowIndex =0;

        while (true){
            ++rowIndex;//行索引自增
            XSSFRow row = stockSheet.getRow(rowIndex);
            if (row == null){
                break;
            }
            //获取第一列：股票名称
            String rateName = this.getStringValue(row.getCell(0));
            if (rateName == null){
                break;
            }
            RateInfoBo rateInfo = new RateInfoBo();
            rateInfo.setRateName(rateName);
            rateInfo.setRateCode(this.getStringValue(row.getCell(1)));
            rateInfo.setRemark(this.getStringValue(row.getCell(2)));
            rateList.add(rateInfo);
        }
    }

    /**
     * 加载excel次页后交易信息,将交易信息加载入transactionInfoList.并装入对应的stock
     * @param wb 次页wb
     * @param rateList 从首页获取的股票列表
     * @param transactionInfoList
     */
     private  void loadTransactionSheet(XSSFWorkbook wb, List<RateInfoBo> rateList, List<TransactionInfoVo> transactionInfoList){
         Map<String,RateInfoBo> rateMap = new HashMap<>();
         Iterator iterator = rateList.iterator();

         while (iterator.hasNext()){
             RateInfoBo rateInfo = (RateInfoBo) iterator.next();
             rateMap.put(rateInfo.getRateCode(),rateInfo);
         }

         //遍历sheet,从次页开始
         int sheetNumber = wb.getNumberOfSheets();

         for (int i=0;i<sheetNumber;++i){
            XSSFSheet oneSheet = wb.getSheetAt(i);
            String sheetName = oneSheet.getSheetName();
            if ("成交记录".equals(sheetName)||this.isRateName(sheetName,rateList)){
                 //将交易信息存入transaction
                int rowIndex = 0;

                //从第一行开始
                while (true){
                    ++rowIndex;
                    XSSFRow row = oneSheet.getRow(rowIndex);
                    if (row == null){
                        break;
                    }

                    String date = this.getStringValue(row.getCell(0));
                    if (date == null){
                        break;
                    }

                    TransactionInfoVo transactionInfo = new TransactionInfoVo();
                    transactionInfo.setDate(date);
                    transactionInfo.setName(this.getStringValue(row.getCell(1)));
                    transactionInfo.setCode(this.getStringValue(row.getCell(2)));
                    transactionInfo.setBuyOrSell(this.getStringValue(row.getCell(3)));
                    transactionInfo.setTransNumber(this.getIntValue(row.getCell(4)));
                    transactionInfo.setPrice(this.getNumericValue(row.getCell(5)));
                    transactionInfo.setFeeService(this.getNumericValue(row.getCell(6)));
                    transactionInfo.setFeeStamp(this.getNumericValue(row.getCell(7)));
                    transactionInfo.setRemark(this.getStringValue(row.getCell(8)));

                    transactionInfoList.add(transactionInfo);
                    RateInfoBo rateInfo= (RateInfoBo) rateMap.get(transactionInfo.getCode());
                    if (rateInfo != null){
                        rateInfo.getTransactionList().add(transactionInfo);
                    }

                    this.logger.debug("load transaction: "+transactionInfo.getName()+"--"+ transactionInfo.getPrice());

                }
            }
         }

    }

    /**
     * 检测sheet名称是否在rate列表中
     * @param sheetName
     * @param rateList
     * @return
     */
    private  boolean isRateName(String sheetName,List<RateInfoBo> rateList){
         if (sheetName == null){
             return  false;
         }else if (rateList!=null && !rateList.isEmpty()){
            Iterator iterator = rateList.iterator();
            RateInfoBo rateInfo;

            do {
                if (!iterator.hasNext()){
                    return  false;
                }
                rateInfo = (RateInfoBo)iterator.next();

            }while (rateInfo.getRateName()==null || rateInfo.getRateName().indexOf(sheetName)==-1);

             return true;
         }else
         {
             return false;
         }
    }

    /**
     * 计算股票的交易信息
     * @param rateList
     */
    private void processStockInfo(List<RateInfoBo> rateList){
        Iterator iterator = rateList.iterator();

        while (iterator.hasNext()){
            RateInfoBo rateInfo = (RateInfoBo) iterator.next();
            List<TransactionInfoVo> transactionList = rateInfo.getTransactionList();
            if (transactionList.size() > 0){
                this.calcOneStock(rateInfo);
            }
        }
    }

    /**
     * 计算单只股票交易信息
     * @param rateInfo
     */
    private void calcOneStock(RateInfoBo rateInfo){
        List<TransactionInfoVo> recordList = rateInfo.getTransactionList();
        BillUtils.clearRecordPair(recordList);
        CalcContext calcCtx = new CalcContext();

        for (int i=0;i<recordList.size();++i){
            TransactionInfoVo currRecord = recordList.get(i);
            //卖
            if (BillUtils.isCloseRecord(currRecord)){
                calcCtx.initRecord(currRecord);
                //遍历该交易以前的所有交易
                for (int j=i-1;j>=0;--j){
                    TransactionInfoVo preRecord= recordList.get(j);
                    calcCtx.buildPair(preRecord);
                    if (calcCtx.getCurrTradeNumber()<=0){
                        break;
                    }
                }
                calcCtx.finishRecord();
            }
        }
        Double lastPrice = null;
        int rateNumber = 0;
        BigDecimal diffAmount = new BigDecimal(0);
        BigDecimal feeService = new BigDecimal(0);
        BigDecimal feeStamp = new BigDecimal(0);
        Iterator iterator = recordList.iterator();

        //统计所有的交易
        while(iterator.hasNext()){
            TransactionInfoVo currRecord = (TransactionInfoVo)iterator.next();
            currRecord.setPair(BillUtils.makePairAttr(currRecord.getPairList(),currRecord.getTransNumber()));
            lastPrice = currRecord.getPrice();
            if (currRecord.getBuyOrSell().equals("买")){
                rateNumber += currRecord.getTransNumber();
            }else if(currRecord.getBuyOrSell().equals("卖")) {
                rateNumber -= currRecord.getTransNumber();
            }

            if (currRecord.getDiffPrice() !=null){
                diffAmount = diffAmount.add(new BigDecimal(currRecord.getDiffPrice()));
            }

            if (currRecord.getFeeService()!=null){
                feeService = feeService.add(new BigDecimal(currRecord.getFeeService()));
            }

            if (currRecord.getFeeStamp()!= null){
                feeStamp = feeStamp.add(new BigDecimal(currRecord.getFeeStamp()));
            }
        }
            rateInfo.setLastPrice(lastPrice);//最后交易价格
            if (rateNumber != 0){
                //设置持仓量
                rateInfo.setRateNumber(rateNumber);
            }

            double diffAmountD =diffAmount.setScale(2, RoundingMode.HALF_UP).doubleValue();

            if (diffAmountD != 0.0D){
                //设置差额
                rateInfo.setDiffAmount(diffAmountD);
            }

            double feeServiceD = feeService.setScale(2,RoundingMode.HALF_UP).doubleValue();

            if (feeServiceD !=0.0D){
                //设置手续费
                rateInfo.setFeeService(feeServiceD);
            }

            double feeStampD = feeStamp.setScale(2,RoundingMode.HALF_UP).doubleValue();

            if (feeStampD !=0.0D){
                //设置印花税
                rateInfo.setFeeStamp(feeStampD);
            }

    }

    //单元测试
//    public static void main(String[] args) {
//        ParseExcelService ParseExcelService = new ParseExcelService();
//        List<StockInfoBo> list = new ArrayList<>();
//        File excelFile = new File("D:/股票交易记录.xlsx");
//        try {
//            list = ParseExcelServiceimpl.parseFile(excelFile);
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
//
//    }
}
