package com.quwen.controller.business;

import com.alibaba.fastjson2.JSONObject;
import com.quwen.common.CommonResult;
import com.quwen.common.MsgCodeUtil;
import com.quwen.entity.business.Child;
import com.quwen.entity.business.User;
import com.quwen.service.business.ChildService;
import com.quwen.service.business.QaService;
import com.quwen.service.business.UserService;
import com.quwen.util.common.TimeUtils;
import com.quwen.util.gpt.GPTUtils;
import com.quwen.util.xunfei.AudioUtils;
import com.quwen.util.xunfei.XunFeiUtils;
import com.quwen.vo.PageVO;
import com.quwen.vo.QaVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/qa")
public class QaController {

    @Resource
    QaService qaService;

    @Resource
    ChildService childService;

    @Resource
    UserService userService;

    @Resource
    XunFeiUtils xunFeiUtils;

    @Resource
    GPTUtils gptUtils;

    @PostMapping("/ask")
    public CommonResult ask(@RequestParam Integer checkAdult, @RequestParam("userId") String questioner,
                            @RequestParam String askTime, @RequestParam("question") MultipartFile question,
                            @RequestParam String fileType) throws Exception {
        CommonResult result = new CommonResult().init();
        if (StringUtils.isBlank(questioner) || question.isEmpty() || StringUtils.isBlank(askTime) || null == checkAdult) {
            result.error(MsgCodeUtil.MSG_CODE_PARAMETER_IS_NULL);
            return (CommonResult) result.end();
        }
        if (checkAdult < 1 || checkAdult > 2) {
            result.error(MsgCodeUtil.MSG_CODE_ILLEGAL_ARGUMENT);
            result.setErrMsg(result.getErrMsg() + "checkAdult只能为1或2");
            return (CommonResult) result.end();
        }
        if (!StringUtils.equals(fileType, "mp3") && !StringUtils.equals(fileType, "wav") && !StringUtils.equals(fileType, "aac")) {
            result.error(MsgCodeUtil.MSG_CODE_ILLEGAL_ARGUMENT);
            return (CommonResult) result.end();
        }
        ZonedDateTime askDateTime = TimeUtils.str2time(askTime);
        if (null == askDateTime) {
            result.error(MsgCodeUtil.MSG_CODE_ILLEGAL_ARGUMENT);
            result.setErrMsg(result.getErrMsg() + "时间格式错误");
            return (CommonResult) result.end();
        }
        if (!TimeUtils.validTime(askDateTime)) {
            result.error(MsgCodeUtil.MSG_CODE_ILLEGAL_ARGUMENT);
            result.setErrMsg(result.getErrMsg() + "时间不在规定范围内");
            return (CommonResult) result.end();
        }

        //查看用户余额
        String parentId = questioner;
        if (checkAdult == 2) {
            parentId = childService.findParent(questioner);
        }
        if(StringUtils.isBlank(parentId)){
            result.error(MsgCodeUtil.MSG_CODE_DATA_NOT_EXIST);
            result.setErrMsg(result.getErrMsg() + "父用户不存在");
            return (CommonResult) result.end();
        }
        if(!userService.checkBalance(parentId)){
            result.error(MsgCodeUtil.MSG_CODE_BALANCE_NOT_ENOUGH);
            return (CommonResult) result.end();
        }

        log.info("问答时间开始:{}", System.currentTimeMillis());

        //语音储存
        FileInputStream inputStream = (FileInputStream) question.getInputStream();
        String inputFilePath = AudioUtils.saveAudioFile(inputStream, fileType);
        //文件转换
        String outputFilePath = AudioUtils.audioToPcm(inputFilePath);
        if (null == outputFilePath || outputFilePath.isBlank()) {
            new File(inputFilePath).delete();
            result.fail(MsgCodeUtil.MSG_XUNFEI_AUDIO_TO_TEXT);
            return (CommonResult) result.end();
        }
        log.info("文件转换结束:{}", System.currentTimeMillis());
        //语音转文字
        FileInputStream outPutFileInputStream = new FileInputStream(outputFilePath);
        String text = xunFeiUtils.audio2text(outPutFileInputStream);
        if (null == text || text.isBlank()) {
            new File(inputFilePath).delete();
            new File(outputFilePath).delete();
            result.error(MsgCodeUtil.MSG_XUNFEI_AUDIO_TO_TEXT);
            return (CommonResult) result.end();
        }
        //删除临时文件
        new File(inputFilePath).delete();
        new File(outputFilePath).delete();
        log.info("语音转文字结束:{}", System.currentTimeMillis());
        //回答文本
        String answer;

        //GPT获取回答文本
        answer = gptUtils.chatGPT(text);
        if (StringUtils.isBlank(answer)) {
            result.fail(MsgCodeUtil.MSG_OPENAI_GPT_ERROR);
            return (CommonResult) result.end();
        }
        log.info("请求GPT结束:{}", System.currentTimeMillis());

        //文本合规检测
        Integer state = xunFeiUtils.sensitiveCheck(text + ";" + answer);
        //检测失败
        if (state == -1) {
            result.error(MsgCodeUtil.MSG_XUNFEI_SENSITIVE_ERROR);
            return (CommonResult) result.end();
        }
        //文本违规
        if (state == -2) {
            answer = "小趣有点不明白了，可以再问一遍或者换个问题吗?";

            result.success("checkAdult", checkAdult);
            result.success("userId", questioner);
            result.success("qaId", "null");
            result.success("questionText", text);
            result.success("answerText", answer);
            result.success("askTime", askTime);
            result.success("answerTime", TimeUtils.getNowTimeFormat());
            return (CommonResult) result.end();
        }
        log.info("文本合规检查结束:{}", System.currentTimeMillis());

        //扣除次数
        if (!userService.reduceBalance(parentId)) {
            result.error(MsgCodeUtil.MSG_BALANCE_DEDUCT_ERROR);
            return (CommonResult) result.end();
        }

        //储存问答记录
        ZonedDateTime answerDateTime = TimeUtils.getNowTime();
        qaService.saveQa(questioner, checkAdult, text, answer, askDateTime, answerDateTime);

        //成功返回
        result.success("checkAdult", checkAdult);
        result.success("userId", questioner);
        result.success("qaId", "null");
        result.success("questionText", text);
        result.success("answerText", answer);
        result.success("askTime", askTime);
        result.success("answerTime", TimeUtils.getTimeFormat(answerDateTime));

        return (CommonResult) result.end();
    }


