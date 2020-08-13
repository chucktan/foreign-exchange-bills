package com.foreign.exchange.service;

import com.foreign.exchange.pojo.Vo.PairInfoVo;
import com.foreign.exchange.pojo.Vo.TransactionInfoVo;

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
    public static void clearRecordPair(List<TransactionInfoVo> recordList){
        TransactionInfoVo record;
        for (Iterator iterator = recordList.iterator(); iterator.hasNext();record.setDiffPrice((Double)null)){
            record = (TransactionInfoVo) iterator.next();
            if (record.getPairList() != null){
                record.getPairList().clear();
            }
        }

    }

    /**
     * 卖
     * @param transactionInfo
     * @return
     */
    public  static  boolean isCloseRecord(TransactionInfoVo transactionInfo)
    {
        return "卖".equals(transactionInfo.getBuyOrSell());
    }

    /**
     * 买
     * @param transactionInfo
     * @return
     */
    public  static  boolean isOpenRecord(TransactionInfoVo transactionInfo){
        return "买".equals(transactionInfo.getBuyOrSell());
    }

    /**
     * 计算剩余可交易数量=当前stockNumber-sum(所有pair.stockNum)
     * @param record
     * @return
     */
    public  static  int getNotPairNumber(TransactionInfoVo record){
        int totalPairNumber = 0;
        PairInfoVo pair;
        if (record.getPairList() !=null ){
            for (Iterator iterator = record.getPairList().iterator();iterator.hasNext();totalPairNumber+=pair.getPairNumber()){
                pair = (PairInfoVo) iterator.next();
            }
        }
        //剩余可交易额度=交易数量-已交易数量
        return  record.getTransNumber() - totalPairNumber;

    }

    /**
     * 编辑交易数量（20）,（15）
     * @param pairList
     * @param tradeNumber
     * @return
     */
    public  static  String makePairAttr(List<PairInfoVo> pairList, int tradeNumber){
        if (pairList !=null && !pairList.isEmpty()){
            StringBuffer sb = new StringBuffer();

            for (int i=0;i<pairList.size();i++){
                PairInfoVo pair = pairList.get(i);
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
