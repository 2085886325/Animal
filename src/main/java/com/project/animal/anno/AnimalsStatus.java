package com.project.animal.anno;

import com.project.animal.validation.StatusValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


//自定义注解
//为了声明动物领养状态的注解

/**
 * 定义了一个约束注解，用于验证对象字段或方法参数的状态是否有效。
 * 此注解可以在字段级别和参数级别上应用，用于运行时检查。
 * 验证逻辑的具体实现位于StatusValidation类中。
 */
@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)//此注解可以在字段级别和参数级别上应用，用于运行时检查。
@Documented
@Constraint(
        validatedBy = {StatusValidation.class}
)
public @interface AnimalsStatus {
    //0未领养
    //1已领养
    //2待批准
    /**
     * 错误消息模板，用于定制当验证失败时的错误消息。
     * 可以通过此模板定制特定的验证错误消息，提高错误消息的可读性和可用性。
     */
    String message() default "只能是0未领养 1已领养 2待批准 其中一个数字";
    /**
     * 定义了注解应用的范围，此处省略详细注释。
     */
    Class<?>[] groups() default {};
    /**
     * 定义了与该约束相关的配置类，此处省略详细注释。
     */
    Class<? extends Payload>[] payload() default {};
}
