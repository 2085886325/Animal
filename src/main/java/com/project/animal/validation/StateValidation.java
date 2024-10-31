package com.project.animal.validation;

import com.project.animal.anno.State;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

//这个类为了给state自定义注解提供校验规则
//尖括号中填<给哪个注解提供校验规则，校验的数据类型>
public class StateValidation implements ConstraintValidator<State,String> {
    /**
     *
     * @param value 将来要校验的数据
     * @param constraintValidatorContext
     * @return 校验规则是否正确
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        //提供校验规则
        if (value==null){
            return false;
        }
        if (value.equals("published") || value.equals("draft")){
            return true;
        }
        return false;
    }
}
