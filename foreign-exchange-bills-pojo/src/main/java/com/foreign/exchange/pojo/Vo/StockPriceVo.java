package com.foreign.exchange.pojo.Vo;

/**
 * 股票价格Vo
 * @author
 * @create 2020-07-23-16:35
 */
public class StockPriceVo {
    private String code;
    private String name;
    private Double preClosePrice;
    private Double nowPrice;
    private Double highPrice;
    private Double lowPrice;
    private String priceDate;
    private String priceTime;
    private Long updateTime;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPreClosePrice() {
        return preClosePrice;
    }

    public void setPreClosePrice(Double preClosePrice) {
        this.preClosePrice = preClosePrice;
    }

    public Double getNowPrice() {
        return nowPrice;
    }

    public void setNowPrice(Double nowPrice) {
        this.nowPrice = nowPrice;
    }

    public Double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(Double highPrice) {
        this.highPrice = highPrice;
    }

    public Double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(Double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public String getPriceDate() {
        return priceDate;
    }

    public void setPriceDate(String priceDate) {
        this.priceDate = priceDate;
    }

    public String getPriceTime() {
        return priceTime;
    }

    public void setPriceTime(String priceTime) {
        this.priceTime = priceTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "StockPriceVo{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", preClosePrice=" + preClosePrice +
                ", nowPrice=" + nowPrice +
                ", highPrice=" + highPrice +
                ", lowPrice=" + lowPrice +
                ", priceDate='" + priceDate + '\'' +
                ", priceTime='" + priceTime + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}
