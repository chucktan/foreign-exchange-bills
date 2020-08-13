package com.foreign.exchange.gui;

import com.foreign.exchange.gui.stock.StockTableCellRenderer;
import com.foreign.exchange.gui.stock.StockTableModel;
import com.foreign.exchange.gui.stock.StockTransactionDialog;
import com.foreign.exchange.pojo.Bo.RateInfoBo;
import com.foreign.exchange.pojo.Bo.StockInfoBo;
import com.foreign.exchange.service.rate.RateMonitorService;
import com.foreign.exchange.service.rate.RateParseExcelService;
import com.foreign.exchange.service.rate.RateUpdateService;
import com.foreign.exchange.service.stock.StockMonitorListener;
import com.foreign.exchange.service.stock.StockMonitorService;
import com.foreign.exchange.service.stock.StockParseExcelService;
import com.foreign.exchange.service.stock.StockUpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

/**
 *GUI界面
 * @author
 * @create 2020-07-30-15:46
 */
public class GUIMain {
    private Logger logger = LoggerFactory.getLogger(GUIMain.class);
    //根据最新的股票价格更新股票相关信息
    private StockMonitorService stockMonitorService;
    //处理Excel文件导入,更新交易信息，计算交易对和交易利润
    private StockParseExcelService stockParseExcelService;
    //更新股票价格
    private StockUpdateService stockUpdateService;

    //根据最新的外汇价格更新股票相关信息
    private RateMonitorService rateMonitorService;
    //处理Excel文件导入,更新交易信息，计算交易对和交易利润
    private RateParseExcelService rateParseExcelService;
    //更新股票价格
    private RateUpdateService rateUpdateService;

    private JFrame rootFrame;
    private JFileChooser fileChooser;
    private JTable stockTable;
    private StockTableModel stockTableModel;
    private StockTransactionDialog stockTransactionDialog;


    public void setStockMonitorService(StockMonitorService stockMonitorService) {
        this.stockMonitorService = stockMonitorService;
    }

    public void setStockParseExcelService(StockParseExcelService stockParseExcelService) {
        this.stockParseExcelService = stockParseExcelService;
    }

    public void setStockUpdateService(StockUpdateService stockUpdateService) {
        this.stockUpdateService = stockUpdateService;
    }

    public void render(){
        this.rootFrame = new JFrame("股票列表");
        this.rootFrame.setDefaultCloseOperation(3);
        this.createNorthPanel(this.rootFrame);
        this.createCenterPanel(this.rootFrame);
        this.initFileChooser();
        this.stockTransactionDialog = new StockTransactionDialog();
        this.stockTransactionDialog.init(this.rootFrame);
        this.listenStockTradeFlag();
        this.rootFrame.setUndecorated(false);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle bounds = new Rectangle(screenSize);
        this.rootFrame.setBounds(bounds);
        this.rootFrame.setExtendedState(6);
        this.rootFrame.setVisible(true);
        this.rootFrame.toFront();


    }

