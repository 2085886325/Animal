package com.project.animal.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author rhy
 * @since 2024-03-04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @NotNull//需要在实体类（形参左边）中添加@validated，这些小的注解才能生效
    //此处有@NotNull @Pattern @Email @NotEmpty都是同理
    private Integer userid;

    /**
     * 用户名
     */
    @NotEmpty
    //用户名只包含字母（大小写）、数字和下划线，并且长度在5到16个字符之间。
    @Pattern(regexp = "^\\S{5,16}$")
    private String username;

    /**
     * 密码
     */
    @JsonIgnore 
    //密码长度为6到20个字符，允许使用字母（大小写）、数字和特殊字符：
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+]{6,20}$")
//    要注意的是，当前端以json格式向后台传password的值，且后台是以实体User接收时，这时候@JsonIgnore会忽略，即不接收password字段的值。
//    若想避免此类情况，建议使用form表单的形式提交参数，而非json格式。
    //让springmvc把当前对象转换成json字符串的时候，
    //忽略password,最终的json字符串中就没有password这个属性
    private String password;

    /**
     * 邮箱
     */
    //@NotEmpty//不能为空且不能是空白字符串
    @Email()
    @Nullable
    private String email;

    /**
     * 电话
     */
    private String phone;

    /**
     * 用户类型
     */
    private String usertype;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 性别
     */
    private String gender;

    /**
     * 出生日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime birthdate;

    /**
     * 地址
     */
    private String address;

    /**
     * 注册日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime registrationDate;

    /**
     * 最后登录日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime lastupdateDate;

    /**
     * 是否激活，就是是否被禁用，1为禁用，0为正常
     */
//    @JsonIgnore
    private Boolean isactive;

}

