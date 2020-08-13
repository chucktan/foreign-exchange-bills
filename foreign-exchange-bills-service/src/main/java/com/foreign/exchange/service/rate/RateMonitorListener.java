package com.foreign.exchange.service.rate;

/**
 * @author
 * @create 2020-07-31-14:07
 */
public interface RateMonitorListener {
    void fireTableDataChanged();

    void updateTradeFlag();
}
