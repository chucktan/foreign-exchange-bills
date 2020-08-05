package com.foreign.exchange.service;

import com.foreign.exchange.pojo.Vo.StockPriceVo;

import java.util.Map;

/**
 * @author
 * @create 2020-07-30-16:05
 */
public interface StockMonitorService {

    /**
     * 更新股票相关信息
     * @param priceMap
     */
    public  void updateStockPrice(Map<String, StockPriceVo> priceMap);
}
