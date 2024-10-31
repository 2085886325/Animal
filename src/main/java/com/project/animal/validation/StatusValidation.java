package com.project.animal.validation;

import com.project.animal.anno.AnimalsStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StatusValidation implements ConstraintValidator<AnimalsStatus, String> {
    /**
     * @param value 将来要校验的数据
     * @param constraintValidatorContext
     * @return 校验规则是否正确
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        //提供校验规则
        if (value == null) {
            return false;
        }
        //0未领养
        //1已领养
        //2待批准
        if (value.equals("0") || value.equals("1") || value.equals("2")) {
            return true;
        }
        return false;
    }
}
