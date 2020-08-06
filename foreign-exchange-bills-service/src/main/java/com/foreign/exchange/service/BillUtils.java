package com.foreign.exchange.service;

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
}
