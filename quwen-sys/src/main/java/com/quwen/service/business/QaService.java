package com.quwen.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quwen.entity.business.Qa;
import com.quwen.vo.PageVO;
import com.quwen.vo.QaVO;

import java.time.ZonedDateTime;
import java.util.List;

public interface QaService extends IService<Qa> {

    PageVO<QaVO> getQaList(String userId, Integer checkAdult, Integer page, Integer size);

    boolean saveQa(String userId, Integer checkAdult, String question, String answer, ZonedDateTime askTime,ZonedDateTime answerTime);
}
