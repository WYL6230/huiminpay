package com.huiminpay.merchant.controller;


import org.springframework.stereotype.Controller;
import com.huiminpay.merchant.service.IStaffService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

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
public class StaffController {

    @Autowired
    private StaffDTOService staffDTOService;
}
