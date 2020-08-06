package com.foreign.exchange.service;

import com.foreign.exchange.pojo.Vo.StockPairInfoVo;
import com.foreign.exchange.pojo.Vo.StockTransactionInfoVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 计算股票交易信息
 * @author
 * @create 2020-08-06-16:32
 */
public class CalcStockContext {

    private int pairIndex = 1;
    private List<StockPairInfoVo> pairList = new ArrayList<>();
    private StockTransactionInfoVo transactionInfo;
    private String currBusinessName;
    private int currTradeNumber;


    /**
     * 初始化为卖方
     * @param recordDto
     */
    public void initRecord(StockTransactionInfoVo recordDto){
        String businessName = recordDto.getStockName();
        if (BillUtils.isCloseRecord(recordDto)){
            this.pairList.clear();
            this.transactionInfo = recordDto;
            this.currBusinessName = businessName;
            this.currTradeNumber = recordDto.getStockNumber();//交易数量

        }
    }
}
