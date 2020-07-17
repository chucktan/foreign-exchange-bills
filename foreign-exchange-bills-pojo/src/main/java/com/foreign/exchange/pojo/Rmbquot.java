package com.foreign.exchange.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

public class Rmbquot {
    /**
     * 牌价ID
     */
    @Id
    @Column(name = "rmbquot_id")
    private String rmbquotId;

    /**
     * 货币代码
     */
    @Column(name = "currency_code")
    private String currencyCode;

    /**
     * 货币名称
     */
    @Column(name = "curreny_name")
    private String currenyName;

    /**
     * 现汇买入价
     */
    @Column(name = "fbuy_price")
    private BigDecimal fbuyPrice;

    /**
     * 现钞买入价
     */
    @Column(name = "mbuy_price")
    private BigDecimal mbuyPrice;

    /**
     * 现汇卖出价
     */
    @Column(name = "fsell_price")
    private BigDecimal fsellPrice;

    /**
     * 现钞卖出价
     */
    @Column(name = "msell_price")
    private BigDecimal msellPrice;

    /**
     * 银行折算价/中间价
     */
    @Column(name = "bank_conversion_price")
    private BigDecimal bankConversionPrice;

    /**
     * 发布日期
     */
    @Column(name = "update_date")
    private Date updateDate;

    /**
     * 发布时间
     */
    @Column(name = "update_time")
    private Date updateTime;

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
     * 获取牌价ID
     *
     * @return rmbquot_id - 牌价ID
     */
    public String getRmbquotId() {
        return rmbquotId;
    }

    /**
     * 设置牌价ID
     *
     * @param rmbquotId 牌价ID
     */
    public void setRmbquotId(String rmbquotId) {
        this.rmbquotId = rmbquotId;
    }

    /**
     * 获取货币代码
     *
     * @return currency_code - 货币代码
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * 设置货币代码
     *
     * @param currencyCode 货币代码
     */
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    /**
     * 获取货币名称
     *
     * @return curreny_name - 货币名称
     */
    public String getCurrenyName() {
        return currenyName;
    }

    /**
     * 设置货币名称
     *
     * @param currenyName 货币名称
     */
    public void setCurrenyName(String currenyName) {
        this.currenyName = currenyName;
    }

    /**
     * 获取现汇买入价
     *
     * @return fbuy_price - 现汇买入价
     */
    public BigDecimal getFbuyPrice() {
        return fbuyPrice;
    }

    /**
     * 设置现汇买入价
     *
     * @param fbuyPrice 现汇买入价
     */
    public void setFbuyPrice(BigDecimal fbuyPrice) {
        this.fbuyPrice = fbuyPrice;
    }

    /**
     * 获取现钞买入价
     *
     * @return mbuy_price - 现钞买入价
     */
    public BigDecimal getMbuyPrice() {
        return mbuyPrice;
    }

    /**
     * 设置现钞买入价
     *
     * @param mbuyPrice 现钞买入价
     */
    public void setMbuyPrice(BigDecimal mbuyPrice) {
        this.mbuyPrice = mbuyPrice;
    }

    /**
     * 获取现汇卖出价
     *
     * @return fsell_price - 现汇卖出价
     */
    public BigDecimal getFsellPrice() {
        return fsellPrice;
    }

    /**
     * 设置现汇卖出价
     *
     * @param fsellPrice 现汇卖出价
     */
    public void setFsellPrice(BigDecimal fsellPrice) {
        this.fsellPrice = fsellPrice;
    }

    /**
     * 获取现钞卖出价
     *
     * @return msell_price - 现钞卖出价
     */
    public BigDecimal getMsellPrice() {
        return msellPrice;
    }

    /**
     * 设置现钞卖出价
     *
     * @param msellPrice 现钞卖出价
     */
    public void setMsellPrice(BigDecimal msellPrice) {
        this.msellPrice = msellPrice;
    }

    /**
     * 获取银行折算价/中间价
     *
     * @return bank_conversion_price - 银行折算价/中间价
     */
    public BigDecimal getBankConversionPrice() {
        return bankConversionPrice;
    }

    /**
     * 设置银行折算价/中间价
     *
     * @param bankConversionPrice 银行折算价/中间价
     */
    public void setBankConversionPrice(BigDecimal bankConversionPrice) {
        this.bankConversionPrice = bankConversionPrice;
    }

    /**
     * 获取发布日期
     *
     * @return update_date - 发布日期
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * 设置发布日期
     *
     * @param updateDate 发布日期
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * 获取发布时间
     *
     * @return update_time - 发布时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置发布时间
     *
     * @param updateTime 发布时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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