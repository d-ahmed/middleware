package com.middleware.gringott.server.rmi.impl;
import com.middleware.gringott.shared.interfaces.IObservable;
import com.middleware.gringott.shared.interfaces.Item;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;

@Slf4j
@Data
public class Observer extends TimerTask {

    private Item item;
    private boolean isSold;
    private IObservable observable;
    private Timer timer = new Timer();



    public Observer(long time, IObservable observable){
        this.item = item;
        this.isSold = false;
        this.observable = observable;
        final Timer timer = new Timer();
        this.timer.schedule(this, time*60*1000);
    }

    @Override
    public void run() {
        this.observable.update();
        timer.cancel();
    }
}
