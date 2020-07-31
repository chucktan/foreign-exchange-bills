package com.foreign.exchange.service.impl;

import com.foreign.exchange.enums.TradeConstant;
import com.foreign.exchange.pojo.Bo.StockInfoBo;
import com.foreign.exchange.pojo.Vo.StockPriceVo;
import com.foreign.exchange.pojo.Vo.StockTransactionInfoVo;
import com.foreign.exchange.service.StockMonitorListener;
import com.foreign.exchange.service.StockMonitorService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author
 * @create 2020-07-31-14:02
 */
public class StockMonitorServiceImpl  implements StockMonitorService {

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
     * 根据获取最新的股票价格，更新单只股票盈亏及持仓信息
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
        double preClosePrice;
        double higePrice;
        if(lastTrans != null && lastTrans.getPrice() !=null){
            preClosePrice = lastTrans.getPrice();
            //95%最低价
            double lowPrice = (double)((int)(preClosePrice*9500.0D))/10000.0D;
            //105%最高价
            double HigePrice = (double)((int)(preClosePrice*10500.0D))/10000.0D;
            //%涨跌幅
            double rate = (newPrice-preClosePrice)*10000.0D/preClosePrice/100.0D;
            stockInfo.setRiseOrDrop(rate);
            if (stockInfo.getTradeFlag() == null){
                stockInfo.setTradeFlag(TradeConstant.TRADE_FLAG_INIT.type);
                stockInfo.setTradeFlagPrice(newPrice);

            }

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
