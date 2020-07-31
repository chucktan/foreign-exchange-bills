package com.foreign.exchange.service;

/**
 * @author
 * @create 2020-07-31-14:07
 */
public interface StockMonitorListener {
    void fireTableDataChanged();

    void updateTradeFlag();
}
