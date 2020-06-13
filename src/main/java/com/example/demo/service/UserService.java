package com.example.demo.service;

import com.example.demo.bean.Role;
import com.example.demo.bean.ShopInformation;
import com.example.demo.bean.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Resource
    private UserRepository userRepository;

    public User loginCheck(String username){
        return userRepository.loginCheckByUsername(username);
    }

    public User findUserByUsername(String username){
        return userRepository.findUserByUsername(username);
    }

    public List<User> findAllUsers(){
        return userRepository.getAllUsers();
    }

    public int addUser(User user){
        return userRepository.addUser(user);
    }

    public int addRole(int user_id,int role_id){
        return userRepository.addRole(user_id,role_id);
    }

    public int updatePassword(User user){
        return userRepository.updatePassword(user);
    }

    public int deleteUser(int id){
        return userRepository.deleteUser(id);
    }

    public int updateUser(User user){
        return userRepository.updateUser(user);
    }

    public List<User> findAllShop(){
        return userRepository.findAllShop();
    }

    public int addShop(ShopInformation information){
        return userRepository.addShop(information);
    }

    public int findMasterId(int id){
        return userRepository.findMasterId(id);
    }

    public List<User> findShopByUid(int id){
        return userRepository.findShopByUid(id);
    }

    public int updateShop(User user){
        return userRepository.updateShop(user);
    }

    public int addShop2(User user){
        return userRepository.addShop2(user);
    }

    public int addShopInfo(ShopInformation information){
        return userRepository.addShopInfo(information);
    }

    public int updateUserPassword(User user){
        return userRepository.updateUserPassword(user);
    }

    public String findPasswordById(int id){
        return userRepository.findPasswordById(id);
    }

    public int uploadIcon(User user){
        return userRepository.uploadIcon(user);
    }

    public int updateUsername(User user){
        return userRepository.updateUserName(user);
    }

    public int updateOther(User user){
        return userRepository.updateOther(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepository.loginCheckByUsername(username);
        if (user==null){
            throw new UsernameNotFoundException("用户名不存在");
        }
        //保存用户权限
        List<GrantedAuthority> authorities
                =new ArrayList<GrantedAuthority>();
        List<Role> roles=user.getRoles();
        for (Role role:roles){
            authorities.add(new SimpleGrantedAuthority(
                    role.getAuthority()));
        }

        return new org.springframework.security.core.
                userdetails.User(user.getUsername(),
                user.getPassword(), authorities);
    }
}
