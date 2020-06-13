package com.example.demo.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class AppAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Override
    public RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }

    //Spring Security 通过RedirectStrategy 对象负责所有事务重定向
    private RedirectStrategy redirectStrategy =new DefaultRedirectStrategy();



    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        String targetUrl = determineTargetUrl(authentication);
        redirectStrategy.sendRedirect(request,response,targetUrl);
    }

    protected String determineTargetUrl(Authentication authentication){
        String url="";

        Collection<? extends GrantedAuthority> authorities=authentication.getAuthorities();

        List<String> roles =new ArrayList<String>();

        for (GrantedAuthority a: authorities){
            roles.add(a.getAuthority());
            System.out.println(a.getAuthority());
        }

        if (isAdmin(roles)){
            url="/admin/home";
        }else if (isUser(roles)){
            url="/user/home";
        } else if (isShop(roles)) {
            url="/shop/home";
        } else {
            url = "/accessDenied";
        }
        System.out.println("url="+url);
        return url;
    }

    private boolean isUser(List<String> roles){
        if (roles.contains(("ROLE_USER"))){
            return true;
        }
        return false;
    }

    private boolean isShop(List<String> roles){
        if (roles.contains(("ROLE_SHOP"))){
            return true;
        }
        return false;
    }

    private boolean isAdmin(List<String> roles){
        if (roles.contains("ROLE_ADMIN")){
            return true;
        }
        return false;
    }

    public void  setRedirectStrategy(RedirectStrategy redirectStrategy){
        this.redirectStrategy=redirectStrategy;
    }
}
