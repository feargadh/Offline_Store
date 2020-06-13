package com.example.demo.service;

import com.example.demo.bean.Customer;
import com.example.demo.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CustomerService {

    @Resource
    private CustomerRepository customerRepository;

    public List<Customer> findAllCustomer(int id){
        return customerRepository.findAllCustomer(id);
    }

    public int addCustomer(Customer customer){
        return customerRepository.addCustomer(customer);
    }

    public int addCustomerInfo(int c_id,int u_id){
        return customerRepository.addCustomerInfo(c_id,u_id);
    }

    public int updateCustomer(Customer customer){
        return customerRepository.updateCustomer(customer);
    }

    public Customer findCustomerByNameAndPhone(Customer customer){
        return customerRepository.findCustomerByNameAndPhone(customer);
    }

    public int findCustomerByNameAndPhoneWithUid(Customer customer){
        return customerRepository.findCustomerByNameAndPhoneWithUid(customer);
    }

    public int addCustomerInfo2(Customer customer){
        return customerRepository.addCustomerInfoByCustomer(customer);
    }

    public int updateCustomerInfo2(Customer customer){
        return customerRepository.updateCustomerInfo2(customer);
    }

}
