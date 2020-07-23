package com.foreign.exchange.service.impl;

import com.foreign.exchange.pojo.Vo.StockPriceVo;
import com.foreign.exchange.service.StockMarketService;
import com.foreign.exchange.utils.HttpRequestUtils;

/**
 * @author
 * @create 2020-07-23-16:38
 */
public class StockMarketServiceImpl implements StockMarketService {

    private  static  final  String STOCK_URL_PREFIX = "http://hq.sinajs.cn/list=";
    @Override
    public StockPriceVo getStockPrice(String stockCode) {
        String fullCode = getFullCode(stockCode);
        if (fullCode == null){
            throw new  RuntimeException("无法拼接fullCode"+stockCode);
        }else{
            String url = STOCK_URL_PREFIX+fullCode;
            String stockInfoStr = HttpRequestUtils.sendGet(url,"utf-8");
            if (stockInfoStr == null){
                return  null;
            }else {
                int startIndex = stockInfoStr.indexOf("\"");
                if (startIndex == -1){
                    return  null;
                }else{
                    int endIndex= stockInfoStr.indexOf("\"",startIndex+1);
                    if (endIndex == -1){
                        return  null;
                    }else {
                        String targetStr = stockInfoStr.substring(startIndex+1,endIndex);
                        String[] infoParts = targetStr.split(",");
                        if (infoParts.length < 32){
                            return  null;
                        }else {
                            StockPriceVo priceInfo = new StockPriceVo();
                            //priceInfo.setCode(stockCode);
                            priceInfo.setPreClosePrice(Double.valueOf(infoParts[2]));
                            priceInfo.setNowPrice(Double.valueOf(infoParts[3]));
                            priceInfo.setHighPrice(Double.valueOf(infoParts[4]));
                            priceInfo.setLowPrice(Double.valueOf(infoParts[5]));
                            priceInfo.setPriceDate(infoParts[30]);
                            priceInfo.setPriceTime(infoParts[31]);
                            priceInfo.setUpdateTime(System.currentTimeMillis());
                            return  priceInfo;
                        }

                    }
                }
            }

        }
    }


    private  String getFullCode(String stockCode){
        String fullCode = null;
        if (stockCode.startsWith("6")){
            fullCode = "sh" + stockCode;
        }else  if (stockCode.startsWith("5")){
            fullCode = "sh" + stockCode;
        }else if (stockCode.startsWith("1")){
            fullCode = "sz" + stockCode;
        }else {
            if (!stockCode.startsWith("0") && !stockCode.startsWith("3"))
            {
                return  null;
            }
            fullCode = "sz" + stockCode;
        }
        return  fullCode;
    }

    //单元测试
//    public static void main(String[] args) {
//        StockMarketServiceImpl stockMarketService = new StockMarketServiceImpl();
//        StockPriceVo stockPriceVo = stockMarketService.getStockPrice("600036");
//        System.out.println(stockPriceVo.toString());
//
//    }
}
