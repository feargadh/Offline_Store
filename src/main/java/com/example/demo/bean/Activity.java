package com.example.demo.bean;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class Activity {
    private int id;
    private double discount;
    private JSONObject typeJson;
    private JSONObject seasonJson;
    private String start_date;
    private String end_date;
    private int s_id;
    private String type="";
    private String season="";
    private int overlay;
    private String plan;
    private int o_id;
    private String shopName;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getO_id() {
        return o_id;
    }

    public void setO_id(int o_id) {
        this.o_id = o_id;
    }

    public int getOverlay() {
        return overlay;
    }

    public void setOverlay(int overlay) {
        this.overlay = overlay;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", discount=" + discount +
                ", typeJson=" + typeJson +
                ", seasonJson=" + seasonJson +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", s_id=" + s_id +
                ", type='" + type + '\'' +
                ", season='" + season + '\'' +
                '}';
    }

    public void setType(String type) {
        this.type = type;
        this.setTypeJson(type);
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
        this.setSeasonJson(season);
    }

    public Activity() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public JSONObject getTypeJson() {
        return typeJson;
    }

    public void setTypeJson(String type) {
        this.typeJson = JSONObject.parseObject(type);
    }

    public JSONObject getSeasonJson() {
        return seasonJson;
    }

    public void setSeasonJson(String season) {
        this.seasonJson = JSONObject.parseObject(season);
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public int getS_id() {
        return s_id;
    }

    public void setS_id(int s_id) {
        this.s_id = s_id;
    }
}
