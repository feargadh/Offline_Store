package com.example.demo.repository;

import com.example.demo.bean.Commodity;
import com.example.demo.bean.CommodityInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface CommodityRepository {

    //获取所有商品
    @Select("select * from tb_commodity")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result(column = "c_name",property = "c_name"),
            @Result(column = "price",property = "price"),
            @Result(column = "season",property = "season"),
            @Result(column = "type",property = "type"),
            @Result(column = "mid",property = "mid"),
            @Result(column = "mid",property = "master",
                    one = @One(select ="com.example.demo.repository.UserRepository.findUserById" ,
                            fetchType = FetchType.EAGER ))
    })
    List<Commodity> findAllCommodity();

    //根据订单编号获取商品信息
    @Select("SELECT tb_commodity.*," +
            "tb_order_commodity.size, tb_order_commodity.quantity " +
            "FROM tb_commodity,tb_order_commodity " +
            "where " +
            "tb_commodity.id in(select c_id from tb_order_commodity where o_id=#{id}) " +
            "and tb_order_commodity.c_id=tb_commodity.id " +
            "and tb_order_commodity.o_id=#{id}")

    List<Commodity> findCommoditiesByOrder(int id);

    //根据店铺id查询商品
    @Select("select * from tb_commodity_info where s_id =#{id}")
    @Results({
            @Result(id=true,column = "id",property = "id"),
            @Result(column = "c_id",property = "c_id"),
            @Result(column = "s_id",property = "s_id"),
            @Result(column = "S",property = "S"),
            @Result(column = "M",property = "M"),
            @Result(column = "L",property = "L"),
            @Result(column = "imgPath",property = "imgPath"),
            @Result(column = "c_id",property = "commodity",
            one = @One(select = "findCommodityById",fetchType = FetchType.EAGER))
    })
    List<CommodityInfo> findAllCommodityByShop(int id);

    //根据商品Id查询商品
    @Select("select * from tb_commodity where id=#{id}")
    Commodity findCommodityById(int id);

    //根据Mid查询商品
    @Select("select * from tb_commodity where mid=#{id}")
    List<Commodity> findCommodityByMid(int id);

    //商品信息入库
    @Insert("insert into tb_commodity_info(s_id,c_id,S,M,L) " +
            "values(#{s_id},#{c_id},#{s},#{m},#{l})")
    int addCommodityInfo(CommodityInfo commodityInfo);

    //添加订单商品
    @Insert({
            "<script>",
            "insert into tb_order_commodity(o_id,c_id,size,quantity) values ",
            "<foreach collection='commodityList' item='item' index='index' separator=','>",
            "(#{item.o_id}, #{item.id}, #{item.size},#{item.quantity})",
            "</foreach>",
            "</script>"
    })
    int addOrderCommodity(@Param(value = "commodityList") List<Commodity> commodityList);

    //批量修改库存
    @Update({"<script>" +
            "<foreach collection=\"commodityList\" item=\"item\" separator=\";\">" +
            " UPDATE" +
            " tb_commodity_info" +
            " SET ${item.size} = ${item.size}-#{item.quantity} " +
            "  WHERE " +
            "  c_id =#{item.id}" +
            "  AND s_id=#{item.s_id}" +
            "</foreach>" +
            "</script>"})
    int updateCommodityList(@Param(value = "commodityList") List<Commodity> commodityList);


    //查询是否已有库存
    @Select("select count(*) from tb_commodity_info where c_id=#{c_id} and s_id=#{s_id}")
     int checkCommodityByShop(CommodityInfo commodityInfo);

    //修改库存
    @Update("update tb_commodity_info set s=s+#{s},m=m+#{m},l=l+#{l} where c_id=#{c_id} and s_id=#{s_id} ")
    int addCommodityInfo2(CommodityInfo commodityInfo);

    //查询所有商品
    @Select("select * from tb_commodity where mid=#{id} ")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result(column = "c_name",property = "c_name"),
            @Result(column = "price",property = "price"),
            @Result(column = "season",property = "season"),
            @Result(column = "type",property = "type"),
            @Result(column = "mid",property = "mid"),
            @Result(column = "imgPath",property = "imgPath"),
            @Result(column = "id",property = "infoList",
            many = @Many(select ="findCommodityInfo2",fetchType = FetchType.EAGER))
    })
    List<Commodity> findCommodityByUid(int id);

    //查询商品信息，并获取店铺信息
    @Select("select * from tb_commodity_info where c_id=#{id}")
    @Results({
            @Result(id=true,column = "id",property = "id"),
            @Result(column = "c_id",property = "c_id"),
            @Result(column = "s_id",property = "s_id"),
            @Result(column = "S",property = "S"),
            @Result(column = "M",property = "M"),
            @Result(column = "L",property = "L"),
            @Result(column = "s_id",property = "shopName",
            one = @One(select = "com.example.demo.repository.UserRepository.findUsernameById",fetchType = FetchType.EAGER))
    })
    List<CommodityInfo> findCommodityInfo2(int id);

    //修改商品信息
    @Update("update tb_commodity " +
            "set c_name=#{c_name},price=#{price},type=#{type},season=#{season},imgPath=#{imgPath} " +
            "where id=#{id}")
    int updateCommodity(Commodity commodity);

    //添加商品
    @Insert("insert into tb_commodity(c_name,price,type,season,mid,imgPath) " +
            "values(#{c_name},#{price},#{type},#{season},#{mid},#{imgPath}) ")
    int addCommodity(Commodity commodity);

    //下架商品
    @Update("update tb_commodity set mid=0 where id=#{id}")
    int removeCommodity(int id);
}
