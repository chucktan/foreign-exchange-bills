package com.foreign.exchange.service;

import com.foreign.exchange.pojo.Vo.StockPriceVo;

/**
 * @author
 * @create 2020-07-23-16:31
 */
public interface StockMarketService {

    public StockPriceVo getStockPrice(String stockCode);

}
