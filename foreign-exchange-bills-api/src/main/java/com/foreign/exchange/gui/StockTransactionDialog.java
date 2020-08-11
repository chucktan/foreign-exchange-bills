package com.foreign.exchange.gui;

import com.foreign.exchange.pojo.Bo.StockInfoBo;
import com.foreign.exchange.pojo.Vo.StockTransactionInfoVo;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;

/**
 * @author
 * @create 2020-08-11-10:43
 */
public class StockTransactionDialog {
    public static final int COLUMN_date = 0;
    public static final int COLUMN_stockName = 1;
    public static final int COLUMN_stockCode = 2;
    public static final int COLUMN_buyOrSell = 3;
    public static final int COLUMN_stockNumber = 4;
    public static final int COLUMN_price = 5;
    public static final int COLUMN_feeService = 6;
    public static final int COLUMN_feeStamp = 7;
    public static final int COLUMN_pair = 8;
    public static final int COLUMN_diffPrice = 9;

    private  StockTransactionDialog.StockTransactionTableModel tableModel;
    private JDialog dialog;


    public  void  init(JFrame frame){
        this.tableModel = new  StockTransactionDialog.StockTransactionTableModel();
        DefaultTableColumnModel tableColumnModel = new DefaultTableColumnModel();
        TableColumn dateColumn = new TableColumn(0);
        dateColumn.setHeaderValue("日期");
        tableColumnModel.addColumn(dateColumn);
        TableColumn stockNameColumn = new TableColumn(1);
        stockNameColumn.setHeaderValue("股票");
        stockNameColumn.setPreferredWidth(50);
        tableColumnModel.addColumn(stockNameColumn);
        TableColumn stockCodeColumn = new TableColumn(2);
        stockCodeColumn.setHeaderValue("代码");
        stockCodeColumn.setPreferredWidth(50);
        tableColumnModel.addColumn(stockCodeColumn);
        TableColumn buyOrSellColumn = new TableColumn(3);
        buyOrSellColumn.setHeaderValue("买卖");
        buyOrSellColumn.setPreferredWidth(30);
        tableColumnModel.addColumn(buyOrSellColumn);
        TableColumn stockNumberColumn = new TableColumn(4);
        stockNumberColumn.setHeaderValue("数量");
        stockNumberColumn.setPreferredWidth(40);
        tableColumnModel.addColumn(stockNumberColumn);
        TableColumn priceColumn = new TableColumn(5);
        priceColumn.setHeaderValue("价格");
        priceColumn.setPreferredWidth(40);
        tableColumnModel.addColumn(priceColumn);
        TableColumn feeServiceColumn = new TableColumn(6);
        feeServiceColumn.setHeaderValue("佣金");
        feeServiceColumn.setPreferredWidth(40);
        tableColumnModel.addColumn(feeServiceColumn);
        TableColumn feeStampColumn = new TableColumn(7);
        feeStampColumn.setHeaderValue("印花税");
        feeStampColumn.setPreferredWidth(40);
        tableColumnModel.addColumn(feeStampColumn);
        TableColumn pairColumn = new TableColumn(8);
        pairColumn.setHeaderValue("配对");
        pairColumn.setPreferredWidth(40);
        tableColumnModel.addColumn(pairColumn);
        TableColumn diffPriceColumn = new TableColumn(9);
        diffPriceColumn.setHeaderValue("差价");
        diffPriceColumn.setPreferredWidth(40);
        tableColumnModel.addColumn(diffPriceColumn);
        JTable table = new JTable(this.tableModel,tableColumnModel);
        JScrollPane scrollPane = new JScrollPane(table);
        this.dialog = new JDialog(frame,"交易记录");
        this.dialog.add(scrollPane,"Center");
        this.dialog.setSize(720,480);
        this.dialog.setLocationRelativeTo((Component) null);
    }

    public  void showTransactionList(StockInfoBo stockInfo){
        this.tableModel.stockInfo = stockInfo;
        this.tableModel.fireTableDataChanged();
        this.dialog.setVisible(true);
        this.dialog.toFront();
    }

    private  class  StockTransactionTableModel extends AbstractTableModel{
        private StockInfoBo stockInfo;
        private  Class[] columnClasses;

        private  StockTransactionTableModel(){
            this.columnClasses = new Class[]{String.class, String.class, String.class, String.class, Integer.class, Double.class, Double.class, Double.class, String.class, Double.class};
        }

        public int getRowCount(){
            if (this.stockInfo == null){
                return  0;
            }else {
                List<StockTransactionInfoVo> transactionList = this.stockInfo.getTransactionList();
                return  transactionList !=null&& !transactionList.isEmpty()?transactionList.size():0;
            }
        }

        public  int getColumnCount(){
            return this.columnClasses.length;
        }

        public Class<?> getColumnClass(int columnIndex) {
            return this.columnClasses[columnIndex];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            StockTransactionInfoVo transactionInfo = null;
            if (this.stockInfo !=null){
                transactionInfo = this.stockInfo.getTransactionList().get(rowIndex);
            }
            if (transactionInfo == null){
                return null;
            }else {
                Object value = null;
                if (columnIndex == 0){
                    value = transactionInfo.getDate();
                } else if (columnIndex == 1) {
                    value = transactionInfo.getStockName();
                } else if (columnIndex == 2) {
                    value = transactionInfo.getStockCode();
                } else if (columnIndex == 3) {
                    value = transactionInfo.getBuyOrSell();
                } else if (columnIndex == 4) {
                    value = transactionInfo.getStockNumber();
                } else if (columnIndex == 5) {
                    value = transactionInfo.getPrice();
                } else if (columnIndex == 6) {
                    value = transactionInfo.getFeeService();
                } else if (columnIndex == 7) {
                    value = transactionInfo.getFeeStamp();
                } else if (columnIndex == 8) {
                    value = transactionInfo.getPair();
                } else if (columnIndex == 9) {
                    value = transactionInfo.getDiffPrice();
                }

                return  value;

            }
        }
    }
}


