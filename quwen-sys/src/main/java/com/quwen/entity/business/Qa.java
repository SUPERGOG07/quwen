package com.quwen.entity.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.quwen.entity.base.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * 问答记录类
 */
public class Qa extends BaseEntity<Qa> {

    private static final long serialVersionUID = 1L;

    @NotBlank
    private String userId;

    @NotBlank
    private String role;

    /**
     * 问题
     */
    @NotNull
    private String question;

    /**
     * 回答
     */
    @NotNull
    private String answer;

    /**
     * 问题时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss 'GMT'xxx", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss 'GMT'xxx")
    private ZonedDateTime askTime;

    /**
     * 回答时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss 'GMT'xxx", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss 'GMT'xxx")
    private ZonedDateTime answerTime;


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", createDate=").append(createDate);
        sb.append(", updateDate=").append(updateDate);
        sb.append(", delFlag=").append(delFlag);

        sb.append(", userId=").append(userId);
        sb.append(", role=").append(role);
        sb.append(", question=").append(question);
        sb.append(", answer=").append(answer);
        sb.append(", askTime=").append(askTime);
        sb.append(", answerTime=").append(answerTime);

        sb.append("]");
        return sb.toString();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public ZonedDateTime getAskTime() {
        return askTime;
    }

    public void setAskTime(ZonedDateTime askTime) {
        this.askTime = askTime;
    }

    public ZonedDateTime getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(ZonedDateTime answerTime) {
        this.answerTime = answerTime;
    }

}
