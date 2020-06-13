package com.example.demo.repository;

import com.example.demo.bean.Activity;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface ActivityRepository {

    //获取店铺活动
    @Select("select * from tb_activity where s_id=#{id}")
    List<Activity> findAllActivities(int id);

    //通过uid获取店铺活动
    @Select("select * from tb_activity where s_id " +
            "in(select sid from tb_shop where mid=#{id})")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result(column = "discount",property = "discount"),
            @Result(column = "type",property = "type"),
            @Result(column = "season",property = "season"),
            @Result(column = "start_date",property = "start_date"),
            @Result(column = "end_date",property = "end_date"),
            @Result(column = "overlay",property = "overlay"),
            @Result(column = "plan",property = "plan"),
            @Result(column = "s_id",property = "s_id"),
            @Result(column = "s_id",property = "shopName",
            one = @One(select = "com.example.demo.repository.UserRepository.findUsernameById",fetchType = FetchType.EAGER))
    })
    List<Activity> findAllActivitiesByUid(int id);

    //创建活动
    @Insert("insert into tb_activity(discount,type,season,start_date,end_date,s_id,plan,overlay) " +
            "value(#{discount},#{type},#{season},#{start_date},#{end_date},#{s_id},#{plan},#{overlay})")
    int addActivity(Activity activity);

    //删除活动
    @Delete("delete from tb_activity where id=#{id}")
    int deleteActivity(int id);

    //查询正在举行的获得
    @Select("SELECT * FROM fds.tb_activity " +
            "where s_id=#{id} and " +
            "DATE_FORMAT(NOW(),'%Y-%m-%d') between start_date and end_date")
    List<Activity> findNowActivity(int id);

    @Insert({
            "<script>",
            "insert into tb_order_activity(order_id,activity_id) values ",
            "<foreach collection='activityList' item='item' index='index' separator=','>",
            "(#{item.o_id}, #{item.id})",
            "</foreach>",
            "</script>"
    })
    int addOrderActivity(@Param(value = "activityList") List<Activity> activityList);

    //根据订单id获取活动
    @Select("select * from tb_activity where id " +
            "in (select activity_id from tb_order_activity where order_id=#{id})")
    List<Activity> findActivityByOrder(int id);

    //下架活动,防止数据混乱
    @Update("update tb_activity set s_id=0 where id=#{id}")
    int removeActivity(int id);

}
