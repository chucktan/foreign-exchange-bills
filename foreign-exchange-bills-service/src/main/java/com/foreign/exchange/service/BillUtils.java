package com.foreign.exchange.service;

import com.foreign.exchange.pojo.Vo.StockPairInfoVo;
import com.foreign.exchange.pojo.Vo.StockPriceVo;
import com.foreign.exchange.pojo.Vo.StockTransactionInfoVo;

import java.util.Iterator;
import java.util.List;

/**
 * @author
 * @create 2020-08-06-16:06
 */
public class BillUtils {

    /**
     * 清空交易列表中（transactionlist）的交易对（pair）
     * @param recordList
     */
    public static void clearRecordPair(List<StockTransactionInfoVo> recordList){
        StockTransactionInfoVo record;
        for (Iterator iterator = recordList.iterator(); iterator.hasNext();record.setDiffPrice((Double)null)){
            record = (StockTransactionInfoVo) iterator.next();
            if (record.getPairList() != null){
                record.getPairList().clear();
            }
        }

    }

    /**
     * 买
     * @param transactionInfo
     * @return
     */
    public  static  boolean isCloseRecord(StockTransactionInfoVo transactionInfo)
    {
        return "买".equals(transactionInfo.getBuyOrSell());
    }

    /**
     * 卖
     * @param transactionInfo
     * @return
     */
    public  static  boolean isOpenRecord(StockTransactionInfoVo transactionInfo){
        return "卖".equals(transactionInfo.getBuyOrSell());
    }

    /**
     * 计算剩余可交易数量
     * @param record
     * @return
     */
    public  static  int getNotPairNumber(StockTransactionInfoVo record){
        int totalPairNumber = 0;
        StockPairInfoVo pair;
        if (record.getPairList() !=null ){
            for (Iterator iterator = record.getPairList().iterator();iterator.hasNext();totalPairNumber+=pair.getPairNumber()){
                pair = (StockPairInfoVo) iterator.next();
            }
        }
        //剩余可交易额度=交易数量-已交易数量
        return  record.getStockNumber() - totalPairNumber;

    }

    public  static  String makePairAttr(List<StockPairInfoVo> pairList,int tradeNumber){
        if (pairList !=null && !pairList.isEmpty()){
            StringBuffer sb = new StringBuffer();

            for (int i=0;i<pairList.size();i++){
                StockPairInfoVo pair = pairList.get(i);
                if (pair.getPairNumber() == tradeNumber){
                    sb.append(pair.getPairCode());
                }else{
                    sb.append(pair.getPairCode());
                    if (pair.getPairNumber() >1){
                        sb.append("(").append(pair.getPairNumber()).append(")");
                    }
                }
                if (i<pairList.size()-1){
                    sb.append(",");
                }
            }
            return  sb.toString();
        }else {
            return  null;
        }
    }
}
