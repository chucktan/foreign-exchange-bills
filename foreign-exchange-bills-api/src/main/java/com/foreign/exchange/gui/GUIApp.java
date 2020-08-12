package com.foreign.exchange.gui;

import com.foreign.exchange.pojo.Vo.StockPriceVo;
import com.foreign.exchange.service.impl.stock.StockMarketServiceImpl;
import com.foreign.exchange.service.stock.StockMarketService;
import com.foreign.exchange.service.stock.StockMonitorService;
import com.foreign.exchange.service.stock.StockParseExcelService;
import com.foreign.exchange.service.stock.StockUpdateService;

/**
 * @author
 * @create 2020-07-30-15:45
 */
public class GUIApp {

    private  GUIMain guiMain;
    private StockMonitorService stockMonitorService;
    private StockParseExcelService stockParseExcelService;
    private StockUpdateService stockUpdateService;
    private StockMarketService stockMarketService;


    public static void main(String[] args) {
        GUIApp guiApp = new GUIApp();
        guiApp.start();

    }

    public  void start(){
        this.guiMain = new GUIMain();
        this.stockMonitorService = new StockMonitorService();
        this.stockMarketService = new StockMarketServiceImpl();
        this.stockUpdateService = new StockUpdateService();
        this.stockParseExcelService = new StockParseExcelService();

        this.guiMain.setStockMonitorService(this.stockMonitorService);
        this.guiMain.setStockParseExcelService(this.stockParseExcelService);
        this.guiMain.setStockUpdateService(this.stockUpdateService);
        this.guiMain.render();

        this.stockUpdateService.setStockMonitorService(this.stockMonitorService);
        this.stockUpdateService.setStockMarketService(this.stockMarketService);
        this.stockUpdateService.startUpdateEveryTime();

    }


}