    /**
     * 画头部面板
     * @param frame
     */
    private  void createNorthPanel(JFrame frame){
        JPanel northPanel = new JPanel();
        northPanel.setSize(300,50);
        northPanel.setBackground(Color.green);
        FlowLayout layout = new FlowLayout();
        layout.setAlignment(0);
        northPanel.setLayout(layout);
        frame.add(northPanel,"North");
        JButton loadStockExcelButton = new JButton("加载股票Excel");
        loadStockExcelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUIMain.this.handleLoadStockExcelButtonClick();
            }
        });

        JButton loadRateExcelButton = new JButton("加载外汇Excel");
        loadRateExcelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUIMain.this.handleLoadRateExcelButtonClick();
            }
        });

        northPanel.add(loadStockExcelButton);
        northPanel.add(loadRateExcelButton);
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
        stockCodeColumn.setHeaderValue("代码");
        stockCodeColumn.setCellRenderer(cellRenderer);
        tableColumnModel.addColumn(stockCodeColumn);
        TableColumn stockNewestPriceColumn = new TableColumn(2);
        stockNewestPriceColumn.setHeaderValue("最新价");
        stockNewestPriceColumn.setCellRenderer(cellRenderer);
        tableColumnModel.addColumn(stockNewestPriceColumn);
        TableColumn StockNewestUpOrDownColumn = new TableColumn(3);
        StockNewestUpOrDownColumn.setHeaderValue("涨跌幅(%)");
        StockNewestUpOrDownColumn.setCellRenderer(cellRenderer);
        tableColumnModel.addColumn(StockNewestUpOrDownColumn);
        TableColumn lastPriceColumn = new TableColumn(4);
        lastPriceColumn.setHeaderValue("最后成交");
        lastPriceColumn.setCellRenderer(cellRenderer);
        tableColumnModel.addColumn(lastPriceColumn);
        TableColumn riseOrDropColumn = new TableColumn(5);
        riseOrDropColumn.setHeaderValue("最后涨跌幅(%)");
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
        this.stockTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//只能选择一项
        this.stockTable.setRowSorter(new TableRowSorter<>(this.stockTableModel));
        cellRenderer.setStockTable(this.stockTable);
        cellRenderer.setStockMonitorService(this.stockMonitorService);


        this.stockTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

             GUIMain.this.handleStockRowDoubleClick(e);

            }
        });

        JScrollPane scrollPane = new JScrollPane(this.stockTable);
        frame.add(scrollPane,"Center");

    }

    private  void initFileChooser(){
        String userDir = System.getProperty("user.dir");
        this.fileChooser = new JFileChooser(userDir);
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()){
                    return  true;
                }else{
                    String fn = f.getName();
                    return  fn.endsWith(".xlsx");
                }
            }
            public  String getDescription(){
                return "xlsx";
            }
        };

        this.fileChooser.setFileFilter((filter));
    }

    private  void listenStockTradeFlag(){
        this.stockMonitorService.setStockMonitorListener(new StockMonitorListener() {
            //更新后，渲染中间面板
            @Override
            public void fireTableDataChanged() {
                int selectedRowIndex = GUIMain.this.stockTable.getSelectedRow();
//                int selectedRowIndex = 0;
                int stockIndex = -1;
                StockInfoBo stockInfo =null;
                if (selectedRowIndex >=0){
                    stockIndex = GUIMain.this.stockTable.convertRowIndexToModel(selectedRowIndex);
                    if (stockIndex >= 0){
                        stockInfo = GUIMain.this.stockMonitorService.getStockbyIndex(stockIndex);
                    }
                }

                GUIMain.this.stockTableModel.fireTableDataChanged();
                if (stockInfo !=null){
                    int viewIndex = GUIMain.this.stockTable.convertRowIndexToView(stockIndex);
                    if (viewIndex >=0){
                        GUIMain.this.stockTable.setRowSelectionInterval(viewIndex,viewIndex);
                    }
                }
            }

            //置顶更新
            @Override
            public void updateTradeFlag() {
                GUIMain.this.rootFrame.toFront();//窗口置顶
            }
        });
    }

    /**
     * 处理excel导入,更新交易信息，计算交易对，交易利润，更新股票价格
     */
    private  void handleLoadStockExcelButtonClick(){
        Font font = this.fileChooser.getFont();
        int returnVal = this.fileChooser.showOpenDialog(this.rootFrame);
        if (returnVal == 0){
            File excelFile = this.fileChooser.getSelectedFile();

            try {
                //处理excel导入,更新交易信息，计算交易对，交易利润
                List<StockInfoBo> stockList = this.stockParseExcelService.parseFile(excelFile);
                //添加stockList到stockMonitorService，并更新前端界面
                this.stockMonitorService.setStockList(stockList);
                //更新股票价格
                this.stockUpdateService.notifyUpdateThread();
            }catch (Exception ex){
                this.logger.error("解析excel出错",ex);
            }

        }
    }

    /**
     * 处理excel导入,更新交易信息，计算交易对，交易利润，更新价格
     */
    private  void handleLoadRateExcelButtonClick(){
        Font font = this.fileChooser.getFont();
        int returnVal = this.fileChooser.showOpenDialog(this.rootFrame);
        if (returnVal == 0){
            File excelFile = this.fileChooser.getSelectedFile();

            try {
                //处理excel导入,更新交易信息，计算交易对，交易利润
                List<RateInfoBo> rateList = this.rateParseExcelService.parseFile(excelFile);
                //添加rateList到rateMonitorService，并更新前端界面
                this.rateMonitorService.setRateList(rateList);
                //更新外汇价格
                this.rateUpdateService.notifyUpdateThread();
            }catch (Exception ex){
                this.logger.error("解析excel出错",ex);
            }

        }
    }

    /**
     *
     */
    private  void handleStockRowDoubleClick(MouseEvent e){
        if (e.getClickCount()==2){
            int selectedRowIndex = this.stockTable.getSelectedRow();
            if(selectedRowIndex !=-1){
                selectedRowIndex = this.stockTable.convertRowIndexToModel(selectedRowIndex);
                StockInfoBo stockInfo = this.stockMonitorService.getStockbyIndex(selectedRowIndex);
                if (stockInfo == null){
                    System.out.println("没有找到股票信息");
                }else {
                    this.stockTransactionDialog.showTransactionList(stockInfo);
                }
            }
        }
    }

}
