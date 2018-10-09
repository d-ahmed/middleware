package com.middleware.gringott.server.rmi.impl;
import com.middleware.gringott.shared.interfaces.ISoldObservable;
import com.middleware.gringott.shared.interfaces.Item;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;

@Slf4j
@Data
public class Enchere extends TimerTask {

    private Item item;
    private boolean isSold;
    private ISoldObservable observable;
    private Timer timer = new Timer();



    public Enchere(Item item, ISoldObservable observable){
        this.item = item;
        this.isSold = false;
        this.observable = observable;
        final Timer timer = new Timer();
        this.timer.schedule(this, item.getTime()*60*1000);
    }

    @Override
    public void run() {
        log.info("The item {} is sold", this.item.getName());
        this.observable.update();
        setSold(true);
        timer.cancel();
    }
}
