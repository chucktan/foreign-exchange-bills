package com.foreign.exchange.service.stock;

import com.foreign.exchange.pojo.Bo.StockInfoBo;
import com.foreign.exchange.pojo.Vo.TransactionInfoVo;
import com.foreign.exchange.service.AbstractParseExcelService;
import com.foreign.exchange.service.BillUtils;
import com.foreign.exchange.service.CalcContext;
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
public class StockParseExcelService extends AbstractParseExcelService {

    private Logger logger = LoggerFactory.getLogger(StockParseExcelService.class);


    /**
     *加载excel,构建交易信息，交易对，并计算交易利润
     * @return
     */
    public  List<StockInfoBo> parseFile(File excelFile){
        this.logger.debug("解析Excel文件："+excelFile.getAbsolutePath());
        FileInputStream input = null;

        try {
            input = new FileInputStream(excelFile);
            XSSFWorkbook wb = new XSSFWorkbook(input);
            List<StockInfoBo> stockList = new ArrayList<>();
            //加载首页基本信息
            this.loadStockSheet(wb,stockList);
            Iterator iterator = stockList.iterator();

            while (iterator.hasNext()) {
                StockInfoBo stockInfo = (StockInfoBo)iterator.next();
                this.logger.debug(stockInfo.getStockName()+"--"+stockInfo.getStockCode()+"--"+stockInfo.getRemark());
            }

            List<TransactionInfoVo> transactionList = new ArrayList<>();
            //加载次页后的交易信息
            this.loadTransactionSheet(wb,stockList,transactionList);
            //计算交易信息，构建交易对
            this.processStockInfo(stockList);
            return  stockList;

        }catch (Exception ex){
            throw  new RuntimeException(ex);
        }finally {
            IOUtils.closeQuietly(input);
        }

    }


    /**
     * 加载excel首页初始信息：股票名称，股票代码，备注
     * @param wb
     * @param stockList
     */
    private  void loadStockSheet(XSSFWorkbook wb, List<StockInfoBo> stockList){
        XSSFSheet stockSheet = wb.getSheet("关注股票");
        int rowIndex =0;

        while (true){
            ++rowIndex;//行索引自增
            XSSFRow row = stockSheet.getRow(rowIndex);
            if (row == null){
                break;
            }
            //获取第一列：股票名称
            String stockName = this.getStringValue(row.getCell(0));
            if (stockName == null){
                break;
            }
            StockInfoBo stockInfo = new StockInfoBo();
            stockInfo.setStockName(stockName);
            stockInfo.setStockCode(this.getStringValue(row.getCell(1)));
            stockInfo.setRemark(this.getStringValue(row.getCell(2)));
            stockList.add(stockInfo);
        }
    }

    /**
     * 加载excel次页后交易信息,将交易信息加载入transactionInfoList.并装入对应的stock
     * @param wb 次页wb
     * @param stockList 从首页获取的股票列表
     * @param transactionInfoList
     */
     private  void loadTransactionSheet(XSSFWorkbook wb, List<StockInfoBo> stockList, List<TransactionInfoVo> transactionInfoList){
         Map<String,StockInfoBo> stockMap = new HashMap<>();
         Iterator iterator = stockList.iterator();

         while (iterator.hasNext()){
             StockInfoBo stockInfo = (StockInfoBo) iterator.next();
             stockMap.put(stockInfo.getStockCode(),stockInfo);
         }

         //遍历sheet,从次页开始
         int sheetNumber = wb.getNumberOfSheets();

         for (int i=0;i<sheetNumber;++i){
            XSSFSheet oneSheet = wb.getSheetAt(i);
            String sheetName = oneSheet.getSheetName();
            if ("成交记录".equals(sheetName)||this.isStockName(sheetName,stockList)){
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
                    StockInfoBo stockInfo= (StockInfoBo) stockMap.get(transactionInfo.getCode());
                    if (stockInfo != null){
                        stockInfo.getTransactionList().add(transactionInfo);
                    }

                    this.logger.debug("load transaction: "+transactionInfo.getName()+"--"+ transactionInfo.getPrice());

                }
            }
         }

    }

    /**
     * 检测sheet名称是否在stock列表中
     * @param sheetName
     * @param stockList
     * @return
     */
    private  boolean isStockName(String sheetName,List<StockInfoBo> stockList){
         if (sheetName == null){
             return  false;
         }else if (stockList!=null && !stockList.isEmpty()){
            Iterator iterator = stockList.iterator();
            StockInfoBo stockInfo;

            do {
                if (!iterator.hasNext()){
                    return  false;
                }
                stockInfo = (StockInfoBo)iterator.next();

            }while (stockInfo.getStockName()==null || stockInfo.getStockName().indexOf(sheetName)==-1);

             return true;
         }else
         {
             return false;
         }
    }

    /**
     * 计算股票的交易信息
     * @param stockList
     */
    private void processStockInfo(List<StockInfoBo> stockList){
        Iterator iterator = stockList.iterator();

        while (iterator.hasNext()){
            StockInfoBo stockInfo = (StockInfoBo) iterator.next();
            List<TransactionInfoVo> transactionList = stockInfo.getTransactionList();
            if (transactionList.size() > 0){
                this.calcOneStock(stockInfo);
            }
        }
    }

    /**
     * 计算单只股票交易信息
     * @param stockInfo
     */
    private void calcOneStock(StockInfoBo stockInfo){
        List<TransactionInfoVo> recordList = stockInfo.getTransactionList();
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
        int stockNumber = 0;
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
                stockNumber += currRecord.getTransNumber();
            }else if(currRecord.getBuyOrSell().equals("卖")) {
                stockNumber -= currRecord.getTransNumber();
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
            stockInfo.setLastPrice(lastPrice);
            if (stockNumber != 0){
                //设置持仓量
                stockInfo.setStockNumber(stockNumber);
            }

            double diffAmountD =diffAmount.setScale(2, RoundingMode.HALF_UP).doubleValue();

            if (diffAmountD != 0.0D){
                //设置差额
                stockInfo.setDiffAmount(diffAmountD);
            }

            double feeServiceD = feeService.setScale(2,RoundingMode.HALF_UP).doubleValue();

            if (feeServiceD !=0.0D){
                //设置手续费
                stockInfo.setFeeService(feeServiceD);
            }

            double feeStampD = feeStamp.setScale(2,RoundingMode.HALF_UP).doubleValue();

            if (feeStampD !=0.0D){
                //设置印花税
                stockInfo.setFeeStamp(feeStampD);
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
