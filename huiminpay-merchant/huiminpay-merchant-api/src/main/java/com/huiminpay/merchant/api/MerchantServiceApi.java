package com.huiminpay.merchant.api;

import com.huiminpay.common.cache.exception.BusinessException;
import com.huiminpay.merchant.dto.MerchantDTO;
import com.huiminpay.merchant.dto.StoreDTO;

/**
 * @AUTHOR: yadong
 * @DATE: 2021/8/17 15:34
 * @DESC:
 */
public interface MerchantServiceApi {

    public MerchantDTO findMerchantById(Long id);

    //商户注册
    public MerchantDTO registerMerchant(MerchantDTO merchantDTO);


}
