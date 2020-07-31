package com.foreign.exchange.gui;

import com.foreign.exchange.pojo.Bo.StockInfoBo;
import com.foreign.exchange.service.StockMonitorService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author
 * @create 2020-07-30-16:39
 */
public class StockTableCellRenderer extends DefaultTableCellRenderer {
    private Font normalFont = new Font("Dialog",0,12);
    private Font riseOrDropFont = new Font("Dialog",1,12);
    private Color dropColor = new Color(0,139,0);
    private Color backgroundColor = new Color(250,250,240);

    private JTable stockTable;
    private StockMonitorService stockMonitorService;

    public void setStockTable(JTable stockTable) {
        this.stockTable = stockTable;
    }

    public void setStockMonitorService(StockMonitorService stockMonitorService) {
        this.stockMonitorService = stockMonitorService;
    }

    /**
     * 返回用于绘制单元格的组件。此方法用于在绘制前适当地配置渲染器。
     * @param table
     * @param value
     * @param isSelected
     * @param hasFocus
     * @param row
     * @param column
     * @return
     */
    public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int column){
        Component component = super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
        Color foreground = Color.black;
        Font font = this.normalFont;
        if (column !=3 && column !=5){
            if (column ==2 &&value !=null){
                int modelRow = this.stockTable.convertRowIndexToModel(row);



            }
        }
        return  component;
    }

}
