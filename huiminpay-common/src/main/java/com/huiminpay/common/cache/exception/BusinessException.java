package com.huiminpay.common.cache.exception;

import com.huiminpay.common.cache.domain.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @AUTHOR: yadong
 * @DATE: 2021/8/20 9:40
 * @DESC:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessException extends RuntimeException{
    private ErrorCode errorCode;
}
