package com.example.demo.bean;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class Commodity {

    private int id;
    private String c_name;
    private double price;
    private String season;
    private String type;
    private int mid;
    private User master;
    private List<CommodityInfo> infoList;
    private int o_id;
    private int s_id;
    private String size;
    private int quantity;
    private String imgPath;
    private MultipartFile file;

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public int getS_id() {
        return s_id;
    }

    public void setS_id(int s_id) {
        this.s_id = s_id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<CommodityInfo> getInfoList() {
        return infoList;
    }

    public void setInfoList(List<CommodityInfo> infoList) {
        this.infoList = infoList;
    }

    @Override
    public String toString() {
        return "Commodity{" +
                "id=" + id +
                ", c_name='" + c_name + '\'' +
                ", price=" + price +
                ", season='" + season + '\'' +
                ", type='" + type + '\'' +
                ", mid=" + mid +
                ", master=" + master +
                '}';
    }

    public Commodity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public User getMaster() {
        return master;
    }

    public void setMaster(User master) {
        this.master = master;
    }

    public int getO_id() {
        return o_id;
    }

    public void setO_id(int o_id) {
        this.o_id = o_id;
    }
}
