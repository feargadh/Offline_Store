package com.example.demo.service;

import com.example.demo.bean.Commodity;
import com.example.demo.bean.CommodityInfo;
import com.example.demo.repository.CommodityRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CommodityService {

    @Resource
    private CommodityRepository commodityRepository;

    public List<Commodity> findAllCommodity(){
        return commodityRepository.findAllCommodity();
    }

    public List<CommodityInfo> findCommodityByShop(int id){
        return commodityRepository.findAllCommodityByShop(id);
    }

    public List<Commodity> findCommodityByMid(int id){
        return commodityRepository.findCommodityByMid(id);
    }

    public int addCommodityInfo(CommodityInfo commodityInfo){
        return commodityRepository.addCommodityInfo(commodityInfo);
    }

    public int addOrderCommodity(List<Commodity> commodityList){
        return commodityRepository.addOrderCommodity(commodityList);
    }

    public int updateCommodityList(List<Commodity> commodityList){
        return commodityRepository.updateCommodityList(commodityList);
    }

    public int checkCommodityByShop(CommodityInfo commodityInfo){
        return commodityRepository.checkCommodityByShop(commodityInfo);
    }

    public int addCommodityInfo2(CommodityInfo commodityInfo){
        return commodityRepository.addCommodityInfo2(commodityInfo);
    }

    public List<Commodity> findCommodityByUid(int id){
        return  commodityRepository.findCommodityByUid(id);
    }

    public int updateCommodity(Commodity commodity){
        return commodityRepository.updateCommodity(commodity);
    }

    public int addCommodity(Commodity commodity){
        return commodityRepository.addCommodity(commodity);
    }

    public int removeCommodity(int id){
        return commodityRepository.removeCommodity(id);
    }
}
