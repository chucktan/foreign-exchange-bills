package com.foreign.exchange.gui.rate;

import com.foreign.exchange.gui.stock.StockTableCellRenderer;
import com.foreign.exchange.gui.stock.StockTableModel;
import com.foreign.exchange.gui.stock.StockTransactionDialog;
import com.foreign.exchange.pojo.Bo.RateInfoBo;
import com.foreign.exchange.pojo.Bo.StockInfoBo;
import com.foreign.exchange.service.rate.RateMonitorListener;
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
import javax.swing.table.*;
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
public class RateGUIMain {
    private Logger logger = LoggerFactory.getLogger(RateGUIMain.class);
    //根据最新的外汇价格更新股票相关信息
    private RateMonitorService rateMonitorService;
    //处理Excel文件导入,更新交易信息，计算交易对和交易利润
    private RateParseExcelService rateParseExcelService;
    //更新股票价格
    private RateUpdateService rateUpdateService;

    private JFrame rootFrame;
    private JFileChooser fileChooser;
    private JTable rateTable;
    private RateTableModel rateTableModel;
    private RateTransactionDialog rateTransactionDialog;


    public void setRateMonitorService(RateMonitorService rateMonitorService) {
        this.rateMonitorService = rateMonitorService;
    }

    public void setRateParseExcelService(RateParseExcelService rateParseExcelService) {
        this.rateParseExcelService = rateParseExcelService;
    }

    public void setRateUpdateService(RateUpdateService rateUpdateService) {
        this.rateUpdateService = rateUpdateService;
    }

    public void render(){
        this.rootFrame = new JFrame("外汇列表");
        this.rootFrame.setDefaultCloseOperation(3);
        this.createNorthPanel(this.rootFrame);
        this.createCenterPanel(this.rootFrame);
        this.initFileChooser();
        this.rateTransactionDialog = new RateTransactionDialog();
        this.rateTransactionDialog.init(this.rootFrame);
        this.listenRateTradeFlag();
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
        JButton loadExcelButton = new JButton("加载Excel");
        loadExcelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RateGUIMain.this.handleLoadExcelButtonClick();
            }
        });

        northPanel.add(loadExcelButton);
    }

    /**
     * 画中间显示面板
     * @param frame
     */
    private  void createCenterPanel(JFrame frame){
        this.rateTableModel = new RateTableModel(this.rateMonitorService);
        DefaultTableColumnModel tableColumnModel = new DefaultTableColumnModel();
        RateTableCellRenderer cellRenderer = new RateTableCellRenderer();
        TableColumn rateNameColumn = new TableColumn(0);
        rateNameColumn.setHeaderValue("名称");
        rateNameColumn.setCellRenderer(cellRenderer);
        tableColumnModel.addColumn(rateNameColumn);
        TableColumn rateCodeColumn = new TableColumn(1);
        rateCodeColumn.setHeaderValue("代码");
        rateCodeColumn.setCellRenderer(cellRenderer);
        tableColumnModel.addColumn(rateCodeColumn);
        TableColumn lastPriceColumn = new TableColumn(2);
        lastPriceColumn.setHeaderValue("最后成交价");
        lastPriceColumn.setCellRenderer(cellRenderer);
        tableColumnModel.addColumn(lastPriceColumn);
        TableColumn rateNewestPriceColumn = new TableColumn(3);
        rateNewestPriceColumn.setHeaderValue("最新价");
        rateNewestPriceColumn.setCellRenderer(cellRenderer);
        tableColumnModel.addColumn(rateNewestPriceColumn);
        TableColumn rateRiseOrDownColumn = new TableColumn(4);
        rateRiseOrDownColumn.setHeaderValue("最后成交价涨跌幅(%)");
        rateRiseOrDownColumn.setCellRenderer(cellRenderer);
        tableColumnModel.addColumn(rateRiseOrDownColumn);
        TableColumn rateNumberColumn = new TableColumn(5);
        rateNumberColumn.setHeaderValue("持仓量");
        rateNumberColumn.setCellRenderer(cellRenderer);
        tableColumnModel.addColumn(rateNumberColumn);
        TableColumn feeServiceColumn = new TableColumn(6);
        feeServiceColumn.setHeaderValue("手续费");
        feeServiceColumn.setCellRenderer(cellRenderer);
        tableColumnModel.addColumn(feeServiceColumn);
        TableColumn feeStampColumn = new TableColumn(7);
        feeStampColumn.setHeaderValue("印花税");
        feeStampColumn.setCellRenderer(cellRenderer);
        tableColumnModel.addColumn(feeStampColumn);
        TableColumn diffAmountColumn = new TableColumn(8);
        diffAmountColumn.setHeaderValue("差额");
        diffAmountColumn.setCellRenderer(cellRenderer);
        tableColumnModel.addColumn(diffAmountColumn);
        TableColumn remarkColumn = new TableColumn(9);
        remarkColumn.setHeaderValue("备注");
        remarkColumn.setCellRenderer(cellRenderer);
        tableColumnModel.addColumn(remarkColumn);

        this.rateTable = new JTable(this.rateTableModel,tableColumnModel);
        this.rateTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//只能选择一项
        this.rateTable.setRowSorter(new TableRowSorter<>(this.rateTableModel));
        cellRenderer.setRateTable(this.rateTable);
        cellRenderer.setRateMonitorService(this.rateMonitorService);


        this.rateTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

             RateGUIMain.this.handleStockRowDoubleClick(e);

            }
        });

        JScrollPane scrollPane = new JScrollPane(this.rateTable);
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

    private  void listenRateTradeFlag(){
        this.rateMonitorService.setRateMonitorListener(new RateMonitorListener() {
            //更新后，渲染中间面板
            @Override
            public void fireTableDataChanged() {
                int selectedRowIndex = RateGUIMain.this.rateTable.getSelectedRow();
//                int selectedRowIndex = 0;
                int rateIndex = -1;
                RateInfoBo rateInfo =null;
                if (selectedRowIndex >=0){
                    rateIndex = RateGUIMain.this.rateTable.convertRowIndexToModel(selectedRowIndex);
                    if (rateIndex >= 0){
                        rateInfo = RateGUIMain.this.rateMonitorService.getRatebyIndex(rateIndex);
                    }
                }

                RateGUIMain.this.rateTableModel.fireTableDataChanged();
                if (rateInfo !=null){
                    int viewIndex = RateGUIMain.this.rateTable.convertRowIndexToView(rateIndex);
                    if (viewIndex >=0){
                        RateGUIMain.this.rateTable.setRowSelectionInterval(viewIndex,viewIndex);
                    }
                }
            }

            //置顶更新
            @Override
            public void updateTradeFlag() {
                RateGUIMain.this.rootFrame.toFront();//窗口置顶
            }
        });
    }

    /**
     * 处理excel导入,更新交易信息，计算交易对，交易利润，更新价格
     */
    private  void handleLoadExcelButtonClick(){
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
            int selectedRowIndex = this.rateTable.getSelectedRow();
            if(selectedRowIndex !=-1){
                selectedRowIndex = this.rateTable.convertRowIndexToModel(selectedRowIndex);
                RateInfoBo rateInfo = this.rateMonitorService.getRatebyIndex(selectedRowIndex);
                if (rateInfo == null){
                    System.out.println("没有找到股票信息");
                }else {
                    this.rateTransactionDialog.showTransactionList(rateInfo);
                }
            }
        }
    }

}
