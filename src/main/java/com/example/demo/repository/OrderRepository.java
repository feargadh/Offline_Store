package com.example.demo.repository;

import com.example.demo.bean.Order;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface OrderRepository {

    @Select("select * from tb_order")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result(column = "sid",property = "sid"),
            @Result(column = "date",property = "date"),
            @Result(column = "cus_id",property = "cus_id"),
            @Result(column = "total",property = "total"),
            @Result(column = "o_price",property = "o_price"),
            @Result(column = "id",property = "commodityList",
            many = @Many(select = "com.example.demo.repository.CommodityRepository.findCommoditiesByOrder"
            ,fetchType = FetchType.EAGER)),
            @Result(column = "sid",property = "master",
            one = @One(select ="com.example.demo.repository.UserRepository.findUserById" ,
                    fetchType = FetchType.EAGER )),
            @Result(column = "id",property = "activityList",
                    many = @Many(select = "com.example.demo.repository.ActivityRepository.findActivityByOrder"
                            ,fetchType = FetchType.EAGER)),
            @Result(column = "cus_id",property = "customer",
                    one = @One(select ="com.example.demo.repository.CustomerRepository.findCustomerById" ,
                            fetchType = FetchType.EAGER ))
    })
    List<Order> findAllOrders();

    //根据店铺id搜索订单
    @Select("select * from tb_order where sid=#{id}")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result(column = "sid",property = "sid"),
            @Result(column = "date",property = "date"),
            @Result(column = "cus_id",property = "cus_id"),
            @Result(column = "total",property = "total"),
            @Result(column = "o_price",property = "o_price"),
            @Result(column = "id",property = "commodityList",
                    many = @Many(select = "com.example.demo.repository.CommodityRepository.findCommoditiesByOrder"
                            ,fetchType = FetchType.EAGER)),
            @Result(column = "cus_id",property = "customer",
                    one = @One(select ="com.example.demo.repository.CustomerRepository.findCustomerById" ,
                            fetchType = FetchType.EAGER )),
            @Result(column = "id",property = "activityList",
                    many = @Many(select = "com.example.demo.repository.ActivityRepository.findActivityByOrder"
                            ,fetchType = FetchType.EAGER)),
    })
    List<Order> findAllOrdersOfShop(int id);

    //创建订单 ，并返回自增主键
    @Insert("insert into tb_order(sid,cus_id,price) " +
            "values(#{sid},#{cus_id},#{price})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    int addOrder(Order order);

    //根据USER_id查询所有其店铺订单
    @Select("select * from tb_order where sid in" +
            "(select sid from tb_shop where mid=#{id})")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result(column = "sid",property = "sid"),
            @Result(column = "date",property = "date"),
            @Result(column = "cus_id",property = "cus_id"),
            @Result(column = "total",property = "total"),
            @Result(column = "o_price",property = "o_price"),
            @Result(column = "id",property = "commodityList",
                    many = @Many(select = "com.example.demo.repository.CommodityRepository.findCommoditiesByOrder"
                            ,fetchType = FetchType.EAGER)),
            @Result(column = "cus_id",property = "customer",
                    one = @One(select ="com.example.demo.repository.CustomerRepository.findCustomerById" ,
                            fetchType = FetchType.EAGER )),
            @Result(column = "id",property = "activityList",
                    many = @Many(select = "com.example.demo.repository.ActivityRepository.findActivityByOrder"
                            ,fetchType = FetchType.EAGER)),
            @Result(column = "sid",property = "master",
                    one = @One(select ="com.example.demo.repository.UserRepository.findUserById" ,
                            fetchType = FetchType.EAGER )),

    })
    List<Order> findOrderByUid(int id);

    //删除订单
    @Delete("delete from tb_order where id=#{id}")
    int deleteOrder(int id);
}
