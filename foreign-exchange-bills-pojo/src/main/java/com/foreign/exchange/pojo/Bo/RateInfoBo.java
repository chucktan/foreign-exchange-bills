package com.foreign.exchange.pojo.Bo;

import com.foreign.exchange.pojo.Vo.TransactionInfoVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 前端导入数据实体
 * @author
 * @create 2020-07-30-16:57
 */
public class RateInfoBo {
    private String rateName;//外汇名称
    private String rateCode;//外汇代码
    private String remark;//备注
    private Double lastPrice;//上次价格
    private Double riseOrDrop;//最后涨跌幅，(涨/跌%,通过最新价/最后一笔交易价格计算)
    private Double newestPrice;//最新价
    private Integer rateNumber;//股票数量
    private Double diffAmount;//差值
    private Double feeService;//佣金
    private Double feeStamp;//印花税

    private Integer tradeFlag;//交易方向（1买/2卖/0无方向）
    private Double tradeFlagPrice;//交易价格(设置为最新获取价)

    //存储所有的交易信息，用于计算盈亏和持仓，使用完即剔除
    private List<TransactionInfoVo> transactionList = new ArrayList();

    public String getRateName() {
        return rateName;
    }

    public void setRateName(String rateName) {
        this.rateName = rateName;
    }

    public String getRateCode() {
        return rateCode;
    }

    public void setRateCode(String rateCode) {
        this.rateCode = rateCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(Double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public Double getRiseOrDrop() {
        return riseOrDrop;
    }

    public void setRiseOrDrop(Double riseOrDrop) {
        this.riseOrDrop = riseOrDrop;
    }

    public Integer getRateNumber() {
        return rateNumber;
    }

    public void setRateNumber(Integer rateNumber) {
        this.rateNumber = rateNumber;
    }

    public Double getDiffAmount() {
        return diffAmount;
    }

    public void setDiffAmount(Double diffAmount) {
        this.diffAmount = diffAmount;
    }

    public Double getFeeService() {
        return feeService;
    }

    public void setFeeService(Double feeService) {
        this.feeService = feeService;
    }

    public Double getFeeStamp() {
        return feeStamp;
    }

    public void setFeeStamp(Double feeStamp) {
        this.feeStamp = feeStamp;
    }

    public Integer getTradeFlag() {
        return tradeFlag;
    }

    public void setTradeFlag(Integer tradeFlag) {
        this.tradeFlag = tradeFlag;
    }

    public Double getTradeFlagPrice() {
        return tradeFlagPrice;
    }

    public void setTradeFlagPrice(Double tradeFlagPrice) {
        this.tradeFlagPrice = tradeFlagPrice;
    }

    public List<TransactionInfoVo> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<TransactionInfoVo> transactionList) {
        this.transactionList = transactionList;
    }

    public Double getNewestPrice() {
        return newestPrice;
    }

    public void setNewestPrice(Double newestPrice) {
        this.newestPrice = newestPrice;
    }
}
