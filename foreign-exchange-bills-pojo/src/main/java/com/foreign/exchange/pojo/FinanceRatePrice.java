package com.foreign.exchange.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name = "finance_rate_price")
public class FinanceRatePrice {
    /**
     * 汇率信息ID
     */
    @Id
    @Column(name = "finance_rate_id")
    private String financeRateId;

    /**
     * 货币代码
     */
    @Column(name = "curreny_code")
    private String currenyCode;

    /**
     * 最新价
     */
    @Column(name = "close_price")
    private BigDecimal closePrice;

    /**
     * 涨跌%
     */
    @Column(name = "diff_percent")
    private BigDecimal diffPercent;

    /**
     * 涨跌金额
     */
    @Column(name = "diff_amount")
    private BigDecimal diffAmount;

    /**
     * 开盘价
     */
    @Column(name = "open_price")
    private BigDecimal openPrice;

    /**
     * 最高价
     */
    @Column(name = "hign_price")
    private BigDecimal hignPrice;

    /**
     * 最低价
     */
    @Column(name = "low_price")
    private BigDecimal lowPrice;

    /**
     * 震幅%
     */
    @Column(name = "price_range")
    private BigDecimal priceRange;

    /**
     * 买入价
     */
    @Column(name = "buy_price")
    private BigDecimal buyPrice;

    /**
     * 卖出价
     */
    @Column(name = "sell_price")
    private BigDecimal sellPrice;

    /**
     * 涨跌颜色
     */
    private String color;

    /**
     * 昨收价
     */
    @Column(name = "yes_price")
    private BigDecimal yesPrice;

    /**
     * 日期
     */
    @Column(name = "update_date")
    private String updateDate;

    /**
     * 数据时间
     */
    @Column(name = "update_time")
    private String updateTime;

    /**
     * 创建时间
     */
    @Column(name = "CREATED_TIME")
    private String createdTime;

    /**
     * 创建人
     */
    @Column(name = "CREATED_BY")
    private String createdBy;

    /**
     * 获取汇率信息ID
     *
     * @return finance_rate_id - 汇率信息ID
     */
    public String getFinanceRateId() {
        return financeRateId;
    }

    /**
     * 设置汇率信息ID
     *
     * @param financeRateId 汇率信息ID
     */
    public void setFinanceRateId(String financeRateId) {
        this.financeRateId = financeRateId;
    }

    /**
     * 获取货币代码
     *
     * @return curreny_code - 货币代码
     */
    public String getCurrenyCode() {
        return currenyCode;
    }

    /**
     * 设置货币代码
     *
     * @param currenyCode 货币代码
     */
    public void setCurrenyCode(String currenyCode) {
        this.currenyCode = currenyCode;
    }

    /**
     * 获取最新价
     *
     * @return close_price - 最新价
     */
    public BigDecimal getClosePrice() {
        return closePrice;
    }

    /**
     * 设置最新价
     *
     * @param closePrice 最新价
     */
    public void setClosePrice(BigDecimal closePrice) {
        this.closePrice = closePrice;
    }

    /**
     * 获取涨跌%
     *
     * @return diff_percent - 涨跌%
     */
    public BigDecimal getDiffPercent() {
        return diffPercent;
    }

    /**
     * 设置涨跌%
     *
     * @param diffPercent 涨跌%
     */
    public void setDiffPercent(BigDecimal diffPercent) {
        this.diffPercent = diffPercent;
    }

    /**
     * 获取涨跌金额
     *
     * @return diff_amount - 涨跌金额
     */
    public BigDecimal getDiffAmount() {
        return diffAmount;
    }

    /**
     * 设置涨跌金额
     *
     * @param diffAmount 涨跌金额
     */
    public void setDiffAmount(BigDecimal diffAmount) {
        this.diffAmount = diffAmount;
    }

    /**
     * 获取开盘价
     *
     * @return open_price - 开盘价
     */
    public BigDecimal getOpenPrice() {
        return openPrice;
    }

    /**
     * 设置开盘价
     *
     * @param openPrice 开盘价
     */
    public void setOpenPrice(BigDecimal openPrice) {
        this.openPrice = openPrice;
    }

    /**
     * 获取最高价
     *
     * @return hign_price - 最高价
     */
    public BigDecimal getHignPrice() {
        return hignPrice;
    }

    /**
     * 设置最高价
     *
     * @param hignPrice 最高价
     */
    public void setHignPrice(BigDecimal hignPrice) {
        this.hignPrice = hignPrice;
    }

    /**
     * 获取最低价
     *
     * @return low_price - 最低价
     */
    public BigDecimal getLowPrice() {
        return lowPrice;
    }

    /**
     * 设置最低价
     *
     * @param lowPrice 最低价
     */
    public void setLowPrice(BigDecimal lowPrice) {
        this.lowPrice = lowPrice;
    }

    /**
     * 获取震幅%
     *
     * @return price_range - 震幅%
     */
    public BigDecimal getPriceRange() {
        return priceRange;
    }

    /**
     * 设置震幅%
     *
     * @param priceRange 震幅%
     */
    public void setPriceRange(BigDecimal priceRange) {
        this.priceRange = priceRange;
    }

    /**
     * 获取买入价
     *
     * @return buy_price - 买入价
     */
    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    /**
     * 设置买入价
     *
     * @param buyPrice 买入价
     */
    public void setBuyPrice(BigDecimal buyPrice) {
        this.buyPrice = buyPrice;
    }

    /**
     * 获取卖出价
     *
     * @return sell_price - 卖出价
     */
    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    /**
     * 设置卖出价
     *
     * @param sellPrice 卖出价
     */
    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    /**
     * 获取涨跌颜色
     *
     * @return color - 涨跌颜色
     */
    public String getColor() {
        return color;
    }

    /**
     * 设置涨跌颜色
     *
     * @param color 涨跌颜色
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * 获取昨收价
     *
     * @return yes_price - 昨收价
     */
    public BigDecimal getYesPrice() {
        return yesPrice;
    }

    /**
     * 设置昨收价
     *
     * @param yesPrice 昨收价
     */
    public void setYesPrice(BigDecimal yesPrice) {
        this.yesPrice = yesPrice;
    }

    /**
     * 获取日期
     *
     * @return update_date - 日期
     */
    public String getUpdateDate() {
        return updateDate;
    }

    /**
     * 设置日期
     *
     * @param updateDate 日期
     */
    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * 获取数据时间
     *
     * @return update_time - 数据时间
     */
    public String getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置数据时间
     *
     * @param updateTime 数据时间
     */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取创建时间
     *
     * @return CREATED_TIME - 创建时间
     */
    public String getCreatedTime() {
        return createdTime;
    }

    /**
     * 设置创建时间
     *
     * @param createdTime 创建时间
     */
    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
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
}