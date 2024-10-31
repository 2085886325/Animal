package com.project.animal.mapper;

import com.project.animal.entity.Donations;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * (Donations)表数据库访问层
 *
 * @author makejava
 * @since 2024-03-14 23:24:46
 */
@Mapper
public interface DonationsMapper {

    @Select("select * from donations")
    List<Donations> selectAll();


    @Select("select * from donations where userid = #{userId}")
    List<Donations> selectByUserId(Integer userId);


    @Select("select * from donations where donationid = #{donationId}")
    Donations selectByDoId(Integer donationId);


    @Insert("insert into donations(userid, amount," +
            "donationdate,donationtype,description) " +
            "values(#{userId}, #{amount}," +
            "#{donationdate},#{donationtype},#{description})")
    void addDonation(Donations donations);

    @Delete("delete from donations where donationid = #{donationid}")
    void deleteDonation(Integer donationid);


    @Update("update donations set " +
            "userid = #{userId}, " +
            "amount = #{amount}, " +
            "donationdate = #{donationdate}, " +
            "donationtype = #{donationtype}, " +
            "description = #{description} " +
            "where donationid = #{donationid}")
    void updateDonation(Donations donations);
}

