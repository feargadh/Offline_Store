package com.example.demo.repository;

import com.example.demo.bean.Customer;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CustomerRepository {

    //根据Id查询顾客
    @Select("select * from tb_customer where id=#{id}")
    Customer findCustomerById(int id);


    //获取顾客列表
    @Select("select tb_customer.*,tb_customer_user.membership,tb_customer_user.consumption " +
            "from tb_customer,tb_customer_user where tb_customer.id " +
            "in(select c_id from tb_customer_user where u_id=#{id}) " +
            "and tb_customer.id=tb_customer_user.c_id")

    List<Customer> findAllCustomer(int id);

    //根据姓名和联系方式查询顾客
    @Select("select * from tb_customer " +
            "where customer_name=#{customer_name} " +
            "and phone=#{phone}")
    Customer findCustomerByNameAndPhone(Customer customer);

    //添加顾客
    @Insert("insert into tb_customer(customer_name,phone) " +
            "values(#{customer_name},#{phone})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    int addCustomer(Customer customer);

    //添加顾客与用户连接信息
    @Insert("insert into tb_customer_user(c_id,u_id) " +
            "values(#{c_id},#{u_id})")
    int addCustomerInfo(int c_id,int u_id);

    //修改顾客基础信息
    @Update("update tb_customer set customer_name=#{customer_name},phone=#{phone} where id=#{id}")
    int updateCustomer(Customer customer);

    //根据姓名电话以及user_id查询顾客
    @Select("select * from tb_customer_user " +
            "where u_id=#{id} and c_id in" +
            "(select id from tb_customer where customer_name=#{customer_name} and phone=#{phone})")
    int findCustomerByNameAndPhoneWithUid(Customer customer);

    //添加顾客信息
    @Insert("insert into tb_customer_user(c_id,u_id,membership) " +
            "values(#{id},#{u_id},#{membership})")
    int addCustomerInfoByCustomer(Customer customer);

    //USER修改顾客信息
    @Update("update tb_customer_user set membership=#{membership} where c_id=#{id} and u_id=#{u_id}")
    int updateCustomerInfo2(Customer customer);

}
