package com.example.demo.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.bean.*;
import com.example.demo.service.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/shop")
public class ShopController {

    @Resource
    private UserService userService;

    @Resource
    private CustomerService customerService;

    @Resource
    private ActivityService activityService;

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
        Customer customer=new Customer();
        customer.setCustomer_name(customer_name);
        customer.setPhone(phone);
        if (customerService.findCustomerByNameAndPhone(customer)==null){
            if (customerService.addCustomer(customer)>0){
                if (customerService.addCustomerInfo(customer.getId(),id)>0){
                    return "添加成功";
                }
            }
        }
        return "添加失败";
    }

    @RequestMapping(value = "/updateCustomer",method = RequestMethod.POST)
    public String updateCustomer(@RequestBody Customer customer){
        if (customerService.updateCustomer(customer)>0){
            return "修改成功";
        }
        return "修改失败";
    }

    //获取活动列表
    @RequestMapping(value = "/getAllActivities",method = RequestMethod.POST)
    public Map<String,Object> getAllActivities(int id){
        Map<String,Object> map=new HashMap<String, Object>();
        List<Activity> activityList=activityService.findAllActivities(id);
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

    //创建活动
    @RequestMapping(value = "/addActivity",method = RequestMethod.POST)
    public String addActivity(@RequestBody Map<String,Object> map){

        System.out.println(map.toString());
        String type=null;
        String season=null;

        //将checkbox里的选项变为Json字符串
        if (map.get("type")!=null){
            type=checkBoxJsonStr(String.valueOf(map.get("type")));
        }
        if (map.get("season")!=null){
            season=checkBoxJsonStr(String.valueOf(map.get("season")));
        }

        //计算discount
        Double discount=0.000;

        if (Integer.valueOf(String.valueOf(map.get("plan")))==1){
            discount=Double.valueOf(map.get("discount").toString())*0.01;
        }else if (Integer.valueOf(String.valueOf(map.get("plan")))==2){
            double full=Double.valueOf(map.get("full").toString());
            double cut=Double.valueOf(map.get("cut").toString());
            BigDecimal b1=new BigDecimal(Double.toString(full));
            BigDecimal b2=new BigDecimal(Double.toString(cut));
            System.out.println("b1="+b1+" b2="+b2);
            cut=b2.divide(b1,3,BigDecimal.ROUND_HALF_UP).doubleValue();
            System.out.println("cut="+cut);
            BigDecimal b3=new BigDecimal(Double.toString(cut));
            discount=b1.add(b3).doubleValue();
            System.out.println(discount);
        }else {
            double full=Double.valueOf(map.get("full").toString());
            double cut=Double.valueOf(map.get("free").toString())*0.01;
            discount=full+cut;

        }

        Activity activity=new Activity();
        activity.setSeason(season);
        activity.setType(type);
        activity.setS_id(Integer.valueOf(String.valueOf(map.get("s_id"))));
        activity.setStart_date(String.valueOf(map.get("start_date")));
        activity.setEnd_date(String.valueOf(map.get("end_date")));
        activity.setDiscount(discount);
        activity.setPlan(String.valueOf(map.get("plan")));
        activity.setOverlay(Integer.valueOf(String.valueOf(map.get("overlay"))));

        if (activityService.addActivity(activity)>0){
            return "创建成功";
        }
        return "数据传输错误";
    }

    //将checkbox里的String数组提取出来，转成Json字符串
    public String checkBoxJsonStr(String checkbox){
        Map<String,Object> map=new HashMap<String, Object>();
        JSONArray array=JSONArray.parseArray(checkbox);
        for (int i=0;i<array.size();i++){
            map.put(String.valueOf(i),array.get(i));
        }

        return JSON.toJSONString(map);
    }

    //删除活动
    @RequestMapping(value = "/deleteActivity",method = RequestMethod.POST)
    public String deleteActivity(int id){
        if (activityService.removeActivity(id)>0){
            return "删除成功";
        }
        return "数据传输错误";
    }

    //获取商品列表
    @RequestMapping(value = "/findAllCommodity",method = RequestMethod.POST)
    public Map<String,Object> findAllCommodity(int id){
        Map<String,Object> map=new HashMap<String, Object>();
        List<CommodityInfo> commodityList=commodityService.findCommodityByShop(id);
        map.put("commodityList",commodityList);
        return map;
    }

    //根据Mid获取所有商品款式
    @RequestMapping(value = "/findCommodityByMid",method = RequestMethod.POST)
    public Map<String,Object> findCommodityByMid(int id){
        Map<String,Object> map=new HashMap<String, Object>();
        List<Commodity> commodityList=commodityService.findCommodityByMid(id);
        map.put("commodityList",commodityList);
        return map;
    }

    @RequestMapping(value = "/addCommodityInfo",method = RequestMethod.POST)
    public String addCommodityInfo(@RequestBody CommodityInfo commodityInfo){
        if (commodityService.checkCommodityByShop(commodityInfo)>0){
            if (commodityService.addCommodityInfo2(commodityInfo)>0){
                return "入库成功";
            }
        }else if (commodityService.addCommodityInfo(commodityInfo)>0){
            return "入库成功";
        }
        return "数据传输错误";
    }

    @RequestMapping(value = "/findAllOrders",method = RequestMethod.POST)
    public Map<String,Object> findAllOrdersOfShop(int id){
        Map<String,Object> map=new HashMap<String, Object>();
        List<Order> orderList=orderService.findAllOrdersOfShop(id);
        map.put("orderList",orderList);
        return map;
    }

    //获取活动列表
    @RequestMapping(value = "/getNowActivity",method = RequestMethod.POST)
    public Map<String,Object> geNowActivities(int id){
        Map<String,Object> map=new HashMap<String, Object>();
        List<Activity> activityList=activityService.findNowActivity(id);
        for (Activity activity:activityList){
            if (!activity.getType().equals("1")){
                activity.setTypeJson(activity.getType());
            }
            if (!activity.getSeason().equals("1")){
                activity.setSeasonJson(activity.getSeason());
            };
        }
        map.put("activityList",activityList);
        return map;
    }

    //创建订单
    @RequestMapping(value = "/addOrder" ,method = RequestMethod.POST)
    public String addOrder(@RequestBody Map<String,Object> map){

        Order order=new Order();
        order.setPrice(Double.valueOf(String.valueOf(map.get("price"))));
        order.setSid(Integer.valueOf(String.valueOf(map.get("sid"))));
        order.setCus_id(Integer.valueOf(String.valueOf(map.get("cus_id"))));
        System.out.println("顾客Id:"+order.getCus_id());

        if (orderService.addOrder(order)>0){
            String commoditiesStr=String.valueOf(map.get("shoppingList"));
            JSONArray array1=JSONArray.parseArray(commoditiesStr);
            System.out.println("shoppingList数组字符串 "+array1.toString());
            List<Commodity> commodityList=new ArrayList<Commodity>();
            for (int i=0;i<array1.size();i++){
                JSONObject jsonObject=JSONObject.parseObject(array1.getString(i));
                Commodity commodity=new Commodity();
                commodity.setQuantity(Integer.valueOf(String.valueOf(jsonObject.get("quantity"))));
                commodity.setId(Integer.valueOf(String.valueOf(jsonObject.get("c_id"))));
                commodity.setSize(String.valueOf(jsonObject.get("size")));
                commodity.setO_id(order.getId());
                commodity.setS_id(Integer.valueOf(String.valueOf(map.get("sid"))));
                commodityList.add(commodity);
            }
            if (commodityService.addOrderCommodity(commodityList)>0){
                System.out.println("订单商品加入成功");
                String activityId=String.valueOf(map.get("activity.id"));
                JSONArray array2=JSONArray.parseArray(activityId);
                List<Activity> activityList=new ArrayList<Activity>();
                for (int i=0;i<array2.size();i++){
                    Activity activity=new Activity();
                    activity.setId(Integer.valueOf(String.valueOf(array2.get(i))));
                    activity.setO_id(order.getId());
                    activityList.add(activity);
                }
                if (activityService.addOrderActivity(activityList)>0){
                    System.out.println("订单活动加入成功");
                    //修改库存
                    if (commodityService.updateCommodityList(commodityList)>0){
                        System.out.println("修改库存成功");
                        return "OK";
                    }

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
            return "修改成功";
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
}
