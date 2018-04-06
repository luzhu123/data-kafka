package com.keruyun.fintech.commons;

import com.keruyun.fintech.commons.exception.BizException;
import com.keruyun.fintech.commons.web.Response;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.Set;

/**
 * 基于hibernate validator的参数校验
 *
 * @author shuw
 * @version 1.0
 * @date 2017/7/4 17:23
 */
public class Validator {
    private static final javax.validation.Validator vf = Validation.buildDefaultValidatorFactory().getValidator();
    private static final char ERROR_INFO_LINK = ';';//连接多个错误信息的分隔符
    private static final char KEY_PREFIX = '[';
    private static final char KEY_SUFFIX = ']';
    private static final String KEY_VALIDATE_ERROR = "validate_error";

    /**
     * 校验请求参数
     *
     * @param request
     * @param groups
     * @throws BizException
     */
    public static void validateRequest(Object request, Class<?>[] groups) throws BizException {
        Set<ConstraintViolation<Object>> rsList;
        if (groups == null || groups.length == 0) {
            rsList = vf.validate(request);
        } else {
            rsList = vf.validate(request, groups);
        }
        if (rsList.isEmpty()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (ConstraintViolation<Object> rs : rsList) {
            sb.append(ERROR_INFO_LINK).append(KEY_PREFIX).append(rs.getPropertyPath()).append(KEY_SUFFIX).append(rs.getMessage());
        }
        sb.deleteCharAt(0);
        Response resp = new Response(ErrorCode.VALIDATE_ERROR);
        throw new BizException(resp);
    }
    /**
     * 校验业务参数
     *
     * @param request
     * @throws BizException
     */
    public static void validateParam(Object request) throws BizException {
        Set<ConstraintViolation<Object>> rsList = vf.validate(request);
        if (rsList.isEmpty()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (ConstraintViolation<Object> rs : rsList) {
            sb.append(ERROR_INFO_LINK).append(KEY_PREFIX).append(rs.getPropertyPath()).append(KEY_SUFFIX).append(rs.getMessage());
        }
        sb.deleteCharAt(0);
        throw new BizException(ErrorCode.VALIDATE_ERROR.getCode(),sb.toString());
    }
}
