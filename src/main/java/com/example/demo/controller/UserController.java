package com.example.demo.controller;

import com.example.demo.bean.*;
import com.example.demo.service.*;
import org.apache.ibatis.annotations.Update;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private ActivityService activityService;

    @Resource
    private CustomerService customerService;

    @Resource
    private CommodityService commodityService;

    @Resource
    private OrderService orderService;

    @RequestMapping(value = "/getAllCustomer",method = RequestMethod.POST)
    public Map<String, Object> getAllCustomer(int id){
        Map<String,Object> map =new HashMap<String, Object>();
        List<Customer> customerList=customerService.findAllCustomer(id);
        map.put("customerList",customerList);
        return map;
    }

    @RequestMapping(value = "/addCustomer",method = RequestMethod.POST)
    public String addCustomer(@RequestBody Map<String,Object> map){
        Integer id=Integer.valueOf(String.valueOf(map.get("id")));
        String customer_name=String.valueOf(map.get("customer_name"));
        String phone=String.valueOf(map.get("phone"));
        Double membership=Double.valueOf(String.valueOf(map.get("membership")));
        Customer customer=new Customer();
        customer.setId(id);
        customer.setCustomer_name(customer_name);
        customer.setPhone(phone);
        customer.setMembership(membership);
        Customer customer1=customerService.findCustomerByNameAndPhone(customer);
        if (customer1 ==null){
            if (customerService.addCustomer(customer)>0){
                if (customerService.addCustomerInfo(customer.getId(),id)>0){
                    return String.valueOf(customer.getId());
                }
            }
        }else {
            if (customerService.findCustomerByNameAndPhoneWithUid(customer)>0){
                return "该顾客已存在";
            }else {
                customer1.setU_id(id);
                customer1.setMembership(membership);
                if (customerService.addCustomerInfo2(customer1)>0){
                    return "添加成功";
                }
            }
        }
        return "添加失败";
    }

    @RequestMapping(value = "/updateCustomer",method = RequestMethod.POST)
    public String updateCustomer(@RequestBody Customer customer){
        if (customerService.updateCustomerInfo2(customer)>0){
            return "修改成功";
        }
        return "修改失败";
    }

    //获取商品列表
    @RequestMapping(value = "/findAllCommodity",method = RequestMethod.POST)
    public Map<String,Object> findAllCommodity(int id){
        Map<String,Object> map=new HashMap<String, Object>();
        List<Commodity> commodityList=commodityService.findCommodityByUid(id);
        map.put("commodityList",commodityList);
        return map;
    }

    //修改商品信息
    @RequestMapping(value = "/updateCommodity",method = RequestMethod.POST)
    public String updateCommodity(Commodity commodity) throws Exception {
        if (commodity.getFile()!=null){
            System.out.println("找到图片");
            uploadImg(commodity);
        }
        if (commodityService.updateCommodity(commodity)>0) {
            return "修改成功";
        }
        return "数据传输错误";
    }

    //添加商品
    @RequestMapping(value = "/addCommodity",method = RequestMethod.POST)
    public String addCommodity(Commodity commodity) throws Exception {
        if (commodity.getFile()!=null){
            System.out.println("找到图片");
            uploadImg(commodity);
        }else {
            System.out.println("没找到图片");
            commodity.setImgPath("/commodity/noImg.jpg");
        }
        if (commodityService.addCommodity(commodity)>0){
            return "添加成功";
        }
        return "数据传输错误";
    }

    //删除商品
    @RequestMapping(value = "/deleteCommodity",method = RequestMethod.POST)
    public String removeCommodity(int id){
        if (commodityService.removeCommodity(id)>0){
            return "删除成功";
        }
        return "数据传输错误";
    }


    //查找订单
    @RequestMapping(value = "/findAllOrders",method = RequestMethod.POST)
    public Map<String,Object> findAllOrdersOfShop(int id){
        Map<String,Object> map=new HashMap<String, Object>();
        List<Order> orderList=orderService.findOrderByUid(id);
        map.put("orderList",orderList);
        return map;
    }

    @RequestMapping(value = "/deleteOrder",method = RequestMethod.POST)
    public String deleteOrder(int id){
        if (orderService.deleteOrder(id)>0){
            return "删除成功";
        }
        return "删除失败";
    }

    //获取所有SHOP用户
    @RequestMapping(value = "findAllShop",method = RequestMethod.POST)
    public Map<String,Object> findAllShop(int id){
        Map<String,Object> map=new HashMap<String, Object>();
        List<User> shopList=userService.findShopByUid(id);
        map.put("shopList",shopList);
        return map;
    }

    //获取活动列表
    @RequestMapping(value = "/getAllActivities",method = RequestMethod.POST)
    public Map<String,Object> getAllActivities(int id){
        Map<String,Object> map=new HashMap<String, Object>();
        List<Activity> activityList=activityService.findActivityByUid(id);
        for (Activity activity:activityList){
            if (!activity.getType().equals("1")){
                activity.setTypeJson(activity.getType());
            }
            if (!activity.getSeason().equals("1")){
                activity.setSeasonJson(activity.getSeason());
            }
        }
        map.put("activityList",activityList);
        return map;
    }

    @RequestMapping(value = "/updateShop",method = RequestMethod.POST)
    public String updateShop(@RequestBody User user){
        if (userService.updateShop(user)>0){
            return "修改成功";
        }
        return "数据传输错误";
    }

    //添加门店
    @RequestMapping(value = "/addShop",method = RequestMethod.POST)
    public String addShop(@RequestBody User user){
        ShopInformation shopInformation=new ShopInformation();
        shopInformation.setMid(user.getId());
        shopInformation.setAddress(user.getAddress());
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        if (userService.addShop2(user)>0){
            shopInformation.setSid(user.getId());
            System.out.println(shopInformation.toString());
            if (userService.addShopInfo(shopInformation)>0){
                if (userService.addRole(user.getId(),3)>0){
                    return "创建成功";
                }

            }
        }
        return "数据传输错误";
    }

    //上传头像
    @RequestMapping(value = "/uploadIcon",method = RequestMethod.POST)
    public String uploadFile(User user, HttpServletResponse response) throws Exception {
        String filePath="F:/projectImg/icon/";
        MultipartFile file=user.getFile();
        String extName=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName= UUID.randomUUID().toString()+extName;
        FileCopyUtils.copy(file.getInputStream(),new FileOutputStream(new File(filePath+fileName)));
        String filename = "/icon/" + fileName;
        user.setImgPath(filename);
        if (userService.uploadIcon(user)>0){
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=utf-8");
            PrintWriter out=response.getWriter();
            out.print("<script>alert('提交成功');history.go(-1);</script>");
        }
        return null;
    }

    //修改用户名
    @RequestMapping(value = "/updateUsername",method = RequestMethod.POST)
    public String updateUsername(@RequestBody User user){
        if (userService.updateUsername(user)>0){
            return "修改成功,请使用新用户名重新登录";
        }
        return "数据传输错误";
    }

    //修改其他信息
    @RequestMapping(value = "/updateOther",method = RequestMethod.POST)
    public String updateOther(@RequestBody User user){
        if (userService.updateOther(user)>0){
            return "修改成功";
        }
        return "数据传输错误";
    }

    //修改密码
    @RequestMapping(value = "/updatePassword",method = RequestMethod.POST)
    public String updatePassword(@RequestBody Map<String,String> map){
        User user=new User();
        user.setId(Integer.valueOf(map.get("id")));
        user.setPassword(map.get("password"));
        String password=userService.findPasswordById(user.getId());
        if (new BCryptPasswordEncoder().matches(map.get("old_password"),password)){
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            if (userService.updateUserPassword(user)>0){
                return "修改成功";
            }
        }else {
            return "请输入正确原密码";
        }
        return "数据传输错误";
    }


    //上传商品图片方法
    public void uploadImg(Commodity commodity) throws Exception {
        String filePath="F:/projectImg/commodity/";
        MultipartFile file=commodity.getFile();
        String extName=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName= UUID.randomUUID().toString()+extName;
        FileCopyUtils.copy(file.getInputStream(),new FileOutputStream(new File(filePath+fileName)));
        String filename = "/commodity/" + fileName;
        commodity.setImgPath(filename);
    }

}
