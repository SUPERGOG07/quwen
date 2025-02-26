package com.quwen.controller.test;

import com.quwen.common.CommonResult;
import com.quwen.common.MsgCodeUtil;
import com.quwen.entity.test.Test;
import com.quwen.service.business.TokenService;
import com.quwen.service.test.TestService;
import com.quwen.util.common.BeanCustomUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    TestService testService;

    @Resource
    TokenService tokenService;

    @GetMapping("/ping")
    public String ping(){
        return "pong";
    }


    @PostMapping("/bodyParam")
    public CommonResult bodyParam(@RequestParam String str){

        CommonResult result = new CommonResult().init();
        if(StringUtils.isBlank(str)){
            result.setMsgCode(MsgCodeUtil.MSG_CODE_ILLEGAL_ARGUMENT);
            result.setErrMsg("str is blank");
            return (CommonResult) result.end();
        }

        result.success("returnStr",str);
        return (CommonResult) result.end();
    }

    @PostMapping("/pathParam/{str}")
    public CommonResult pathParam(@PathVariable("str") String str){

        CommonResult result = new CommonResult().init();
        if(StringUtils.isBlank(str)){
            result.setMsgCode(MsgCodeUtil.MSG_CODE_ILLEGAL_ARGUMENT);
            result.setErrMsg("str is blank");
            return (CommonResult) result.end();
        }

        result.success("returnStr",str);
        return (CommonResult) result.end();
    }

    @PostMapping("/add")
    public CommonResult add(@Validated @RequestBody Test test, BindingResult bindingResult){

        CommonResult result = new CommonResult().init();
        if(bindingResult.hasErrors()){
            return (CommonResult) result.failIllegalArgument(bindingResult.getFieldErrors()).end();
        }

        Test test1 = new Test();
        BeanCustomUtils.copyProperties(test,test1);

        if(!testService.save(test1)){
            result.error(MsgCodeUtil.MSC_DATA_ADDDATA_ERROR);
            return (CommonResult) result.end();
        }

        result.success("returnTest",test1);
        return (CommonResult) result.end();
    }

    @PutMapping("/update")
    public CommonResult update(@Validated @RequestBody Test test, BindingResult bindingResult){

        CommonResult result = new CommonResult().init();
        if(bindingResult.hasErrors()){
            return (CommonResult) result.failIllegalArgument(bindingResult.getFieldErrors()).end();
        }

        Test test1 = new Test();
        BeanCustomUtils.copyProperties(test,test1);

        if(!testService.updateById(test1)){
            result.error(MsgCodeUtil.MSC_DATA_UPDATADATA_ERROR);
            return (CommonResult) result.end();
        }

        result.success("returnTest",test1);
        return (CommonResult) result.end();
    }

    @GetMapping("/get")
    public CommonResult get(@RequestParam String id){

        CommonResult result = new CommonResult().init();
        if(StringUtils.isBlank(id)){
            result.error(MsgCodeUtil.MSG_CODE_ILLEGAL_ARGUMENT);
            return (CommonResult) result.end();
        }

        Test test = testService.getById(id);
        if(null==test){
            result.error(MsgCodeUtil.MSG_CODE_DATA_NOT_EXIST);
            return (CommonResult) result.end();
        }

        result.success("returnTest",test);
        return (CommonResult) result.end();
    }

    @DeleteMapping("/delete")
    public CommonResult delete(@RequestParam String id){
        CommonResult result = new CommonResult().init();
        if(StringUtils.isBlank(id)){
            result.error(MsgCodeUtil.MSG_CODE_ILLEGAL_ARGUMENT);
            return (CommonResult) result.end();
        }

        if(!testService.removeById(id)){
            result.error(MsgCodeUtil.MSC_DATA_DROPDATA_ERROR);
            return (CommonResult) result.end();
        }

        result.success();
        return (CommonResult) result.end();
    }

    @GetMapping("/token")
    public CommonResult token(@RequestParam String userId, @RequestParam String role){
        CommonResult result = new CommonResult().init();
        if(StringUtils.isBlank(userId)||StringUtils.isBlank(role)){
            result.error(MsgCodeUtil.MSG_CODE_ILLEGAL_ARGUMENT);
            return (CommonResult) result.end();
        }

        String refreshToken = tokenService.createRefreshToken(userId,role);
        String accessToken = tokenService.refreshAccessToken(refreshToken);
        result.success("accessToken", accessToken);
        result.success("refreshToken",refreshToken);

        return (CommonResult) result.end();
    }

}