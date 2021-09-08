package com.huiminpay.common.cache.exception;

import com.huiminpay.common.cache.domain.CommonErrorCode;
import com.huiminpay.common.cache.domain.ErrorCode;

/**
 * @AUTHOR: yadong
 * @DATE: 2021/8/20 9:59
 * @DESC:
 */
public class BusinessCast {

    public static void cast(ErrorCode errorCode){
        throw new BusinessException(errorCode);
    }
}
