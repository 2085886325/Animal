package com.project.animal.mapper;

import com.project.animal.entity.Users;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author rhy
 * @since 2024-03-04
 */
@Mapper
public interface UsersMapper{

    @Select("select * from users where username=#{username}")
    Users findByUserName(String username);

    @Insert("insert into users(username,email,password,usertype,registration_date,lastupdate_date,isactive) " +
            "values (#{username},#{email},#{password},0,now(),now(),0)")
    void register(String username, String email,String password);

    @Update("update users set username=#{username},email=#{email},phone=#{phone},usertype=#{usertype},avatar=#{avatar}" +
            ",nickname=#{nickname},gender=#{gender},birthdate=#{birthdate},address=#{address},isactive=#{isactive},lastupdate_date=#{lastupdateDate} " +
            "where userid=#{userid}")
    void update(Users user);

    @Update("update users set avatar=#{avatarUrl},lastupdate_date=now() where userid=#{userId}")
    void updateAvatar(String avatarUrl,Integer userId);

    @Update("update users set password=#{newPassword},lastupdate_date=now() where userid=#{userId}")
    void updatePwd(String newPassword,Integer userId);

    //根据userid判断是否是管理员
    @Select("select usertype from users where userid=#{userid}")
    boolean isAdmin(Integer userid);

    @Select("select * from users")
    List<Users> getUserList();

    @Insert("insert into users(username,password,email,phone,usertype,avatar,nickname,gender,birthdate,address,registration_date,lastupdate_date,isactive) "+
            "values (#{username},#{password},#{email},#{phone},#{usertype},#{avatar},#{nickname},#{gender},#{birthdate},#{address},now(),now(),#{isactive})")
    void addUser(Users user);

    @Delete("delete from users where userid=#{userid}")
    void deleteUser(Integer userid);

    @Select("select * from users where userid=#{userid}")
    Users findByUserId(Integer userid);

    @Update("update users set email=#{email},lastupdate_date=now() where userid=#{userId}")
    void updateEmail(String email, Integer userId);

    @Select("select * from users where phone=#{phone}")
    Users findByUserPhone(String phone);
}
