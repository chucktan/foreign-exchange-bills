package com.foreign.exchange.gui.rate;

import com.foreign.exchange.enums.TradeConstant;
import com.foreign.exchange.pojo.Bo.RateInfoBo;
import com.foreign.exchange.pojo.Bo.StockInfoBo;
import com.foreign.exchange.service.rate.RateMonitorService;
import com.foreign.exchange.service.stock.StockMonitorService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author
 * @create 2020-07-30-16:39
 */
public class RateTableCellRenderer extends DefaultTableCellRenderer {
    private Font normalFont = new Font("Dialog",0,12);
    private Font riseOrDropFont = new Font("Dialog",1,12);
    private Color dropColor = new Color(0,139,0);
    private Color backgroundColor = new Color(250,250,240);

    private JTable rateTable;

    private RateMonitorService rateMonitorService;

    public void setRateTable(JTable rateTable) {
        this.rateTable = rateTable;
    }

    public void setRateMonitorService(RateMonitorService rateMonitorService) {
        this.rateMonitorService = rateMonitorService;
    }


    /**
     * 返回用于绘制单元格的组件。此方法用于在绘制前适当地配置渲染器。
     * @param table
     * @param value
     * @param isSelected
     * @param hasFocus
     * @param row  行索引
     * @param column 列索引
     * @return
     */
    public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int column){
        Component component = super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
        Color foreground = Color.black;
        Font font = this.normalFont;
        //非涨幅列
        if (column !=4){
            if (column ==2 &&value !=null){
                //转换成的模型列
                int modelRow = this.rateTable.convertRowIndexToModel(row);
                RateInfoBo rateInfo = this.rateMonitorService.getRatebyIndex(modelRow);
                if (rateInfo !=null && rateInfo.getTradeFlag()!=null){
                    //如果预设为买,背景为绿
                    if (rateInfo.getTradeFlag().equals(TradeConstant.TRADE_FLAG_BUY.type)){
                        foreground = this.dropColor;
                        font = this.riseOrDropFont;
                    //如果预设为卖，背景为红
                    }else if(rateInfo.getTradeFlag().equals(TradeConstant.TRADE_FLAG_SELL.type)){
                        foreground = Color.red;
                        font = this.riseOrDropFont;
                    }
                }
            }
        }else if(value != null){
            Double rate = (Double)value;
            if (rate > 0.0D){
                foreground = Color.red;
                font = this.riseOrDropFont;
            }else if(rate < 0.0D){
                foreground = this.dropColor;
                font = this.riseOrDropFont;
            }

        }
        this.setForeground(foreground);
        this.setFont(font);
        return  component;
    }

}
