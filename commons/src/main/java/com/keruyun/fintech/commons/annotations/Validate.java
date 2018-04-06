package com.keruyun.fintech.commons.annotations;

import java.lang.annotation.*;

/**
 * @author shuw
 * @version 1.0
 * @date 2016/10/24 14:05
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Validate {

    int reqParamIndex() default 0;//需要校验的对象在目标方法参数列表中的序号，从0开始

    Class<?>[] groups() default {};//分组，指定分组后则之校验对应的分组标识的字段

    boolean checkTimestamp() default true;//是否校验时间戳，跟服务器的时间差不能超过指定的范围

    boolean checkSign() default true;//是否验签，基本的参数校验通过后进行签名校验
}