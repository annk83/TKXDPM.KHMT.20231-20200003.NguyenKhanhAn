package controller.impl;

import utils.IEtc;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseController {
    //Prepare for multithread enviroment
    protected final IEtc config;
    protected BaseController(IEtc etc) {
        config = etc;
    }
    private final ThreadLocal<SimpleDateFormat> threadLocal = ThreadLocal.withInitial(()-> new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"));
    public String formatMoney(long amount) {
        return config.getCurrencyConfig().formatMoney(amount);
    }

    public String formatDate(Date date) {
        return threadLocal.get().format(date);
    }
    public String formatWeight(int weight) {
        if(weight < 1000) return weight + " g";
        else return weight/1000 + " kg";
    }
}
