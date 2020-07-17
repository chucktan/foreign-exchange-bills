package com.foreign.exchange.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

public class Transactioninfo {
    /**
     * 交易ID
     */
    @Id
    @Column(name = "transaction_id")
    private String transactionId;

    /**
     * 货币代码 CNYUSD
     */
    @Column(name = "currency_code")
    private String currencyCode;

    /**
     * 货币名称 人民币兑美元
     */
    @Column(name = "currency_name")
    private String currencyName;

    /**
     * 交易方向 1.买；2.卖
     */
    private Integer buyorsell;

    /**
     * 交易数量
     */
    @Column(name = "trade_number")
    private Integer tradeNumber;

    /**
     * 交易价格
     */
    private BigDecimal price;

    /**
     * 手续费
     */
    private BigDecimal fee;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    @Column(name = "CREATED_BY")
    private String createdBy;

    /**
     * 创建时间
     */
    @Column(name = "CREATED_TIME")
    private Date createdTime;

    /**
     * 获取交易ID
     *
     * @return transaction_id - 交易ID
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * 设置交易ID
     *
     * @param transactionId 交易ID
     */
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * 获取货币代码 CNYUSD
     *
     * @return currency_code - 货币代码 CNYUSD
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * 设置货币代码 CNYUSD
     *
     * @param currencyCode 货币代码 CNYUSD
     */
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    /**
     * 获取货币名称 人民币兑美元
     *
     * @return currency_name - 货币名称 人民币兑美元
     */
    public String getCurrencyName() {
        return currencyName;
    }

    /**
     * 设置货币名称 人民币兑美元
     *
     * @param currencyName 货币名称 人民币兑美元
     */
    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    /**
     * 获取交易方向 1.买；2.卖
     *
     * @return buyorsell - 交易方向 1.买；2.卖
     */
    public Integer getBuyorsell() {
        return buyorsell;
    }

    /**
     * 设置交易方向 1.买；2.卖
     *
     * @param buyorsell 交易方向 1.买；2.卖
     */
    public void setBuyorsell(Integer buyorsell) {
        this.buyorsell = buyorsell;
    }

    /**
     * 获取交易数量
     *
     * @return trade_number - 交易数量
     */
    public Integer getTradeNumber() {
        return tradeNumber;
    }

    /**
     * 设置交易数量
     *
     * @param tradeNumber 交易数量
     */
    public void setTradeNumber(Integer tradeNumber) {
        this.tradeNumber = tradeNumber;
    }

    /**
     * 获取交易价格
     *
     * @return price - 交易价格
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 设置交易价格
     *
     * @param price 交易价格
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 获取手续费
     *
     * @return fee - 手续费
     */
    public BigDecimal getFee() {
        return fee;
    }

    /**
     * 设置手续费
     *
     * @param fee 手续费
     */
    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取创建人
     *
     * @return CREATED_BY - 创建人
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * 设置创建人
     *
     * @param createdBy 创建人
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * 获取创建时间
     *
     * @return CREATED_TIME - 创建时间
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * 设置创建时间
     *
     * @param createdTime 创建时间
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}