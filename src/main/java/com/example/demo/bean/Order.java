package com.example.demo.bean;

import java.sql.Timestamp;
import java.util.List;

public class Order {

    private int id;//订单编号
    private int sid;//订单所属店铺
    private int cus_id;//买家ID
    private String date;//订单创建时间
    private List<Commodity> commodityList;//订单商品
    private double price;//订单原价
    private User master;//卖家
    private Customer customer;//买家
    private List<Activity> activityList;

    public List<Activity> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<Activity> activityList) {
        this.activityList = activityList;
    }

    public int getCus_id() {
        return cus_id;
    }

    public void setCus_id(int cus_id) {
        this.cus_id = cus_id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public User getMaster() {
        return master;
    }

    public void setMaster(User master) {
        this.master = master;
    }

    public Order() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Commodity> getCommodityList() {
        return commodityList;
    }

    public void setCommodityList(List<Commodity> commodityList) {
        this.commodityList = commodityList;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", sid=" + sid +
                ", cus_id=" + cus_id +
                ", date='" + date + '\'' +
                ", commodityList=" + commodityList +
                ", price=" + price +
                ", master=" + master +
                ", customer=" + customer +
                '}';
    }
}
