package com.foreign.exchange.gui;

import com.foreign.exchange.pojo.Bo.StockInfoBo;
import com.foreign.exchange.service.ParseExcelService;
import com.foreign.exchange.service.StockMonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

/**
 *GUI界面
 * @author
 * @create 2020-07-30-15:46
 */
public class GUIMain {
    private Logger logger = LoggerFactory.getLogger(GUIMain.class);
    //根据最新的股票价格更新股票相关信息
    private StockMonitorService stockMonitorService;
    //处理Excel文件导入
//    private ParseExcelService parseExcelService;
    //计算利润

    private JFrame rootFrame;
    private JFileChooser fileChooser;
    private JTable stockTable;
    private StockTableModel stockTableModel;


    public void render(){
        this.rootFrame = new JFrame("股票列表");
        this.rootFrame.setDefaultCloseOperation(3);
    }

    /**
     * 画头部面板
     * @param frame
     */
    private  void createNorthPanel(JFrame frame){
        JPanel northPanel = new JPanel();
        northPanel.setSize(300,500);
        northPanel.setBackground(Color.green);
        FlowLayout layout = new FlowLayout();
        layout.setAlignment(0);
        northPanel.setLayout(layout);
        frame.add(northPanel,"North");
        JButton loadExcelButton = new JButton("加载Excel");
        loadExcelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                GUIMain.this.handleLoadExcelButtonClick();
            }
        });

        northPanel.add(loadExcelButton);
    }

    /**
     * 画中间显示面板
     * @param frame
     */
    private  void createCenterPanel(JFrame frame){
        this.stockTableModel = new StockTableModel(this.stockMonitorService);
        DefaultTableColumnModel tableColumnModel = new DefaultTableColumnModel();
        StockTableCellRenderer cellRenderer = new StockTableCellRenderer();
        TableColumn stockNameColumn = new TableColumn(0);
        stockNameColumn.setHeaderValue("名称");
        stockNameColumn.setCellRenderer(cellRenderer);
        tableColumnModel.addColumn(stockNameColumn);
        TableColumn stockCodeColumn = new TableColumn(1);
        stockNameColumn.setHeaderValue("代码");
        stockNameColumn.setCellRenderer(cellRenderer);
        tableColumnModel.addColumn(stockCodeColumn);
        TableColumn stockNewestPriceColumn = new TableColumn(2);
        stockNameColumn.setHeaderValue("最新价");
        stockNameColumn.setCellRenderer(cellRenderer);
        tableColumnModel.addColumn(stockNewestPriceColumn);
        TableColumn StocknewestUpOrDownColumn = new TableColumn(3);
        stockNameColumn.setHeaderValue("涨跌幅");
        stockNameColumn.setCellRenderer(cellRenderer);
        tableColumnModel.addColumn(StocknewestUpOrDownColumn);
        TableColumn lastPriceColumn = new TableColumn(4);
        lastPriceColumn.setHeaderValue("最后成交");
        lastPriceColumn.setCellRenderer(cellRenderer);
        tableColumnModel.addColumn(lastPriceColumn);
        TableColumn riseOrDropColumn = new TableColumn(5);
        riseOrDropColumn.setHeaderValue("最后涨跌幅");
        riseOrDropColumn.setCellRenderer(cellRenderer);
        tableColumnModel.addColumn(riseOrDropColumn);
        TableColumn stockNumberColumn = new TableColumn(6);
        stockNumberColumn.setHeaderValue("持仓量");
        stockNumberColumn.setCellRenderer(cellRenderer);
        tableColumnModel.addColumn(stockNumberColumn);
        TableColumn feeServiceColumn = new TableColumn(7);
        feeServiceColumn.setHeaderValue("手续费");
        feeServiceColumn.setCellRenderer(cellRenderer);
        tableColumnModel.addColumn(feeServiceColumn);
        TableColumn feeStampColumn = new TableColumn(8);
        feeStampColumn.setHeaderValue("印花税");
        feeStampColumn.setCellRenderer(cellRenderer);
        tableColumnModel.addColumn(feeStampColumn);
        TableColumn diffAmountColumn = new TableColumn(9);
        diffAmountColumn.setHeaderValue("差额");
        diffAmountColumn.setCellRenderer(cellRenderer);
        tableColumnModel.addColumn(diffAmountColumn);
        TableColumn remarkColumn = new TableColumn(10);
        remarkColumn.setHeaderValue("备注");
        remarkColumn.setCellRenderer(cellRenderer);
        tableColumnModel.addColumn(remarkColumn);

        this.stockTable = new JTable(this.stockTableModel,tableColumnModel);
        this.stockTable.setSelectionMode(0);
        this.stockTable.setRowSorter(new TableRowSorter<>(this.stockTableModel));
        cellRenderer.setStockMonitorService(this.stockMonitorService);
        cellRenderer.setStockTable(this.stockTable);

        this.stockTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
//                GUIMain.this.handle
            }
        });

        JScrollPane scrollPane = new JScrollPane(this.stockTable);
        frame.add(scrollPane,"Center");

    }

    /**
     * 处理excel导入
     */
    private  void handleLoadExcelButtonClick(){
        Font font = this.fileChooser.getFont();
        int returnVal = this.fileChooser.showOpenDialog(this.rootFrame);
        if (returnVal == 0){
            File excelFile = this.fileChooser.getSelectedFile();

            try {
//                List<StockInfoBo> stockList  = this.par
            }catch (Exception ex){

            }
        }


    }

}
