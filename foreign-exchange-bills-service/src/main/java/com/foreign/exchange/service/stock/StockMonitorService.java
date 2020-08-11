package com.foreign.exchange.service.stock;

import com.foreign.exchange.enums.TradeConstant;
import com.foreign.exchange.pojo.Bo.StockInfoBo;
import com.foreign.exchange.pojo.Vo.StockPriceVo;
import com.foreign.exchange.pojo.Vo.StockTransactionInfoVo;

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
public class StockMonitorService  {

    private List<StockInfoBo> stockList = new ArrayList<>();
    private StockMonitorListener stockMonitorListener;

    public List<StockInfoBo> getStockList() {
        return stockList;
    }

    public void setStockList(List<StockInfoBo> stockList) {
        this.stockList.clear();
        if (stockList != null){
            this.stockList.addAll(stockList);
        }
        this.stockMonitorListener.fireTableDataChanged();

    }


    public void setStockMonitorListener(StockMonitorListener stockMonitorListener) {
        this.stockMonitorListener = stockMonitorListener;
    }

    /**
     * 获取所有的股票（代码+名称）
     * @return
     */
    public  List<StockInfoBo> getAllStock(){
        List<StockInfoBo> allStocks = new ArrayList<>();
        Iterator iterator = this.stockList.iterator();
        while (iterator.hasNext()){
            StockInfoBo stockInfoBo = (StockInfoBo) iterator.next();
            StockInfoBo si = new StockInfoBo();
            si.setStockCode(stockInfoBo.getStockCode());
            si.setStockName(stockInfoBo.getStockName());
            allStocks.add(si);
        }
        return  allStocks;
    }


    public  StockInfoBo getStockbyIndex(int rowIndex){
        return  rowIndex>= this.stockList.size()?null:(StockInfoBo)this.stockList.get(rowIndex);
    }

    /**
     * 根据最新价格，更新股票相关信息
     * @param priceMap
     */
    public  void updateStockPrice(Map<String,StockPriceVo> priceMap){
        boolean haveUpdate = false;
        boolean updateTradeFlag = false;
        Iterator iterator = this.stockList.iterator();
        while (true){
            StockInfoBo stockInfo;
            StockPriceVo newPriceInfo;
            do{
                do {
                    do {
                        if (!iterator.hasNext()){
                            if (haveUpdate){
                                this.stockMonitorListener.fireTableDataChanged();
                            }

                            if (updateTradeFlag){
                                this.stockMonitorListener.updateTradeFlag();
                            }
                            return;
                        }

                        stockInfo = (StockInfoBo) iterator.next();
                        newPriceInfo = (StockPriceVo) priceMap.get(stockInfo.getStockCode());
                    }while (newPriceInfo ==null);
                }while (newPriceInfo.getNowPrice() == null);
                //如果股票记录的最新价与最新获取的价一样则不更新。
            }while (stockInfo.getNewestPrice() !=null && stockInfo.getNewestPrice() == newPriceInfo.getNowPrice());

            haveUpdate = true;
            boolean tf = this.updateOneStock(stockInfo,newPriceInfo);
            if (tf){
                updateTradeFlag = true;
            }
        }
    }

    /**
     * 根据获取最新的股票价格，更新单只股票最新价，涨跌幅度，建议交易方向，建议交易价格，市场方向
     * @param stockInfo
     * @param newPriceInfo
     * @return
     */
    private  boolean updateOneStock(StockInfoBo stockInfo, StockPriceVo newPriceInfo){
        boolean updateTradeFlag = false;
        StockTransactionInfoVo lastTrans = null;
        //含有未计算的交易对
        if (!stockInfo.getTransactionList().isEmpty()){
            //获取最后一笔交易
            lastTrans = (StockTransactionInfoVo) stockInfo.getTransactionList().get(stockInfo.getTransactionList().size() -1);
        }

        //最近价格
        double newPrice = newPriceInfo.getNowPrice();
        //收盘价
        double preClosePrice;
        if(lastTrans != null && lastTrans.getPrice() !=null){
            preClosePrice = lastTrans.getPrice();
            //最低价：收盘价95%
            double lowPrice = (double)((int)(preClosePrice*9500.0D))/10000.0D;
            //最高价：收盘价105%
            double highPrice = (double)((int)(preClosePrice*10500.0D))/10000.0D;
            //%涨跌幅
            double rate = (newPrice-preClosePrice)*10000.0D/preClosePrice/100.0D;
            stockInfo.setRiseOrDrop(rate);
            if (stockInfo.getTradeFlag() == null){
                stockInfo.setTradeFlag(TradeConstant.TRADE_FLAG_INIT.type);
                stockInfo.setTradeFlagPrice(newPrice);
            }else{
                double fp = stockInfo.getTradeFlagPrice();
                //根据最新价格与最低价、最高价的比较，拟定建议新的交易方向
                //最新价低于最近价
                if (newPrice < lowPrice){
                    //且最新价低于上次交易价，建议买
                    if (newPrice *100.0D /fp < 99.0D){
                        this.generateTradeFlag(stockInfo,TradeConstant.TRADE_FLAG_BUY.type,newPrice);
                        updateTradeFlag = true;
                    }
                    //最新价高于最高价，且最新价高于上次交易价，建议卖
                }else if (newPrice > highPrice && newPrice *100.0D /fp >101.0D){
                        this.generateTradeFlag(stockInfo,TradeConstant.TRADE_FLAG_SELL.type,newPrice);
                        updateTradeFlag = true;
                }

            }

        }
        //更新最新价
        stockInfo.setNewestPrice(newPrice);
        if (newPriceInfo.getPreClosePrice() !=null && newPriceInfo.getPreClosePrice() > 0.0D){
            preClosePrice = newPriceInfo.getPreClosePrice();
            BigDecimal num1 = new BigDecimal((newPrice - preClosePrice) * 100.0D);
            BigDecimal num2 = new BigDecimal(preClosePrice);
            Double ratePrice = num1.divide(num2,2, RoundingMode.HALF_UP).doubleValue();
            //更新市场涨跌方向
            stockInfo.setNewestUpOrDown(ratePrice);

        }
        return  false;


    }

    /**
     * 生成交易方向
     * @param stockInfo
     * @param flag
     * @param price
     */
    private  void generateTradeFlag(StockInfoBo stockInfo,Integer flag,Double price){
        stockInfo.setTradeFlag(flag);
        stockInfo.setTradeFlagPrice(price);
        StringBuffer sb = new StringBuffer();
        String msg = flag.equals(TradeConstant.TRADE_FLAG_BUY.value)?"买":"卖";
        sb.append(msg).append(stockInfo.getStockName());
        sb.append("(").append(stockInfo.getStockCode()).append(")");
        sb.append("，最新价：").append(price);
        System.out.println(sb.toString());
    }

}
