package com.example.demo.repository;

import com.example.demo.bean.Role;
import com.example.demo.bean.ShopInformation;
import com.example.demo.bean.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;
@Mapper
public interface UserRepository {

    //登录验证，关联查询用户权限
    @Select("select * from tb_user where username=#{username}")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result(column = "username",property = "username"),
            @Result(column = "password",property = "password"),
            @Result(column = "sex",property = "sex"),
            @Result(column = "phone",property = "phone"),
            @Result(column = "id",property = "roles",
                    many = @Many(select ="findRoleById" ,
                            fetchType = FetchType.EAGER ))
    })
    User loginCheckByUsername(String username);

    //根据用户id查询用户权限
    //查询用户权限
    @Select("select r.id,r.authority from tb_role r,tb_user_role ur " +
            "where r.id=ur.role_id and user_id=#{id}")
    List<Role> findRoleById(int id);

    //获取个人用户信息
    @Select("select * from tb_user where username=#{username}")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result(column = "username",property = "username"),
            @Result(column = "password",property = "password"),
            @Result(column = "sex",property = "sex"),
            @Result(column = "phone",property = "phone"),
    })
    User findUserByUsername(String username);


    //获取所有USER用户
    @Select("select * from tb_user where id " +
            "in(select user_id from tb_user_role where role_id=2)")
    List<User> getAllUsers();

    //添加用户
    @Insert("insert into tb_user(username,password,sex,phone)" +
            "values(#{username},#{password},#{sex},#{phone})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    int addUser(User user);

    //设置用户权限
    @Insert("insert into tb_user_role(user_id,role_id)" +
            "values(#{user_id},#{role_id})")
    int addRole(int user_id,int role_id);

    //修改用户密码
    @Update("update tb_user set password=#{password} where id=#{id}")
    int updatePassword(User user);

    //删除用户
    @Delete("delete from tb_user where id=#{id}")
    int deleteUser(int id);

    //更新用户
    @Update("update tb_user set sex=#{sex},phone=#{phone} where id=#{id}")
    int updateUser(User user);

    //查询门店用户
    @Select("select * from tb_user where id " +
            "in(select user_id from tb_user_role where role_id=3)")
    @Results({
            @Result(id = true,column = "id",property = "id"),
            @Result(column = "username",property = "username"),
            @Result(column = "sex",property = "sex"),
            @Result(column = "phone",property = "phone"),
            @Result(column = "id",property = "information",
                    one = @One(select ="findShopById" ,
                            fetchType = FetchType.EAGER ))
    })
    List<User> findAllShop();

    //根据shopid查询SHOP信息
    @Select("select * from tb_shop where sid=#{id}")
    @Results({
            @Result(id=true,column = "id",property = "id"),
            @Result(column = "mid",property = "mid"),
            @Result(column = "sid",property = "sid"),
            @Result(column = "address",property = "address"),
            @Result(column = "mid",property = "master",
            one = @One(select = "findUserById",fetchType = FetchType.EAGER))
    })
    ShopInformation findShopById(int id);

    //根据id查询USER信息
    @Select("select id,username from tb_user where id =#{id}")
    User findUserById(int id);

    @Insert("insert into tb_shop(mid,sid,address)" +
            "values(#{mid},#{sid},#{address})")
    int addShop(ShopInformation information);

    //查询店铺主id
    @Select("select mid from tb_shop where sid=#{id}")
    Integer findMasterId(int id);

    //根据id查询用户名
    @Select("select username from tb_user where id =#{id}")
    String findUsernameById(int id);

    //通过Uid查询Shop
    @Select("SELECT tb_user.*,tb_shop.address from tb_user,tb_shop where tb_user.id=tb_shop.sid and tb_shop.mid=#{id}")
    List<User> findShopByUid(int id);

    //USER修改SHOP
    @Update("update tb_shop,tb_user " +
            "set tb_user.sex=#{sex},tb_user.username=#{username},tb_user.phone=#{phone}," +
            "tb_shop.address=#{address} where tb_shop.sid=#{id} and tb_user.id=#{id}")
    int updateShop(User user);

    //添加门店
    @Insert("insert into tb_user(username,password,sex,phone) " +
            "values(#{username},#{password},#{sex},#{phone})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    int addShop2(User user);

    @Insert("insert into tb_shop(mid,sid,address) " +
            "values(#{mid},#{sid},#{address})")
    int addShopInfo(ShopInformation shopInformation);

    //修改密码
    @Update("update tb_user set password=#{password} where id=#{id}")
    int updateUserPassword(User user);

    //获取用户密码
    @Select("select password from tb_user where id=#{id}")
    String findPasswordById(int id);

    //修改头像
    @Update("update tb_user set imgPath=#{imgPath} where id=#{id}")
    int uploadIcon(User user);

    //修改姓名
    @Update("update tb_user set username=#{username} where id=#{id}")
    int updateUserName(User user);

    //修改其他信息
    @Update("update tb_user set sex=#{sex},phone=#{phone} where id=#{id}")
    int updateOther(User user);
}
