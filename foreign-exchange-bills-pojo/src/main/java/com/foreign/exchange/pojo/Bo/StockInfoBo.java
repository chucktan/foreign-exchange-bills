package com.foreign.exchange.pojo.Bo;

import com.foreign.exchange.pojo.TransactionInfo;
import com.foreign.exchange.pojo.Vo.StockTransactionInfoVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 前端导入数据实体
 * @author
 * @create 2020-07-30-16:57
 */
public class StockInfoBo {
    private String stockName;//股票名称
    private String stockCode;//股票代码
    private String remark;//备注
    private Double newestPrice;//最新价
    private Double newestUpOrDown;//市场涨跌幅（涨/跌,通过最新价/昨日收盘价计算）
    private Double lastPrice;//上次价格
    private Double riseOrDrop;//最后涨跌幅，(涨/跌%,通过最新价/最后一笔交易价格计算)
    private Integer stockNumber;//股票数量
    private Double diffAmount;//差值
    private Double feeService;//佣金
    private Double feeStamp;//印花税
    private Integer tradeFlag;//交易方向（1买/2卖/0无方向）
    private Double tradeFlagPrice;//交易价格(设置为最新获取价)

    //存储所有的交易信息，用于计算盈亏和持仓，使用完即剔除
    private List<StockTransactionInfoVo> transactionList = new ArrayList();

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

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Double getNewestPrice() {
        return this.newestPrice;
    }

    public void setNewestPrice(Double newestPrice) {
        this.newestPrice = newestPrice;
    }

    public Double getNewestUpOrDown() {
        return this.newestUpOrDown;
    }

    public void setNewestUpOrDown(Double newestUpOrDown) {
        this.newestUpOrDown = newestUpOrDown;
    }

    public Double getLastPrice() {
        return this.lastPrice;
    }

    public void setLastPrice(Double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public Double getRiseOrDrop() {
        return this.riseOrDrop;
    }

    public void setRiseOrDrop(Double riseOrDrop) {
        this.riseOrDrop = riseOrDrop;
    }

    public Integer getStockNumber() {
        return this.stockNumber;
    }

    public void setStockNumber(Integer stockNumber) {
        this.stockNumber = stockNumber;
    }

    public Double getDiffAmount() {
        return this.diffAmount;
    }

    public void setDiffAmount(Double diffAmount) {
        this.diffAmount = diffAmount;
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

    public Integer getTradeFlag() {
        return this.tradeFlag;
    }

    public void setTradeFlag(Integer tradeFlag) {
        this.tradeFlag = tradeFlag;
    }

    public Double getTradeFlagPrice() {
        return this.tradeFlagPrice;
    }

    public void setTradeFlagPrice(Double tradeFlagPrice) {
        this.tradeFlagPrice = tradeFlagPrice;
    }

    public List<StockTransactionInfoVo> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<StockTransactionInfoVo> transactionList) {
        this.transactionList = transactionList;
    }
}
