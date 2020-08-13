package com.foreign.exchange.gui;

import com.foreign.exchange.gui.rate.RateGUIMain;
import com.foreign.exchange.gui.stock.StockGUIMain;
import com.foreign.exchange.service.impl.rate.FinanceRateMarketServiceImpl;
import com.foreign.exchange.service.rate.FinanceRateMarketService;
import com.foreign.exchange.service.rate.RateMonitorService;
import com.foreign.exchange.service.rate.RateParseExcelService;
import com.foreign.exchange.service.rate.RateUpdateService;
import com.foreign.exchange.service.stock.StockMarketService;
import com.foreign.exchange.service.stock.StockMonitorService;
import com.foreign.exchange.service.stock.StockParseExcelService;
import com.foreign.exchange.service.stock.StockUpdateService;

/**
 * @author
 * @create 2020-07-30-15:45
 */
public class GUIApp {

    private StockGUIMain stockGUIMain;
    private StockMonitorService stockMonitorService;
    private StockParseExcelService stockParseExcelService;
    private StockUpdateService stockUpdateService;
    private StockMarketService stockMarketService;

    private RateGUIMain rateGUIMain;
    private RateMonitorService rateMonitorService;
    private RateParseExcelService rateParseExcelService;
    private RateUpdateService rateUpdateService;
    private FinanceRateMarketService rateMarketService;

    public static void main(String[] args) {
        GUIApp guiApp = new GUIApp();
        guiApp.start();

    }

    public  void start(){
//        this.stockGUIMain = new StockGUIMain();
//        this.stockMonitorService = new StockMonitorService();
//        this.stockMarketService = new StockMarketServiceImpl();
//        this.stockUpdateService = new StockUpdateService();
//        this.stockParseExcelService = new StockParseExcelService();
//
//        this.stockGUIMain.setStockMonitorService(this.stockMonitorService);
//        this.stockGUIMain.setStockParseExcelService(this.stockParseExcelService);
//        this.stockGUIMain.setStockUpdateService(this.stockUpdateService);
//        this.stockGUIMain.render();
//
//        this.stockUpdateService.setStockMonitorService(this.stockMonitorService);
//        this.stockUpdateService.setStockMarketService(this.stockMarketService);
//        this.stockUpdateService.startUpdateEveryTime();

        this.rateGUIMain = new RateGUIMain();
        this.rateMonitorService = new RateMonitorService();
        this.rateMarketService = new FinanceRateMarketServiceImpl();
        this.rateUpdateService = new RateUpdateService();
        this.rateParseExcelService = new RateParseExcelService();

        this.rateGUIMain.setRateMonitorService(this.rateMonitorService);
        this.rateGUIMain.setRateParseExcelService(this.rateParseExcelService);
        this.rateGUIMain.setRateUpdateService(this.rateUpdateService);
        this.rateGUIMain.render();

        this.rateUpdateService.setRateMonitorService(this.rateMonitorService);
        this.rateUpdateService.setRateMarketService(this.rateMarketService);
        this.rateUpdateService.startUpdateEveryTime();


    }


}
