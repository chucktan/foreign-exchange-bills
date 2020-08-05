package com.foreign.exchange.gui;

import com.foreign.exchange.service.StockMonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *GUI界面
 * @author
 * @create 2020-07-30-15:46
 */
public class GUIMain {
    private Logger logger = LoggerFactory.getLogger(GUIMain.class);
    private StockMonitorService stockMonitorService;


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
        
    }

}
