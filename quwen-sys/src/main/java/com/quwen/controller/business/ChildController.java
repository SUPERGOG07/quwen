package com.quwen.controller.business;

import com.alibaba.fastjson2.JSONObject;
import com.quwen.common.CommonResult;
import com.quwen.common.MsgCodeUtil;
import com.quwen.entity.business.Child;
import com.quwen.entity.business.Role;
import com.quwen.service.business.ChildService;
import com.quwen.service.business.TokenService;
import com.quwen.service.business.UserService;
import com.quwen.util.common.CacheUtils;
import com.quwen.util.common.TimeUtils;
import com.quwen.util.wechat.WechatHttpUtils;
import com.quwen.vo.ChildVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/child")
@Slf4j
public class ChildController {

    @Resource
    ChildService childService;

    @Resource
    UserService userService;

    @Resource
    WechatHttpUtils wechatHttpUtils;

    @Resource
    TokenService tokenService;

    @GetMapping("/device")
    public CommonResult getDeviceCode() {
        CommonResult result = new CommonResult().init();

        String deviceCode = String.valueOf(System.currentTimeMillis())+String.valueOf((int) ((Math.random()*9+1)*100));
        while (CacheUtils.existDeviceCode(deviceCode)){
            deviceCode = String.valueOf(System.currentTimeMillis())+String.valueOf((int) ((Math.random()*9+1)*100));
        }
        CacheUtils.addDeviceCode(deviceCode);

        result.success("deviceCode", deviceCode);
        return (CommonResult) result.end();
    }

    @GetMapping("/QRCode/{deviceCode}")
    public void getQRCode(@PathVariable("deviceCode") String deviceCode, HttpServletResponse response) {
        CommonResult result = new CommonResult().init();
        boolean ok = true;
        if (StringUtils.isBlank(deviceCode)) {
            result.error(MsgCodeUtil.MSG_CODE_PARAMETER_IS_NULL);
            ok = false;
        }

        //检查deviceCode是否可用
        if (ok && null == CacheUtils.getChildByDeviceCode(deviceCode)) {
            result.error(MsgCodeUtil.MSG_DEVICE_CODE_NOT_AVAILABLE);
            ok = false;
        }

        //获取二维码文件流
        InputStream inputStream = ok?wechatHttpUtils.getWechatQRCode(deviceCode):null;
        if (inputStream == null) {
            result.fail(MsgCodeUtil.MSG_WECHAT_QRCODE_EXCEPTION);
            ok = false;
        }

        //正常返回图片文件流
        if (ok) {
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            response.addHeader("Content-disposition", "attachment; filename=QRCODE+" + LocalDateTime.now() + ".jpg");
            try {
                ServletOutputStream outputStream = response.getOutputStream();
                byte[] buffer = new byte[1024];
                int len = inputStream.read(buffer);
                while (len != -1) {
                    outputStream.write(buffer, 0, len);
                    len = inputStream.read(buffer);
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                result.fail(MsgCodeUtil.MSG_IMAGE_SEND_ERROR);
                ok = false;
            }
        }

        //异常返回JSON数据
        if (!ok) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
            response.setHeader("Content-disposition", null);
            response.setStatus(200);
            response.resetBuffer();
            try {
                PrintWriter writer = response.getWriter();
                JSONObject resultJson = JSONObject.from(result.end());
                TimeUtils.jsonTimeFormat(resultJson);
                writer.print(resultJson);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                log.error("获得微信二维码接口返回json数据异常",e);
                throw new RuntimeException(e);
            }

        }


    }

    @GetMapping("/state/{deviceCode}")
    public CommonResult getState(@PathVariable("deviceCode") String deviceCode) {
        CommonResult result = new CommonResult().init();
        if (StringUtils.isBlank(deviceCode)) {
            result.error(MsgCodeUtil.MSG_CODE_PARAMETER_IS_NULL);
            return (CommonResult) result.end();
        }

        String childId = CacheUtils.getChildByDeviceCode(deviceCode);
        //检查deviceCode是否可用
        if (null == childId) {
            result.error(MsgCodeUtil.MSG_DEVICE_CODE_NOT_AVAILABLE);
            return (CommonResult) result.end();
        }
        //deviceCode未绑定
        if (StringUtils.equals(childId, "unknown")) {
            result.success("ok", false);
            result.success("childId", "null");
            result.success("parentId", "null");
            result.success("nickname", "null");
            result.success("school", "null");
            result.success("grade", "null");
            result.success("age", 0);
            result.success("gender", "null");
            result.success("accessToken", "null");
            result.success("refreshToken", "null");
            return (CommonResult) result.end();
        }

        Child child = childService.getById(childId);
        if (null == child) {
            result.fail(MsgCodeUtil.MSG_CODE_DATA_NOT_EXIST);
            return (CommonResult) result.end();
        }

        String refreshToken = tokenService.createRefreshToken(childId, Role.CHILD_WATCH);
        String accessToken = tokenService.refreshAccessToken(refreshToken);

        result.success("ok", true);
        result.success("childId", child.getId());
        result.success("parentId", child.getParentId());
        result.success("nickname", child.getNickname());
        result.success("school", child.getSchool());
        result.success("grade", child.getGrade());
        result.success("age", child.getAge());
        result.success("gender", child.getGender());
        result.success("accessToken", accessToken);
        result.success("refreshToken", refreshToken);

        return (CommonResult) result.end();
    }

