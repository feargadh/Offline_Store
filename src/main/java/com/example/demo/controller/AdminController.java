package com.example.demo.controller;

import com.example.demo.bean.Commodity;
import com.example.demo.bean.Order;
import com.example.demo.bean.ShopInformation;
import com.example.demo.bean.User;
import com.example.demo.service.CommodityService;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;
import org.apache.ibatis.annotations.Insert;
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
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private UserService userService;

    @Resource
    private OrderService orderService;

    @Resource
    private CommodityService commodityService;

    //查询所有USER用户
    @RequestMapping(value = "/findAllUsers",method = RequestMethod.POST)
    public Map<String,Object> findAllUsers(){
        Map<String,Object> map=new HashMap<String, Object>();
        List<User> userList=userService.findAllUsers();
        map.put("userList",userList);
        return map;
    }

    //添加USER用户
    @RequestMapping(value = "/addUser",method = RequestMethod.POST)
    public String addUser(@RequestBody User user){
        System.out.println(user.toString());
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        if (userService.addUser(user)>0){
            if (userService.addRole(user.getId(),2)>0){
                return "创建成功";
            }
        }
        return "创建失败";
    }

    //重置用户密码为123
    @RequestMapping(value = "/resetPassword",method = RequestMethod.POST)
    public String resetPassword(int id){
        User user=new User();
        user.setId(id);
        user.setPassword(new BCryptPasswordEncoder().encode("123"));
        if (userService.updatePassword(user)>0){
            return "已重置密码为123";
        }
        return "重置失败";
    }

    //删除用户
    @RequestMapping(value = "/deleteUser",method = RequestMethod.POST)
    public String deleteUser(Integer id){

        if (userService.deleteUser(id)>0){
            return "删除成功";
        }
        return "删除失败";
    }

    //修改用户信息
    @RequestMapping(value = "/updateUser",method = RequestMethod.POST)
    public String updateUser(@RequestBody User user){
        System.out.println(user.toString());
        if (userService.updateUser(user)>0){
            return "修改成功";
        }
        return "修改失败";
    }

    //获取所有SHOP用户
    @RequestMapping(value = "findAllShop",method = RequestMethod.POST)
    public Map<String,Object> findAllShop(){
        Map<String,Object> map=new HashMap<String, Object>();
        List<User> shopList=userService.findAllShop();
        map.put("shopList",shopList);
        return map;
    }

    @RequestMapping(value = "/addShop",method = RequestMethod.POST)
    public String addShop(@RequestBody Map<String,Object> map){

        //获取user信息
        User user =new User();
        user.setUsername(String.valueOf(map.get("username")));
        user.setPassword(new BCryptPasswordEncoder().encode(String.valueOf(map.get("password"))));
        user.setSex(String.valueOf(map.get("sex")).charAt(0));
        user.setPhone(String.valueOf(map.get("phone")));

        if (userService.addUser(user)>0){
            if (userService.addRole(user.getId(),3)>0){
                ShopInformation information=new ShopInformation();
                information.setMid(Integer.valueOf(String.valueOf(map.get("mid"))));
                information.setAddress(String.valueOf(map.get("address")));
                information.setSid(user.getId());
                if (userService.addShop(information)>0){
                    return "添加成功";
                }
            }
        }
        return "添加失败";
    }

    @RequestMapping(value = "/findAllCommodity",method = RequestMethod.POST)
    public Map<String ,Object> findAllCommodity(){
        Map<String,Object> map=new HashMap<String, Object>();
        List<Commodity> commodityList=commodityService.findAllCommodity();
        for (Commodity commodity:commodityList){
            if (commodity.getMid()==0){
                User user=new User();
                user.setUsername("无");
                commodity.setMaster(user);
            }
        }
        map.put("commodityList",commodityList);
        return map;
    }

    @RequestMapping(value = "/findAllOrders")
    public Map<String,Object> findAllOrders(){
        Map<String,Object> map=new HashMap<String, Object>();
        List<Order> orderList=orderService.findAllOrders();
        map.put("orderList",orderList);
        return map;
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

}
