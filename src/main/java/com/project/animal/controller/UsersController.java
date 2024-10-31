package com.project.animal.controller;


import com.project.animal.entity.Users;
import com.project.animal.entity.Result;
import com.project.animal.service.IUsersService;
import com.project.animal.utils.JwtUtil;
import com.project.animal.utils.Md5Util;
import com.project.animal.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author why
 * @since 2024-03-04
 */
@RestController
@Validated //参数校验需要
@RequestMapping("/users")
@CrossOrigin//允许跨域
public class UsersController {
    @Autowired
    private IUsersService userservice;

    @Autowired
    private StringRedisTemplate redisTemplate;
    //验证是否存在该用户
    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}$") String username, @Email String email, @Pattern(regexp = "^\\S{6,20}$") String password){
        Users user = userservice.findByUserName(username);
        if(user!=null){
            return Result.error("用户名已经被占用");
        }else{
            //未被占用用户名
            //注册
            userservice.register(username,email,password);
            return Result.success();
        }
    }
    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp = "^\\S{5,16}$") String username,@Pattern(regexp = "^\\S{6,20}$") String password){

        Users loginUser = userservice.findByUserName(username);

        if(loginUser==null){
            return Result.error("该用户不存在");
        }

        //用户名匹配上了，数据库中存在该用户
        String pwd = Md5Util.transToMD5(password);
        if(pwd.equals(loginUser.getPassword())){
            HashMap<String, Object> claims = new HashMap<>();
            //此处的claims中不能添加隐私信息，例如密码
            claims.put("id",loginUser.getUserid());
            claims.put("username",loginUser.getUsername());
            //工具类把获得的载荷部分拿给JWT生成Token
            String token = JwtUtil.genToken(claims);
            //把结果给result类的data属性
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            operations.set(token,token,12, TimeUnit.HOURS);

            return Result.success(token);
        }

        return Result.error("密码错误");
    }

    @GetMapping("/userInfo")
    public Result<Users> userInfo(/*@RequestHeader(name = "Authorization") String token*/){
        //根据用户名查询用户,因为使用了ThreadLocal所以此处和形参处注释掉
        /*Map<String,Object> map = JwtUtil.parseToken(token);
        String username = (String) map.get("username");*/

        //使用ThreadLocal存储的数据是线程安全的，一定要在使用完以后调用remove方法
        Map<String,Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        Users users = userservice.findByUserName(username);
        return Result.success(users);
    }

    //退出登录
    @PostMapping("/logout")
    public Result logout(@RequestHeader("Authorization") String token){
        //根据token查询redis中是否有该token
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String redisToken = operations.get(token);
        //如果redis中没有该token，说明用户没有登录，直接返回
        /*if(redisToken==null){
            return Result.error("用户没有登录");
        }*/
        //删除redis中该token
        //operations.getOperations().delete(token);
        redisTemplate.delete(token);
        return Result.success("已经退出成功，继续访问需要重新登录");
    }


    public boolean isAdmin(){
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userid = (Integer) map.get("id");
        //根据userid查询用户是否是管理员
        boolean isadmin = userservice.isAdmin(userid);
        //如果isadmin==0，说明不是管理员，不可以修改
        return isadmin;
    }

    @PostMapping("/update")
   //@Validated这个添加到形参中才能使用实体类的参数校验
    public Result update(@ModelAttribute @Validated Users user){
        //如果你是管理员或者是该用户，可以修改
        if(!isAdmin()){
            return Result.error("非管理员无修改权限");
        }
        //用户名不能一样，手机号也不能一样
        if(!StringUtils.isEmpty(user.getPhone())){
            Users user1 = userservice.findByUserPhone(user.getPhone());
            if(user1!=null){
                if(user1.getUserid()!=user.getUserid()){
                    return Result.error("该手机号已被使用");
                }
            }
        }
        if(!StringUtils.isEmpty(user.getUsername())){
            Users user1 = userservice.findByUserName(user.getUsername());
            if(user1!=null){
                if(user1.getUserid()!=user.getUserid()){
                    return Result.error("该用户名已被使用");
                }
            }
        }
        //如果用户的手机号不为空，则需要匹配正则表达式
        if(!StringUtils.isEmpty(user.getPhone())){
            if(!user.getPhone().matches("^1[3-9]\\d{9}$")){
                return Result.error("手机号格式不正确");
            }
        }
        userservice.update(user);
        return Result.success(user);
    }

    //用户修改自己的信息---不能改类型和是否被封禁，暂时不能改用户名
    @PostMapping("/updateUserBySelf")
    //@Validated这个添加到形参中才能使用实体类的参数校验
    public Result updateUserBySelf(@ModelAttribute @Validated Users user){
        //判断是不是登录的那个用户，不然没有修改权限
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userid = (Integer) map.get("id");
        //如果你不是管理员或者不是该用户，不可以修改
        if(userid!=user.getUserid()&&!isAdmin()){
            return Result.error("你不是该用户，没有修改权限，你的用户id:"+userid +
                    "你输入的id："+user.getUserid());
        }
        //如果用户的手机号不为空，则需要匹配正则表达式
        if(!StringUtils.isEmpty(user.getPhone())){
            if(!user.getPhone().matches("^1[3-9]\\d{9}$")){
                return Result.error("手机号格式不正确");
            }
        }
        //通过用户名获取用户信息
        Users newUser = userservice.findByUserId(user.getUserid());
        //用户自己修改的时候，不能修改类型和是否被封禁
        user.setUsertype("0");
        user.setIsactive(false);
        //前端去掉邮箱提交的输入框，不让改邮箱，另写接口，因为要获取验证码保证安全
        user.setEmail(newUser.getEmail());
        userservice.update(user);
        return Result.success(user);
    }

    //修改邮箱接口
    @PostMapping("/updateEmail")
    public Result updateEmail(@RequestBody Map<String,String> data){
        String email = data.get("email");
        String verificationCode = data.get("verificationCode");
//        System.out.println(data);
        if(email==null||verificationCode==null){
            return Result.error("参数错误");
        }
        if (redisTemplate == null && !redisTemplate.hasKey(email)) {
            return Result.error("验证码已过期");
        }
        String key = "email";
        String sendCode = redisTemplate.opsForValue().get(key);
//        System.out.println(sendCode);
        if(sendCode!=null&&sendCode.equals(verificationCode)){
            userservice.updateEmail(email);
            return Result.success(email);
        }else{
            return Result.error("验证码错误或已过期");
        }
    }



    @PatchMapping("/updateAvatar")
    //@RequestParam是为了标明我们要从query string中获取数据
    public Result updateAvatar(@RequestParam @URL String avatarUrl){
        userservice.updateAvatar(avatarUrl);
        return Result.success();
    }

    @PatchMapping("/updatePwd")
    //@RequestBody添加上才可以自动读取请求体里面的json格式的数据，转换成map集合对象
    public Result updatePwd(@RequestBody Map<String,@Pattern(regexp = "^\\S{6,20}$") String> pwdMap,@RequestHeader("Authorization") String token){
        //1.参数校验
        String oldPwd = pwdMap.get("old_pwd");
        String newPwd = pwdMap.get("new_pwd");
        String rePwd = pwdMap.get("re_pwd");

        //判断三个都不能为空
        if (!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)){
            return Result.error("缺少必要的参数");
        }
        //2.获取用户数据
        Map<String,Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        //这里调用username查询数据的方法获取到用户的数据库中密码
        Users loginUser = userservice.findByUserName(username);
        //System.out.println(loginUser);
        //3.判断输入的旧密码和数据库中的是否一致
        if (!loginUser.getPassword().equals(Md5Util.transToMD5(oldPwd))){
            return Result.error("旧密码输入错误");
        }
        //判断两次新密码是否一致
        if (!newPwd.equals(rePwd)){
            return Result.error("两次新密码输入不一致");
        }
        //判断新旧密码不能一样
        if (newPwd.equals(oldPwd)){
            return Result.error("密码未作修改");
        }
        //4.调用service完成密码更新
        userservice.updatePwd(newPwd);
        //密码更新后删除redis中存储的token
        redisTemplate.delete(token);
        return Result.success();
    }


    //获取用户列表
    @GetMapping("/userList")
    public Result<List<Users>> userList(){
        if (!isAdmin()){
            return Result.error("非管理员无权限查询用户列表");
        }
        List<Users> list = userservice.getUserList();
        return Result.success(list);
    }

    //新增用户
    @PostMapping("/addUser")
    public Result addUser(@ModelAttribute Users user){
        System.out.println(user);
        if (!isAdmin()){
            return Result.error("非管理员无权限新增用户");
        }
        Users Inuser = userservice.findByUserName(user.getUsername());
        if(Inuser!=null){
            return Result.error("用户名已经被占用");
        }
        userservice.addUser(user);
        return Result.success("新增用户成功");
    }

    //通过用户id删除用户
    @DeleteMapping("/deleteUser/{userid}")
    public Result deleteUser(@PathVariable Integer userid){
        if (!isAdmin()){
            return Result.error("非管理员无权限删除用户");
        }
        userservice.deleteUser(userid);
        return Result.success("删除用户成功");
    }

    //查询所有用户的用户名和用户id管理员
    @GetMapping("/getAllName")
    public Result<List<Map<String,Object>>> getAllName(){
        if (!isAdmin()){
            return Result.error("非管理员无权限查询用户列表");
        }
        List<Users> list = userservice.getUserList();
        //把获取到的用户id和用户名封装到List集合中返回给前端
        List<Map<String,Object>> map = new ArrayList<>();
        list.forEach(users -> {
            Map<String,Object> map1 = new HashMap<>();
            map1.put("userid",users.getUserid());
            map1.put("username",users.getUsername());
            map.add(map1);
        });
        return Result.success(map);
    }

    //通过用户id获取用户信息
    @GetMapping("/getUserById/{userid}")
    public Result<Users> getUserById(@PathVariable Integer userid){
        //判断是不是登录的那个用户，不然没有修改权限
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer Loginuserid = (Integer) map.get("id");
        //如果你是管理员或者是该用户，可以查看
        if(userid==Loginuserid||!isAdmin()){
            return Result.error("非管理员无权限查询用户信息");
        }
        Users user = userservice.findByUserId(userid);
        return Result.success(user);
    }

    //通过用户id查用户名
    @GetMapping("/getUserNameById/{userid}")
    public Result<String> getUserNameById(@PathVariable Integer userid){
        Users user = userservice.findByUserId(userid);
        return Result.success(user.getUsername());
    }


    //通过用户id获取用户头像
    @GetMapping("/getAvatarById/{userid}")
    public Result<String> getAvatarById(@PathVariable Integer userid){
       Users user = userservice.findByUserId(userid);
       return Result.success(user.getAvatar());
    }




}
