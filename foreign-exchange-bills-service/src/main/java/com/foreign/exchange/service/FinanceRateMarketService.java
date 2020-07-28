package com.foreign.exchange.service;

import com.foreign.exchange.pojo.FinanceRatePrice;
import com.foreign.exchange.pojo.RmbQuote;

/**
 * @author
 * @create 2020-07-20-15:34
 */
public interface FinanceRateMarketService {

    /**
     * 获取最新的汇率价格
     * @param rateCode
     */
    public RmbQuote getPrice(String rateCode);
}
