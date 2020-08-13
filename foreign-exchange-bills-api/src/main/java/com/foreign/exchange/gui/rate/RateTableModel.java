package com.foreign.exchange.gui.rate;

import com.foreign.exchange.pojo.Bo.RateInfoBo;
import com.foreign.exchange.pojo.Bo.StockInfoBo;
import com.foreign.exchange.service.rate.RateMonitorService;
import com.foreign.exchange.service.stock.StockMonitorService;

import javax.swing.table.AbstractTableModel;

/**
 * 更新股票信息Model
 * @author
 * @create 2020-07-30-16:01
 */
public class RateTableModel extends AbstractTableModel {
    public static final int COLUMN_rateName = 0;
    public static final int COLUMN_rateCode = 1;
    public static final int COLUMN_lastPrice = 2;
    public static final int COLUMN_newestPrice = 3;
    public static final int COLUMN_riseOrDrop = 4;
    public static final int COLUMN_rateNumber = 5;
    public static final int COLUMN_feeService = 6;
    public static final int COLUMN_feeStamp = 7;
    public static final int COLUMN_diffAmount = 8;
    public static final int COLUMN_remark = 9;

    private RateMonitorService rateMonitorService;
    private String[] columnNames = new String[]{"名称", "代码", "最后成交价", "最新价", "最后成交价涨跌幅(%)", "持仓量", "手续费", "印花税", "差额", "备注"};
    private Class[] columnClasses = new Class[]{String.class, String.class, Double.class, Double.class, Double.class, Integer.class, Double.class, Double.class, Double.class, String.class};


    public RateTableModel(RateMonitorService rateMonitorService) {
        this.rateMonitorService = rateMonitorService;
    }

    @Override
    public int getRowCount() {
        return  this.rateMonitorService.getRateList().size();
    }

    @Override
    public int getColumnCount() {
        return this.columnClasses.length;
    }

    public String getColumnName(int column) {
        return this.columnNames[column];
    }

    public  Class<?> getColumnClass(int column){
       return this.columnClasses[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        RateInfoBo rateInfo = this.rateMonitorService.getRateList().get(rowIndex);
        if (rateInfo == null){
            return  null;
        }else{
            Object value = null;
            if (columnIndex == 0){
                value = rateInfo.getRateName();
            }else if (columnIndex == 1){
                value = rateInfo.getRateCode();
            }else if (columnIndex == 2){
                value = rateInfo.getLastPrice();
            } else if (columnIndex == 3){
                value = rateInfo.getNewestPrice();
            }else if(columnIndex == 4){
                value = rateInfo.getRiseOrDrop();
            }else if(columnIndex == 5){
                value = rateInfo.getRateNumber();
            }else if(columnIndex == 6){
                value = rateInfo.getFeeService();
            }else if(columnIndex == 7){
                value = rateInfo.getFeeStamp();
            }else if(columnIndex == 8){
                value = rateInfo.getDiffAmount();
            }else if(columnIndex == 9){
                value = rateInfo.getRemark();
            }
            return  value;
        }

    }
}
