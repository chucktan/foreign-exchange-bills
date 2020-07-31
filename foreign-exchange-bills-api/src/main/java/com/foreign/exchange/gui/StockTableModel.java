package com.foreign.exchange.gui;

import com.foreign.exchange.service.StockMonitorService;

import javax.swing.table.AbstractTableModel;

/**
 * 更新股票信息Model
 * @author
 * @create 2020-07-30-16:01
 */
public class StockTableModel extends AbstractTableModel {
    public static final int COLUMN_stockName = 0;
    public static final int COLUMN_stockCode = 1;
    public static final int COLUMN_newestPrice = 2;
    public static final int COLUMN_newestUpOrDown = 3;
    public static final int COLUMN_lastPrice = 4;
    public static final int COLUMN_riseOrDrop = 5;
    public static final int COLUMN_stockNumber = 6;
    public static final int COLUMN_feeService = 7;
    public static final int COLUMN_feeStamp = 8;
    public static final int COLUMN_diffAmount = 9;
    public static final int COLUMN_remark = 10;

    private StockMonitorService stockMonitorService;
    private String[] columnNames = new String[]{"名称", "代码", "最新价", "涨跌幅", "最后成交", "最后涨跌幅", "持仓量", "手续费", "印花税", "差额", "备注"};
    private Class[] columnClasses = new Class[]{String.class, String.class, Double.class, Double.class, Double.class, Double.class, Integer.class, Double.class, Double.class, Double.class, String.class};


    public StockTableModel(StockMonitorService stockMonitorService) {
        this.stockMonitorService = stockMonitorService;
    }

    @Override
    public int getRowCount() {
        return  0;
    }

    @Override
    public int getColumnCount() {
        return 0;
    }

    public String getColumnName(int column) {
        return this.columnNames[column];
    }

   public  Class<?> getColumnClass(int column){
       return this.columnClasses[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return  null;
    }
}
