package com.foreign.exchange.enums;

/**
 * @author
 * @create 2020-07-31-15:18
 */
public enum  TradeConstant {

    TRADE_FLAG_INIT(0,"无"),
    TRADE_FLAG_BUY(1,"买"),
    TRADE_FLAG_SELL(2,"卖");

    public final  Integer type;
    public final  String  value;

    TradeConstant(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
