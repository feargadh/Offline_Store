package com.example.demo.service;

import com.example.demo.bean.Order;
import com.example.demo.repository.OrderRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OrderService {

    @Resource
    private OrderRepository orderRepository;

    public List<Order> findAllOrders(){
        return orderRepository.findAllOrders();
    }

    public List<Order> findAllOrdersOfShop(int id){
        return orderRepository.findAllOrdersOfShop(id);
    }

    public int addOrder(Order order){
        return orderRepository.addOrder(order);
    }

    public List<Order> findOrderByUid(int id){
        return orderRepository.findOrderByUid(id);
    }

    public int deleteOrder(int id){
        return orderRepository.deleteOrder(id);
    }
}
