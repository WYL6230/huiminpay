package com.huiminpay.merchant.service;

import com.huiminpay.merchant.api.MerchantServiceApi;

import com.huiminpay.merchant.dto.MerchantDTO;

import com.huiminpay.merchant.entity.Merchant;
import com.huiminpay.merchant.mapper.MerchantMapper;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @AUTHOR: yadong
 * @DATE: 2021/8/17 15:39
 * @DESC:
 */
@org.apache.dubbo.config.annotation.Service
public class MerchantService implements MerchantServiceApi {


    @Autowired
    private MerchantMapper merchantMapper;




    @Override
    public MerchantDTO findMerchantById(Long id) {

        Merchant merchant = merchantMapper.selectById(id);

        MerchantDTO merchantDTO = new MerchantDTO();
        //对象对拷
        BeanUtils.copyProperties(merchant,merchantDTO);
        return merchantDTO;
    }

    @Override
    @Transactional
    public MerchantDTO registerMerchant(MerchantDTO merchantDTO) {

        // merchantDTO.setId(merchant.getId());
//        MerchantDTO dto = MerchantConvert.INSTANCE.entity2dto(merchant);
        //添加员工信息等
        //....

        return merchantDTO;
    }
}