    @PostMapping("/register")
    public CommonResult register(@RequestParam(value = "parentId") String parentId, @RequestParam(value = "nickname") String nickname,
                                 @RequestParam(value = "school",required = false) String school, @RequestParam(value = "grade",required = false) String grade,
                                 @RequestParam(value = "age",required = false) Integer age, @RequestParam(value = "gender",required = false) String gender,
                                 @RequestParam(value = "deviceCode",required = false) String deviceCode) {

        CommonResult result = new CommonResult().init();
        if (StringUtils.isBlank(parentId) || StringUtils.isBlank(nickname)) {
            result.error(MsgCodeUtil.MSG_CODE_PARAMETER_IS_NULL);
            return (CommonResult) result.end();
        }

        if(!userService.userExist(parentId)){
            result.error(MsgCodeUtil.MSG_CODE_DATA_NOT_EXIST);
            return (CommonResult) result.end();
        }

        Child child = new Child().build();
        child.setParentId(parentId);
        child.setNickname(nickname);
        if (StringUtils.isNotBlank(school)) {
            child.setSchool(school);
        }
        if (StringUtils.isNotBlank(grade)) {
            child.setGrade(grade);
        }
        if (null != age && age >= 0) {
            child.setAge(age);
        }
        if (StringUtils.isNotBlank(gender)) {
            child.setGender(gender);
        }

        if (!childService.save(child)) {
            result.fail(MsgCodeUtil.MSC_DATA_ADDDATA_ERROR);
            return (CommonResult) result.end();
        }

        //有设备码则绑定儿童账号和设备码
        if (StringUtils.isNotBlank(deviceCode)) {
            if (!CacheUtils.bindChildToDeviceCode(deviceCode, child.getId())) {
                result.fail(MsgCodeUtil.MSG_DEVICE_CODE_BIND_ERROR);
                return (CommonResult) result.end();
            }
        }

        result.success("childId", child.getId());
        result.success("parentId", child.getParentId());
        result.success("nickname", child.getNickname());
        result.success("school", child.getSchool());
        result.success("grade", child.getGrade());
        result.success("age", child.getAge());
        result.success("gender", child.getGender());

        return (CommonResult) result.end();
    }

    @PostMapping("/login")
    public CommonResult login(@RequestParam String parentId, @RequestParam String childId, @RequestParam String deviceCode) {
        CommonResult result = new CommonResult().init();
        if (StringUtils.isBlank(parentId) || StringUtils.isBlank(childId) || StringUtils.isBlank(deviceCode)) {
            result.error(MsgCodeUtil.MSG_CODE_PARAMETER_IS_NULL);
            return (CommonResult) result.end();
        }

        Child child = childService.getById(childId);
        //儿童账号存在性校验
        if (null == child || !StringUtils.equals(parentId, child.getParentId())) {
            result.error(MsgCodeUtil.MSG_CODE_DATA_NOT_EXIST);
            return (CommonResult) result.end();
        }

        if (!CacheUtils.bindChildToDeviceCode(deviceCode, childId)) {
            result.fail(MsgCodeUtil.MSG_DEVICE_CODE_BIND_ERROR);
            return (CommonResult) result.end();
        }

        result.success("childId", child.getId());
        result.success("parentId", child.getParentId());
        result.success("nickname", child.getNickname());
        result.success("school", child.getSchool());
        result.success("grade", child.getGrade());
        result.success("age", child.getAge());
        result.success("gender", child.getGender());

        return (CommonResult) result.end();

    }

