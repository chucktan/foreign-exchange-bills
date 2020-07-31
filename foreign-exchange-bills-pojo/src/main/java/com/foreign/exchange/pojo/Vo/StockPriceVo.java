package com.foreign.exchange.pojo.Vo;

/**
 * 股票价格Vo(接口更新数据实体)
 * @author
 * @create 2020-07-23-16:35
 */
public class StockPriceVo {
    private String code;//股票代码
    private String name;//股票名称
    private Double preClosePrice;//昨日收盘价
    private Double nowPrice;//当前价格
    private Double highPrice;//最高价
    private Double lowPrice;//最低价
    private String priceDate;//价格日期
    private String priceTime;//价格时间
    private Long updateTime;//更新日期

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
