package com.huiminpay.merchant.controller;


import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 刘光辉
 * @since 2021-09-03
 */
@Slf4j
@Controller
@Api(value = "", tags = "", description="")

public class AppController {

    @Autowired
    private AppDTOService appDTOService;
}