    /**
     * @param userId
     * @param checkAdult 小程序选1, 手表选2
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list/{userId}/{checkAdult}/{page}/{size}")
    public CommonResult getQaList(@PathVariable("userId") String userId, @PathVariable("checkAdult") Integer checkAdult,
                                  @PathVariable("page") Integer page, @PathVariable("size") Integer size) {
        CommonResult result = new CommonResult().init();
        if (StringUtils.isBlank(userId) || checkAdult < 1 || checkAdult > 2 || page < 1 || size < 1) {
            result.error(MsgCodeUtil.MSG_CODE_ILLEGAL_ARGUMENT);
            return (CommonResult) result.end();
        }

        String nickname = null;
        if (checkAdult == 1) {
            User user = userService.getById(userId);
            if (null == user) {
                result.error(MsgCodeUtil.MSG_CODE_DATA_NOT_EXIST);
                return (CommonResult) result.end();
            }
            nickname = user.getNickname();
        }
        if (checkAdult == 2) {
            Child child = childService.getById(userId);
            if (null == child) {
                result.error(MsgCodeUtil.MSG_CODE_DATA_NOT_EXIST);
                return (CommonResult) result.end();
            }
            nickname = child.getNickname();
        }


        PageVO<QaVO> resultPage = qaService.getQaList(userId, checkAdult, page, size);
        result.success("pageList", resultPage);
        result.success("checkAdult", checkAdult);
        result.success("userId", userId);
        result.success("nickname", nickname);

        return (CommonResult) result.end();
    }


    @PostMapping("/audio2text")
    public CommonResult audio2text(@RequestParam("file") MultipartFile file, @RequestParam("fileType") String fileType) throws Exception {
        CommonResult result = new CommonResult().init();
        if (file.isEmpty()) {
            result.error(MsgCodeUtil.MSG_CODE_PARAMETER_IS_NULL);
            return (CommonResult) result.end();
        }
        if (!StringUtils.equals(fileType, "mp3") && !StringUtils.equals(fileType, "wav") && !StringUtils.equals(fileType, "aac")) {
            result.error(MsgCodeUtil.MSG_CODE_ILLEGAL_ARGUMENT);
            return (CommonResult) result.end();
        }

        //语音储存
        FileInputStream inputStream = (FileInputStream) file.getInputStream();
        String inputFilePath = AudioUtils.saveAudioFile(inputStream, fileType);

        //文件转换
        String outputFilePath = AudioUtils.audioToPcm(inputFilePath);
        if (null == outputFilePath || outputFilePath.isBlank()) {
            new File(inputFilePath).delete();
            result.fail(MsgCodeUtil.MSG_XUNFEI_AUDIO_TO_TEXT);
            return (CommonResult) result.end();
        }

        //语音转文字
        FileInputStream outPutFileInputStream = new FileInputStream(outputFilePath);
        String text = xunFeiUtils.audio2text(outPutFileInputStream);
        if (null == text || text.isBlank()) {
            new File(inputFilePath).delete();
            new File(outputFilePath).delete();
            result.error(MsgCodeUtil.MSG_XUNFEI_AUDIO_TO_TEXT);
            return (CommonResult) result.end();
        }

        new File(inputFilePath).delete();
        new File(outputFilePath).delete();
        result.success("text", text);
        return (CommonResult) result.end();
    }

    @PostMapping("/text2audio")
    public void text2audio(@RequestParam("text") String text,@RequestParam("resultType") String resultType, HttpServletResponse response) throws Exception {
        CommonResult result = new CommonResult().init();
        boolean ok = true;
        if (StringUtils.isBlank(text)) {
            result.error(MsgCodeUtil.MSG_CODE_PARAMETER_IS_NULL);
            errorBack(result,response);
            return;
        }
        if(!StringUtils.equals(resultType,"mp3")&&!StringUtils.equals(resultType,"wav")&&!StringUtils.equals(resultType,"aac")){
            result.error(MsgCodeUtil.MSG_CODE_ILLEGAL_ARGUMENT);
            errorBack(result,response);
            return;
        }

        String filePath = xunFeiUtils.text2audio(text);
        if(StringUtils.isBlank(filePath)){
            result.error(MsgCodeUtil.MSG_XUNFEI_TEXT_TO_AUDIO);
            errorBack(result,response);
            return;
        }

        //发送文件流
        String outputFilePath = AudioUtils.pcmToAudio(filePath,resultType);
        if (null != outputFilePath) {
            File outputFile = new File(outputFilePath);
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.addHeader("Content-disposition", "attachment; filename=" + UUID.randomUUID() + "."+resultType);
            try (ServletOutputStream outputStream = response.getOutputStream();
                 FileInputStream inputStream = new FileInputStream(outputFile)) {
                byte[] buffer = new byte[1024];
                int len = inputStream.read(buffer);
                while (len != -1) {
                    outputStream.write(buffer, 0, len);
                    len = inputStream.read(buffer);
                }
                outputStream.flush();
                return;
            } catch (IOException e) {
                log.error("文字转语音返回数据流失败", e);
                throw new RuntimeException(e);
            } finally {
                outputFile.delete();
            }
        }

        //失败
        result.error(MsgCodeUtil.MSG_XUNFEI_TEXT_TO_AUDIO);
        errorBack(result,response);
    }

    /**
     * 设置响应，返回错误json
     * @param result
     * @param response
     */
    private void errorBack(CommonResult result, HttpServletResponse response){
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
            log.error("文字转语音接口返回json数据错误", e);
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/sensitive/{text}")
    public CommonResult sensitiveCheck(@PathVariable("text") String text) throws Exception {
        CommonResult result = new CommonResult().init();
        if (StringUtils.isBlank(text)) {
            result.error(MsgCodeUtil.MSG_CODE_PARAMETER_IS_NULL);
            return (CommonResult) result.end();
        }

        Integer state = xunFeiUtils.sensitiveCheck(text);
        if (state == -1) {
            result.error(MsgCodeUtil.MSG_XUNFEI_SENSITIVE_ERROR);
            return (CommonResult) result.end();
        }

        if (state == -2) {
            result.success("ok", false);
            result.success("text", "文本存在违规内容");
            return (CommonResult) result.end();
        }

        result.success("ok", true);
        result.success("text", "文本合规");
        return (CommonResult) result.end();
    }

    @PostMapping("/gpt")
    public CommonResult gpt(@RequestParam String text) {
        CommonResult result = new CommonResult().init();
        if (StringUtils.isBlank(text)) {
            result.error(MsgCodeUtil.MSG_CODE_PARAMETER_IS_NULL);
            return (CommonResult) result.end();
        }

        String answer = gptUtils.chatGPT(text);
        if (StringUtils.isBlank(answer)) {
            result.fail(MsgCodeUtil.MSG_OPENAI_GPT_ERROR);
            return (CommonResult) result.end();
        }

        result.success("answer", answer);
        return (CommonResult) result.end();
    }

}
