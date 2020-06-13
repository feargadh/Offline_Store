package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo.bean.User;
import com.example.demo.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AppController {

    @Resource
    private UserService userService;

    //系统登录
    @RequestMapping("/login")
    public ModelAndView loginPage(){
        return new ModelAndView("login");
    }

    //系统登出
    @RequestMapping("/logout")
    public String logoutPage(HttpServletRequest request,
                             HttpServletResponse response)
    {
        Authentication auth= SecurityContextHolder.getContext()
                .getAuthentication();
        if (auth!=null){
            new SecurityContextLogoutHandler()
                    .logout(request,response,auth);
        }
        return "redirect:/login";
    }

    //管理员主页
    @RequestMapping("/admin/home")
    public ModelAndView adminHome(Model model){
        User user=userService.findUserByUsername(getUsername());
        if (user!=null){
            System.out.println(user.toString());
        }else {
            System.out.println("没有找到该用户");
        }
        model.addAttribute("user",user);
        model.addAttribute("role",getAuthority());
        return new ModelAndView("adminHome");
    }

    //店铺主页
    @RequestMapping("/shop/home")
    public ModelAndView shopHome(Model model){
        User user=userService.findUserByUsername(getUsername());
        if (user!=null){
            System.out.println(user.toString());
        }else {
            System.out.println("没有找到该用户");
        }
        model.addAttribute("mid",userService.findMasterId(user.getId()));
        model.addAttribute("user",user);
        model.addAttribute("role",getAuthority());
        return new ModelAndView("shopHome");
    }


    //用户主页
    @RequestMapping("/user/home")
    public ModelAndView userHome(Model model){
        User user=userService.findUserByUsername(getUsername());
        if (user!=null){
            System.out.println(user.toString());
        }else {
            System.out.println("没有找到该用户");
        }
        model.addAttribute("user",user);
        model.addAttribute("userJsonString", JSON.toJSONString(user));
        model.addAttribute("role",getAuthority());
        return new ModelAndView("userHome");
    }


    //没有权限
    @RequestMapping(value = "/accessDenied")
    public ModelAndView accessDeniedPage(Model model){
        model.addAttribute("user",getUsername());
        model.addAttribute("role",getAuthority());
        return new ModelAndView("accessDenied");
    }

    //没有权限
    @RequestMapping(value = "/user/test")
    public ModelAndView testPage(Model model){
        model.addAttribute("user",getUsername());
        model.addAttribute("role",getAuthority());
        return new ModelAndView("imgTest");
    }


    private String getAuthority() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> roles =new ArrayList<String>();
        for (GrantedAuthority a : authentication.getAuthorities()){
            roles.add(a.getAuthority());
        }
        return roles.toString();
    }

    private String getUsername() {

        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("username="+username);
        return username;
    }
}
