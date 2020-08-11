package com.foreign.exchange.service;

import com.foreign.exchange.pojo.Vo.StockPairInfoVo;
import com.foreign.exchange.pojo.Vo.StockTransactionInfoVo;
import com.foreign.exchange.utils.OptionUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 计算股票交易信息
 * @author
 * @create 2020-08-06-16:32
 */
public class CalcStockContext {

    private int pairIndex = 1;
    private List<StockPairInfoVo> pairList = new ArrayList<>();
    private StockTransactionInfoVo currRecord;
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
            this.currRecord = recordDto;
            this.currBusinessName = businessName;
            this.currTradeNumber = recordDto.getStockNumber();//交易数量
        }else{
            throw  new RuntimeException("不支持的businessName,"+businessName);
        }
    }

    /**
     *计算过程为卖方-买方
     * @param preRecord
     */
    public void buildPair(StockTransactionInfoVo preRecord){
        if (this.currTradeNumber > 0){
            //只选择买
            if (BillUtils.isOpenRecord(preRecord)){
                if (preRecord.getStockNumber() > 0){
                    int preNotPairNumber = BillUtils.getNotPairNumber(preRecord);
                    if (preNotPairNumber > 0){
                        int pairNumber = this.currTradeNumber <= preNotPairNumber?this.currTradeNumber:preNotPairNumber;
                        //将交易pair添加进对应的transaction中
                        StockPairInfoVo pairInfo = new StockPairInfoVo();
                        pairInfo.setPairCode(this.pairIndex);
                        pairInfo.setPairNumber(pairNumber);
                        this.addPairToRecord(preRecord,pairInfo);
                        this.pairList.add(pairInfo);
                        //卖为正
                        double currClearAmount = this.calcClearAmount(this.currRecord,pairNumber);
                        //买为负
                        double preClearAmount = this.calcClearAmount(preRecord,pairNumber);
                        double addDiff = OptionUtils.add(currClearAmount,preClearAmount);
                        double currDiffAmount = this.currRecord.getDiffPrice()==null?0.0D:this.currRecord.getDiffPrice();
                        this.currRecord.setDiffPrice(OptionUtils.add(addDiff,currDiffAmount));
                        this.currTradeNumber -=pairNumber;
                    }
                }
            }
        }
    }


    /**
     * 买方添加实际卖方交易信息
     * @param record
     * @param pairInfo
     */
    private  void addPairToRecord(StockTransactionInfoVo record,StockPairInfoVo pairInfo){
        List<StockPairInfoVo> pairList = record.getPairList();


        if (pairList == null){
            pairList = new ArrayList<>();
            record.setPairList(pairList);
        }

        pairList.add(pairInfo);
    }

    /**
     * 计算交易花费
     * @param record
     * @param pairNumber
     * @return
     */
    private  double calcClearAmount(StockTransactionInfoVo record,int pairNumber){
        double costPrice = OptionUtils.multi(record.getPrice(),(double)record.getStockNumber());
        double feeService = record.getFeeService() == null?0.0D:record.getFeeService();
        double feeStamp = record.getFeeStamp()==null?0.0D:record.getFeeStamp();
        double clearAmount = 0.0D;
        double d1;
        //买=-（交易额+佣金+印花税）,取负
        if ("买".equals(record.getBuyOrSell())){
            d1 = OptionUtils.substract(-costPrice,feeService);
            clearAmount = OptionUtils.substract(d1,feeStamp);
            //卖=交易额-佣金-印花税
        }else {
            d1= OptionUtils.substract(costPrice,feeService);
            clearAmount = OptionUtils.substract(d1,feeStamp);
        }
        //非全额按比例
        return  pairNumber == record.getStockNumber()? clearAmount:OptionUtils.multi(OptionUtils.divide(clearAmount,(double)record.getStockNumber()), pairNumber);
    }

    public  int getCurrTradeNumber(){
        return  this.currTradeNumber;
    }

    public  void finishRecord(){
        if (this.pairList.size() > 0){
            StockPairInfoVo currPair = new StockPairInfoVo();
            currPair.setPairCode(this.pairIndex++);
            int toltalNumber = 0;

            StockPairInfoVo prePair;
            for (Iterator iterator = this.pairList.iterator();iterator.hasNext();toltalNumber += prePair.getPairNumber()){
                prePair = (StockPairInfoVo) iterator.next();
            }

            currPair.setPairNumber(toltalNumber);
            this.addPairToRecord(this.currRecord,currPair);

        }
    }
}
