package com.foreign.exchange.pojo.Vo;

/**
 * 实际交易对
 * @author
 * @create 2020-07-31-10:11
 */
public class StockPairInfoVo {
    private  Integer pairCode;
    private  Integer pairNumber;

    private  StockTransactionInfoVo transactionInfo;

    public Integer getPairCode() {
        return pairCode;
    }

    public void setPairCode(Integer pairCode) {
        this.pairCode = pairCode;
    }

    public Integer getPairNumber() {
        return pairNumber;
    }

    public void setPairNumber(Integer pairNumber) {
        this.pairNumber = pairNumber;
    }

    public StockTransactionInfoVo getTransactionInfo() {
        return transactionInfo;
    }

    public void setTransactionInfo(StockTransactionInfoVo transactionInfo) {
        this.transactionInfo = transactionInfo;
    }
}
