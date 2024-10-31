package com.project.animal.service;

import com.project.animal.entity.Users;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author rhy
 * @since 2024-03-04
 */
public interface IUsersService {

    //根据用户名获取到用户信息
    Users findByUserName(String username);

    //注册
    void register(String username,String email, String password);

    //更新用户信息
    void update(Users user);


    void updateEmail(String email);

    //更新头像
    void updateAvatar(String avatarUrl);

    //修改密码
    void updatePwd(String newPwd);


    void resetPwd(String newPwd, Integer userId);

    //判断用户是不是管理员
    boolean isAdmin(Integer userid);

    List<Users> getUserList();

    void addUser(Users user);

    void deleteUser(Integer userid);

    Users findByUserId(Integer userid);

    Users findByUserPhone(String phone);
}
