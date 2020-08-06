package com.foreign.exchange.service.impl;

import com.foreign.exchange.pojo.Bo.StockInfoBo;
import com.foreign.exchange.pojo.TransactionInfo;
import com.foreign.exchange.pojo.Vo.StockTransactionInfoVo;
import com.foreign.exchange.service.AbstractParseExcelService;
import com.foreign.exchange.service.BillUtils;
import com.foreign.exchange.service.ParseExcelService;
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
 * @author
 * @create 2020-08-06-10:18
 */
public class ParseExcelServiceImpl  extends AbstractParseExcelService implements ParseExcelService{

    private Logger logger = LoggerFactory.getLogger(ParseExcelServiceImpl.class);


    /**
     *解析Excel,将数据加载到对应的stock.transaction中
     * @return
     */
    public  List<StockInfoBo> parseFile(File excelFile) throws IOException {
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

            List<StockTransactionInfoVo> transactionList = new ArrayList<>();
            //加载次页后的交易信息
            this.loadTransactionSheet(wb,stockList,transactionList);
            //计算交易信息

        }catch (Exception ex){

        }

        return  null;
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
     private  void loadTransactionSheet(XSSFWorkbook wb, List<StockInfoBo> stockList, List<StockTransactionInfoVo> transactionInfoList){
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

                    StockTransactionInfoVo transactionInfo = new StockTransactionInfoVo();
                    transactionInfo.setDate(date);
                    transactionInfo.setStockName(this.getStringValue(row.getCell(1)));
                    transactionInfo.setStockCode(this.getStringValue(row.getCell(2)));
                    transactionInfo.setBuyOrSell(this.getStringValue(row.getCell(3)));
                    transactionInfo.setStockNumber(this.getIntValue(row.getCell(4)));
                    transactionInfo.setPrice(this.getNumericValue(row.getCell(5)));
                    transactionInfo.setFeeService(this.getNumericValue(row.getCell(6)));
                    transactionInfo.setFeeStamp(this.getNumericValue(row.getCell(7)));
                    transactionInfo.setRemark(this.getStringValue(row.getCell(8)));

                    transactionInfoList.add(transactionInfo);
                    StockInfoBo stockInfo= (StockInfoBo) stockMap.get(transactionInfo.getStockCode());
                    if (stockInfo != null){
                        stockInfo.getTransactionList().add(transactionInfo);
                    }

                    this.logger.debug("load transaction: "+transactionInfo.getStockName()+"--"+ transactionInfo.getPrice());

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
            List<StockTransactionInfoVo> transactionList = stockInfo.getTransactionList();
            if (transactionList.size() > 0){

            }
        }
    }

    /**
     * 计算单只股票交易信息
     * @param stockInfo
     */
    public void calcOneStock(StockInfoBo stockInfo){
        List<StockTransactionInfoVo> list = stockInfo.getTransactionList();
        BillUtils.clearRecordPair(list);

    }
}
