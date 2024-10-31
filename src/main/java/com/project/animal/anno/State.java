package com.project.animal.anno;

import com.project.animal.validation.StateValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

//自定义注解
//为了声明公告状态的注解

//元注解 标注用在哪里
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
//元注解 注解保留到什么时候
@Retention(RetentionPolicy.RUNTIME)
/**
 * 元注解：标记state注解为文档化，即该注解会在Javadoc中显示。
 * 适用于其他注解类型，表明被注解的注解类型自身是文档化的，会作为API的一部分被公开。
 */
@Documented
//谁给注解提供校验规则
@Constraint(
        validatedBy = {StateValidation.class}//指定提供校验规则的类
)
public @interface State {

        /**
         * 指定验证失败时的错误消息。
         * 默认值为"{jakarta.validation.constraints.NotNull.message}"。
         * 这允许在不写明具体错误消息的情况下，使用预定义的消息。
         *
         * @return 错误消息字符串。
         */
        String message() default "state的参数只能是published或者draft";

        /**
         * 定义验证约束所属的验证组。
         * 验证组允许对不同的验证场景应用不同的验证规则。
         * 默认情况下，不指定任何组，即适用于所有场景。
         *
         * @return 验证组的类数组。
         */
        Class<?>[] groups() default {};

        /**
         * 定义与验证注解相关联的负载信息。
         * 负载信息可以用于更细粒度地控制验证行为，例如通过安全权限。
         * 默认情况下，不指定任何负载信息。
         *
         * @return 负载信息的类数组。
         */
        Class<? extends Payload>[] payload() default {};
}
