package com.huiminpay.common.cache.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @AUTHOR: yadong
 * @DATE: 2021/8/20 9:45
 * @DESC:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestErrorResponse {
    private int errCode;

    private String errMessage;
}
