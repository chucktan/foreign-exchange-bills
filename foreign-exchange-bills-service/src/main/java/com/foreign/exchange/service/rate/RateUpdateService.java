package com.foreign.exchange.service.rate;

import com.foreign.exchange.pojo.Bo.RateInfoBo;
import com.foreign.exchange.pojo.Bo.StockInfoBo;
import com.foreign.exchange.pojo.Vo.RmbQuoteVo;
import com.foreign.exchange.pojo.Vo.StockPriceVo;
import com.foreign.exchange.service.stock.StockMarketService;
import com.foreign.exchange.service.stock.StockMonitorService;
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
public class RateUpdateService {

    private Logger logger = LoggerFactory.getLogger(RateUpdateService.class);

    private  long interval = 300000L;//等待时间为300秒
    private  boolean isRun = false;
    private RateMonitorService rateMonitorService;
    private FinanceRateMarketService rateMarketService;


    public void setRateMarketService(FinanceRateMarketService rateMarketService) {
        this.rateMarketService = rateMarketService;
    }

    public void setRateMonitorService(RateMonitorService rateMonitorService) {
        this.rateMonitorService = rateMonitorService;
    }

    public  synchronized  void startUpdateEveryTime(){
        if (this.isRun){
            this.logger.warn("already run");
        }else {
            this.isRun = true;
            Thread updateThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    RateUpdateService.this.exec();
                }
            },"RateUpdateService");
            updateThread.start();
        }
    }


    private void  exec(){
        while (isRun){
            try {
                this.doUpdate();
            }catch (Exception ex){
                this.logger.error("更新外汇失败：",ex);
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
        this.logger.debug("开始更新外汇");
        List<RateInfoBo> allRates = this.rateMonitorService.getAllRate();
        if (allRates != null && allRates.size()>0){
            final Map<String, RmbQuoteVo> priceMap = new HashMap<>();
            Iterator iterator = allRates.iterator();

            while (iterator.hasNext()){
                RateInfoBo rateInfo = (RateInfoBo) iterator.next();
                //获取最新的外汇价格
                RmbQuoteVo ratePrice = this.rateMarketService.getPrice(rateInfo.getRateCode());
                if (ratePrice != null){
                    priceMap.put(rateInfo.getRateCode(),ratePrice);
                }
            }
            if (priceMap.isEmpty()){
                this.logger.debug("没有获得最新的外汇");
            }else {
                this.logger.debug("更新外汇数量："+priceMap.size());
                EventQueue.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        //更新股票价格
                        RateUpdateService.this.rateMonitorService.updateRatePrice(priceMap);
                    }
                });
            }

        }else{
            this.logger.debug("没有外汇");
        }
        this.logger.debug("结束更新外汇");
    }

    public  void notifyUpdateThread(){
        synchronized (this){
            this.notify();
        }
    }

}