    @GetMapping("/list/{parentId}")
    public CommonResult childrenOfParent(@PathVariable("parentId") String parentId) {
        CommonResult result = new CommonResult().init();
        if (StringUtils.isBlank(parentId)) {
            result.error(MsgCodeUtil.MSG_CODE_PARAMETER_IS_NULL);
            return (CommonResult) result.end();
        }

        List<ChildVO> children = childService.childrenOfParent(parentId);
        if (null == children || children.size() == 0) {
            result.error(MsgCodeUtil.MSG_CODE_DATA_NOT_EXIST);
            result.setErrMsg(result.getErrMsg() + "家长账户下未查询到子女信息");
            return (CommonResult) result.end();
        }

        result.success("children", children);
        return (CommonResult) result.end();
    }

    @PutMapping("/info")
    public CommonResult updateChildInfo(@RequestParam String parentId, @RequestParam String childId, @RequestParam(required = false) String nickname,
                                        @RequestParam(required = false) String school, @RequestParam(required = false) String grade,
                                        @RequestParam(required = false) Integer age, @RequestParam(required = false) String gender) {
        CommonResult result = new CommonResult().init();
        if (StringUtils.isBlank(parentId) || StringUtils.isBlank(childId)) {
            result.error(MsgCodeUtil.MSG_CODE_PARAMETER_IS_NULL);
            return (CommonResult) result.end();
        }

        Child child = childService.getById(childId);
        //儿童账号存在性校验
        if (null == child || !StringUtils.equals(parentId, child.getParentId())) {
            result.error(MsgCodeUtil.MSG_CODE_DATA_NOT_EXIST);
            return (CommonResult) result.end();
        }

        //更新数据
        if(StringUtils.isNotBlank(nickname)){
            child.setNickname(nickname);
        }
        if(StringUtils.isNotBlank(school)){
            child.setSchool(school);
        }
        if(StringUtils.isNotBlank(grade)){
            child.setGrade(grade);
        }
        if(null!=age&&age>=0){
            child.setAge(age);
        }
        if(StringUtils.isNotBlank(gender)){
            child.setGender(gender);
        }

        if(!childService.updateById(child)){
            result.fail(MsgCodeUtil.MSC_DATA_UPDATADATA_ERROR);
            return (CommonResult) result.end();
        }

        result.success("childId", child.getId());
        result.success("parentId", child.getParentId());
        result.success("nickname", child.getNickname());
        result.success("school", child.getSchool());
        result.success("grade", child.getGrade());
        result.success("age", child.getAge());
        result.success("gender", child.getGender());

        return (CommonResult) result.end();

    }

    @GetMapping("/info/{parentId}/{childId}")
    public CommonResult getChildInfo(@PathVariable("parentId") String parentId,@PathVariable("childId") String childId){
        CommonResult result = new CommonResult().init();
        if (StringUtils.isBlank(parentId) || StringUtils.isBlank(childId)) {
            result.error(MsgCodeUtil.MSG_CODE_PARAMETER_IS_NULL);
            return (CommonResult) result.end();
        }

        Child child = childService.getById(childId);
        //儿童账号存在性校验
        if (null == child || !StringUtils.equals(parentId, child.getParentId())) {
            result.error(MsgCodeUtil.MSG_CODE_DATA_NOT_EXIST);
            return (CommonResult) result.end();
        }

        result.success("childId", child.getId());
        result.success("parentId", child.getParentId());
        result.success("nickname", child.getNickname());
        result.success("school", child.getSchool());
        result.success("grade", child.getGrade());
        result.success("age", child.getAge());
        result.success("gender", child.getGender());

        return (CommonResult) result.end();
    }

    @DeleteMapping("/info/{parentId}/{childId}")
    public CommonResult deleteChild(@PathVariable("parentId") String parentId, @PathVariable("childId") String childId){
        CommonResult result = new CommonResult().init();
        if (StringUtils.isBlank(parentId) || StringUtils.isBlank(childId)) {
            result.error(MsgCodeUtil.MSG_CODE_PARAMETER_IS_NULL);
            return (CommonResult) result.end();
        }

        Child child = childService.getById(childId);
        //儿童账号存在性校验
        if (null == child || !StringUtils.equals(parentId, child.getParentId())) {
            result.error(MsgCodeUtil.MSG_CODE_DATA_NOT_EXIST);
            return (CommonResult) result.end();
        }

        if(!childService.removeById(childId)){
            result.error(MsgCodeUtil.MSC_DATA_DROPDATA_ERROR);
            return (CommonResult) result.end();
        }

        result.success("parentId",parentId);
        result.success("childId",child);

        return (CommonResult) result.end();
    }

}
