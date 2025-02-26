package com.quwen.vo;

import java.time.ZonedDateTime;

public class QaVO {
    private String qaId;

    private String questionText;

    private String answerText;

    private ZonedDateTime askTime;

    private ZonedDateTime answerTime;

    public String getQaId() {
        return qaId;
    }

    public void setQaId(String qaId) {
        this.qaId = qaId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
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
