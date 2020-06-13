package com.example.demo.bean;

public class ShopInformation {

    public ShopInformation() {
    }

    private int id;
    private int mid;//门店主Id
    private int sid;//门店id;
    private String address;
    private User master;//门店店主信息

    public User getMaster() {
        return master;
    }

    public void setMaster(User master) {
        this.master = master;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "ShopInformation{" +
                "id=" + id +
                ", mid=" + mid +
                ", sid=" + sid +
                ", address='" + address + '\'' +
                ", master=" + master +
                '}';
    }
}
