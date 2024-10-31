package com.project.animal.service.impl;

import com.project.animal.entity.Result;
import com.project.animal.entity.Users;
import com.project.animal.mapper.UsersMapper;
import com.project.animal.service.IUsersService;
import com.project.animal.utils.Md5Util;
import com.project.animal.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rhy
 * @since 2024-03-04
 */
@Service
public class UsersServiceImpl implements IUsersService {

    @Autowired
    private UsersMapper userMapper;
    @Override
    public Users findByUserName(String username) {
        return userMapper.findByUserName(username);
    }

    @Override
    public void register(String username,String email, String password) {
//        userMapper.register(username,password);
//        不能直接把密码明文存储，这里进行加密
        Md5Util md5Util = new Md5Util();
        String pwd = md5Util.transToMD5(password);
        //加密后的密码
        userMapper.register(username,email,pwd);
    }

    @Override
    public void update(Users user) {
        //最后活动时间
        user.setLastupdateDate(LocalDateTime.now());
        //对密码加密 不让更改密码了，，因为密码在前端无法展示未加密的
        //user.setPassword(Md5Util.transToMD5(user.getPassword()));
        //调用usermapper操作数据库
        userMapper.update(user);
    }

    @Override
    public void updateEmail(String email) {
        //因为这里存储的是一个map类型的数据，我们可以拿到后获取id
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        userMapper.updateEmail(email,userId);
    }

    @Override
    public void updateAvatar(String avatarUrl) {
        //因为这里存储的是一个map类型的数据，我们可以拿到后获取id
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        //System.out.println(userId);
        userMapper.updateAvatar(avatarUrl,userId);
    }

    /**
     * 更新用户密码
     * @param newPwd 新密码，将被加密后存储
     *
     * 注：此方法通过获取当前线程本地存储的用户ID，然后更新对应用户的密码。
     */
    @Override
    public void updatePwd(String newPwd) {
        // 从线程本地存储中获取用户信息，并提取用户ID
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        // 使用MD5加密新密码后，更新用户密码
        userMapper.updatePwd(Md5Util.transToMD5(newPwd),userId);
    }

    @Override
    public void resetPwd(String newPwd, Integer userId) {
        // 使用MD5加密新密码后，更新用户密码
        userMapper.updatePwd(Md5Util.transToMD5(newPwd),userId);
    }


    @Override
    public boolean isAdmin(Integer userid) {
        return userMapper.isAdmin(userid);
    }

    @Override
    public List<Users> getUserList() {
        return userMapper.getUserList();
    }

    @Override
    public void addUser(Users user) {
        String pwd = Md5Util.transToMD5(user.getPassword());
        user.setPassword(pwd);
        //加密后的密码
        userMapper.addUser(user);
    }

    @Override
    public void deleteUser(Integer userid) {
        userMapper.deleteUser(userid);
    }

    @Override
    public Users findByUserId(Integer userid) {
        return userMapper.findByUserId(userid);
    }

    @Override
    public Users findByUserPhone(String phone) {
        return userMapper.findByUserPhone(phone);
    }

}
