package com.foreign.exchange.service.stock;

import com.foreign.exchange.pojo.Bo.StockInfoBo;
import com.foreign.exchange.pojo.Vo.StockPriceVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @create 2020-08-10-14:53
 */
public class StockUpdateService {

    private Logger logger = LoggerFactory.getLogger(StockUpdateService.class);

    private  long interval = 30000L;
    private  boolean isRun = false;
    private  StockMonitorService stockMonitorService;
    private  StockMarketService stockMarketService;


    public StockMonitorService getStockMonitorService() {
        return stockMonitorService;
    }

    public void setStockMarketService(StockMarketService stockMarketService) {
        this.stockMarketService = stockMarketService;
    }

    public void setStockMonitorService(StockMonitorService stockMonitorService) {
        this.stockMonitorService = stockMonitorService;
    }

    public  synchronized  void startUpdateEveryTime(){
        if (this.isRun){
            this.logger.warn("already run");
        }else {
            this.isRun = true;
            Thread updateThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    StockUpdateService.this.exec();
                }
            },"StockUpdateService");
            updateThread.start();
        }
    }


    private void  exec(){
        while (isRun){
            try {
                this.doUpdate();
            }catch (Exception ex){
                this.logger.error("更新股票失败：",ex);
            }

            try {
                synchronized (this){
                    this.wait(this.interval);
                }
            }catch (InterruptedException ex){
                this.logger.error("中断",ex);
            }
        }
    }

    private  void doUpdate()  throws  Exception{
        this.logger.debug("开始更新股票");
        List<StockInfoBo> allStocks = this.stockMonitorService.getAllStock();
        if (allStocks != null && allStocks.size()>0){
            final Map<String, StockPriceVo> priceMap = new HashMap<>();
            Iterator iterator = allStocks.iterator();

            while (iterator.hasNext()){
                StockInfoBo stockInfo = (StockInfoBo) iterator.next();
                //获取最新的股票价格
                StockPriceVo stockPrice = this.stockMarketService.getStockPrice(stockInfo.getStockCode());
                if (stockPrice != null){
                    priceMap.put(stockInfo.getStockCode(),stockPrice);
                }
            }
            if (priceMap.isEmpty()){
                this.logger.debug("没有获得最新的股票");
            }else {
                this.logger.debug("更新股票数量："+priceMap.size());
                EventQueue.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        //更新股票价格
                        StockUpdateService.this.stockMonitorService.updateStockPrice(priceMap);
                    }
                });
            }

        }
    }

    public  void notifyUpdateThread(){
        synchronized (this){
            this.notify();
        }
    }

}
