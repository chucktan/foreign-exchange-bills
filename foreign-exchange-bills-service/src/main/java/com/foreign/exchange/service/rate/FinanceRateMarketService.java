package com.foreign.exchange.service.rate;

import com.foreign.exchange.pojo.RmbQuote;
import com.foreign.exchange.pojo.Vo.RmbQuoteVo;

/**
 * @author
 * @create 2020-07-20-15:34
 */
public interface FinanceRateMarketService {

    /**
     * 获取最新的汇率价格
     * @param rateCode
     */
    public RmbQuoteVo getPrice(String rateCode);
}
