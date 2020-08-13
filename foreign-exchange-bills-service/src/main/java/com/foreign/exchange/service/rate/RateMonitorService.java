package com.foreign.exchange.service.rate;

import com.foreign.exchange.enums.TradeConstant;
import com.foreign.exchange.pojo.Bo.RateInfoBo;
import com.foreign.exchange.pojo.Bo.StockInfoBo;
import com.foreign.exchange.pojo.RmbQuote;
import com.foreign.exchange.pojo.Vo.RmbQuoteVo;
import com.foreign.exchange.pojo.Vo.StockPriceVo;
import com.foreign.exchange.pojo.Vo.TransactionInfoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @create 2020-07-31-14:02
 */
public class RateMonitorService {

    private Logger logger = LoggerFactory.getLogger(RateMonitorService.class);

    private List<RateInfoBo> rateList = new ArrayList<>();
    private RateMonitorListener rateMonitorListener;

    public List<RateInfoBo> getRateList() {
        return rateList;
    }

    public void setRateList(List<RateInfoBo> rateList) {
        this.rateList.clear();
        if (rateList != null){
            this.rateList.addAll(rateList);
        }
        this.rateMonitorListener.fireTableDataChanged();

    }


    public void setRateMonitorListener(RateMonitorListener rateMonitorListener) {
        this.rateMonitorListener = rateMonitorListener;
    }

    /**
     * 获取所有的外汇（代码+名称）
     * @return
     */
    public  List<RateInfoBo> getAllRate(){
        List<RateInfoBo> allRates = new ArrayList<>();
        Iterator iterator = this.rateList.iterator();
        while (iterator.hasNext()){
            RateInfoBo rateInfoBo = (RateInfoBo) iterator.next();
            RateInfoBo ri = new RateInfoBo();
            ri.setRateCode(rateInfoBo.getRateCode());
            ri.setRateName(rateInfoBo.getRateName());
            allRates.add(ri);
        }
        return  allRates;
    }


    public  RateInfoBo getRatebyIndex(int rowIndex){
        return  rowIndex>= this.rateList.size()?null:(RateInfoBo)this.rateList.get(rowIndex);
    }

    /**
     * 根据最新价格，更新外汇相关信息
     * @param priceMap
     */
    public  void updateRatePrice(Map<String, RmbQuoteVo> priceMap){
        boolean haveUpdate = false;
        boolean updateTradeFlag = false;
        Iterator iterator = this.rateList.iterator();
        while (true){
            RateInfoBo rateInfo;
            RmbQuoteVo newRmbQuote;
            do{
                do {
                    do {
                        if (!iterator.hasNext()){
                            //如果最新价更新，前端渲染面板
                            if (haveUpdate){
                                this.rateMonitorListener.fireTableDataChanged();
                            }
                            //如果更新利润等信息，前端置顶
                            if (updateTradeFlag){
                                this.rateMonitorListener.updateTradeFlag();
                            }
                            return;
                        }

                        rateInfo = (RateInfoBo) iterator.next();
                        newRmbQuote = (RmbQuoteVo) priceMap.get(rateInfo.getRateCode());
                    }while (newRmbQuote ==null);
                }while (newRmbQuote.getBankConversionPrice() == null);
                //如果股票记录的最新价与最新获取的价一样则不更新，否则更新股票当前利润等相关信息，并前端渲染面板，并置顶利润更新项
            }while (rateInfo.getNewestPrice() !=null && rateInfo.getNewestPrice() == newRmbQuote.getBankConversionPrice());

            haveUpdate = true;
            boolean tf = this.updateOneRate(rateInfo,newRmbQuote);
            if (tf){
                updateTradeFlag = true;
            }
        }
    }

    /**
     * 根据获取最新的外汇价格，更新单只外汇最新价，交易涨跌幅度，建议交易方向，建议交易价格，市场涨跌幅
     * @param rateInfo
     * @param newRmbQuote
     * @return
     */
    private  boolean updateOneRate(RateInfoBo rateInfo, RmbQuoteVo newRmbQuote){
        boolean updateTradeFlag = false;
        TransactionInfoVo lastTrans = null;
        //含有未计算的交易对
        if (!rateInfo.getTransactionList().isEmpty()){
            //获取最后一笔交易
            lastTrans =  rateInfo.getTransactionList().get(rateInfo.getTransactionList().size() -1);
        }

        //最新价格
        double newPrice = newRmbQuote.getBankConversionPrice();
        //最后交易价格
        double preClosePrice;
        if(lastTrans != null && lastTrans.getPrice() !=null){
            preClosePrice = lastTrans.getPrice();
            //最低价：收盘价95%
            double lowPrice = (double)((int)(preClosePrice*9500.0D))/10000.0D;
            //最高价：收盘价105%
            double highPrice = (double)((int)(preClosePrice*10500.0D))/10000.0D;
            //%涨跌幅
            BigDecimal difPrice = new BigDecimal((newPrice-preClosePrice)*100.0D);
            double rate = difPrice.divide(new BigDecimal(preClosePrice),2,RoundingMode.HALF_UP).doubleValue();
            rateInfo.setRiseOrDrop(rate);//设置最后涨跌幅
            if (rateInfo.getTradeFlag() == null){
                rateInfo.setTradeFlag(TradeConstant.TRADE_FLAG_INIT.type);
                rateInfo.setTradeFlagPrice(newPrice);//初始化为最新价格
//                updateTradeFlag = true;
            }else{
                double fp = rateInfo.getTradeFlagPrice();
                //根据最新价格与最低价、最高价的比较，拟定建议新的交易方向
                //最新价低于最近价
                if (newPrice < lowPrice){
                    //且最新价低于上次交易价（1%变更），建议买
                    if (newPrice *100.0D /fp < 99.0D){
                        this.generateTradeFlag(rateInfo,TradeConstant.TRADE_FLAG_BUY.type,newPrice);
                        updateTradeFlag = true;
                    }
                    //最新价高于最高价，且最新价高于上次交易价（1%变更），建议卖
                }else if (newPrice > highPrice && newPrice *100.0D /fp >101.0D){
                        this.generateTradeFlag(rateInfo,TradeConstant.TRADE_FLAG_SELL.type,newPrice);
                        updateTradeFlag = true;
                }

            }

        }
        //更新最新价
        rateInfo.setNewestPrice(newPrice);
//        if (rateInfo.getPreClosePrice() !=null && rateInfo.getPreClosePrice() > 0.0D){
//            preClosePrice = rateInfo.getPreClosePrice();
//            BigDecimal num1 = new BigDecimal((newPrice - preClosePrice) * 100.0D);
//            BigDecimal num2 = new BigDecimal(preClosePrice);
//            Double ratePrice = num1.divide(num2,2, RoundingMode.HALF_UP).doubleValue();
//            //更新市场涨跌方向
//            rateInfo.setNewestUpOrDown(ratePrice);
//
//        }
        return  updateTradeFlag;


    }

    /**
     * 生成交易方向
     * @param rateInfo
     * @param flag
     * @param price
     */
    private  void generateTradeFlag(RateInfoBo rateInfo,Integer flag,Double price){
        rateInfo.setTradeFlag(flag);
        rateInfo.setTradeFlagPrice(price);
        StringBuffer sb = new StringBuffer();
        String msg = flag.equals(TradeConstant.TRADE_FLAG_BUY.value)?"买":"卖";
        sb.append(msg).append(rateInfo.getRateName());
        sb.append("(").append(rateInfo.getRateCode()).append(")");
        sb.append("，最新价：").append(price);
        this.logger.debug(sb.toString());
    }

}
