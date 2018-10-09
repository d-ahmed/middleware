package com.middleware.gringott.server.data.entities;

import com.middleware.gringott.shared.interfaces.Item;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;


@NoArgsConstructor
@Slf4j
public class SellableItem implements Item {

    private static final long serialVersionUID = -4517882019233732317L;

    //@Id
    //@GeneratedValue(generator="system-uuid")
    //@GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;
    private String name;
    private String description;
    private String seller;
    private String leader;
    private double price;
    private long time;
    private boolean sold;


    public SellableItem(String name, String description, double price, String seller, long time2leave) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.seller = seller;
        this.leader = null;
        this.time = time2leave;
        this.sold = false;



    }



    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getLeader() {
        return this.leader;
    }

    @Override
    public double getPrice() {
        return this.price;
    }

    @Override
    public boolean isSold() {
        return this.sold;
    }

    @Override
    public void setLeader(String leader) {
        this.leader = leader;
    }

    @Override
    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public void setSold(boolean status) {
        this.sold = status;
    }

    @Override
    public long getTime() {
        return this.time;
    }

    @Override
    public Object getSeller() {
        return this.seller;
    }
}