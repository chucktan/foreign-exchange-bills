package com.foreign.exchange.pojo.Vo;

import javax.persistence.Column;
import java.math.BigDecimal;

/**
 * @author
 * @create 2020-08-13-10:52
 */
public class RmbQuoteVo {

    private String rmbquotId;

    /**
     * 货币代码
     */
    private String currencyCode;

    /**
     * 货币名称
     */
    private String currenyName;

    /**
     * 现汇买入价
     */
    private Double fbuyPrice;

    /**
     * 现钞买入价
     */
    private Double mbuyPrice;

    /**
     * 现汇卖出价
     */
    private Double fsellPrice;

    /**
     * 现钞卖出价
     */
    private Double msellPrice;

    /**
     * 银行折算价/中间价
     */
    private Double bankConversionPrice;

    /**
     * 发布日期
     */
    private String updateDate;

    /**
     * 发布时间
     */
    private String updateTime;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private String createdTime;


    public String getRmbquotId() {
        return rmbquotId;
    }

    public void setRmbquotId(String rmbquotId) {
        this.rmbquotId = rmbquotId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrenyName() {
        return currenyName;
    }

    public void setCurrenyName(String currenyName) {
        this.currenyName = currenyName;
    }

    public Double getFbuyPrice() {
        return fbuyPrice;
    }

    public void setFbuyPrice(Double fbuyPrice) {
        this.fbuyPrice = fbuyPrice;
    }

    public Double getMbuyPrice() {
        return mbuyPrice;
    }

    public void setMbuyPrice(Double mbuyPrice) {
        this.mbuyPrice = mbuyPrice;
    }

    public Double getFsellPrice() {
        return fsellPrice;
    }

    public void setFsellPrice(Double fsellPrice) {
        this.fsellPrice = fsellPrice;
    }

    public Double getMsellPrice() {
        return msellPrice;
    }

    public void setMsellPrice(Double msellPrice) {
        this.msellPrice = msellPrice;
    }

    public Double getBankConversionPrice() {
        return bankConversionPrice;
    }

    public void setBankConversionPrice(Double bankConversionPrice) {
        this.bankConversionPrice = bankConversionPrice;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }
}
