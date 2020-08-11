package com.foreign.exchange.pojo.Vo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.util.Length;

import java.util.ArrayList;
import java.util.List;

/**
 * 交易信息题
 * @author
 * @create 2020-07-30-17:07
 */
public class StockTransactionInfoVo {
    private Logger logger = LoggerFactory.getLogger(StockTransactionInfoVo.class);
    private String date; //日期
    private String stockName;//股票名称
    private String stockCode;//股票代码
    private String buyOrSell;//买/卖
    private Integer stockNumber;//交易数量
    private Double price;//交易价格
    private Double feeService;//佣金
    private Double feeStamp;//印花税
    private String remark;//备注
    private String pair;//交易信息201254(2001),201531(1240.1)
    private Double diffPrice;//差价
    private String sheetName;//excel对应下标名称
    private int rowIndex;//excel对应列标

    private List<StockPairInfoVo> pairList;//1（20）,2（50）

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStockName() {
        return this.stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getStockCode() {
        return this.stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getBuyOrSell() {
        return this.buyOrSell;
    }

    public void setBuyOrSell(String buyOrSell) {
        this.buyOrSell = buyOrSell;
    }

    public Integer getStockNumber() {
        return this.stockNumber;
    }

    public void setStockNumber(Integer stockNumber) {
        this.stockNumber = stockNumber;
    }

    public Double getPrice() {
        return this.price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getFeeService() {
        return this.feeService;
    }

    public void setFeeService(Double feeService) {
        this.feeService = feeService;
    }

    public Double getFeeStamp() {
        return this.feeStamp;
    }

    public void setFeeStamp(Double feeStamp) {
        this.feeStamp = feeStamp;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPair() {
        return this.pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public Double getDiffPrice() {
        return this.diffPrice;
    }

    public void setDiffPrice(Double diffPrice) {
        this.diffPrice = diffPrice;
    }

    public String getSheetName() {
        return this.sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public int getRowIndex() {
        return this.rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public List<StockPairInfoVo> getPairList() {
        return pairList;
    }

    public void setPairList(List<StockPairInfoVo> pairList) {
        this.pairList = pairList;
    }

    /**
     * 加载实际交易对
     * @return
     */
    public  List<StockPairInfoVo> loadPairList(){
        //有存量交易
       if (this.pair !=null && !this.pair.trim().isEmpty()){
           if (this.pairList !=null){
               return  this.pairList;
           }else {
               List<StockPairInfoVo> pairInfos = new ArrayList<>();
               //存量交易只有一份
               if (this.pair.indexOf(",") == -1 && this.pair.indexOf("(") ==-1){
                   StockPairInfoVo p = new StockPairInfoVo();
                   p.setPairCode(this.getPairCode(this.pair));
                   p.setPairNumber(this.stockNumber);
                   p.setTransactionInfo(this);
                   pairInfos.add(p);
               }else{
                   //存量交易有多份
                   String[] pairStrings = this.pair.split(",");
                   int pairLen = pairStrings.length;

                   //分批处理每个交易,1(20),3(30.25)
                   for (int i=0;i<pairLen;i++){
                        String pairStr = pairStrings[i].trim();
                        int start = pairStr.indexOf("(");
                        int end = pairStr.indexOf(")");
                        if(start !=-1 && end!=-1){
                            StockPairInfoVo p = new StockPairInfoVo();
                            p.setPairCode(this.getPairCode(pairStr.substring(0,start)));
                            p.setPairNumber(this.getPairCode(pairStr.substring(start+1,end)));
                            p.setTransactionInfo(this);
                            pairInfos.add(p);

                        }else{
                            this.logger.error("pair信息格式错误:"+this.pair);
                        }
                   }

               }
               this.pairList = pairInfos;
               return  pairList;
           }
       }else{
           return  null;

       }
    }


    private  Integer getPairCode(String pairStr){
        return  pairStr.indexOf(".") ==-1? Integer.valueOf(pairStr):Double.valueOf(pairStr).intValue();
    }
}
