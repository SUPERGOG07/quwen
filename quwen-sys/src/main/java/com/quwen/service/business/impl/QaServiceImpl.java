package com.quwen.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quwen.entity.business.Qa;
import com.quwen.entity.business.Role;
import com.quwen.mapper.business.QaMapper;
import com.quwen.service.business.QaService;
import com.quwen.vo.PageVO;
import com.quwen.vo.QaVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class QaServiceImpl extends ServiceImpl<QaMapper, Qa> implements QaService {

    @Resource
    QaMapper qaMapper;

    @Override
    public PageVO<QaVO> getQaList(String userId, Integer checkAdult, Integer page, Integer size) {
        String role = null;
        if(checkAdult==1){
            role= Role.LIGHT_APP;
        }
        if(checkAdult==2){
            role=Role.CHILD_WATCH;
        }

        Page<Qa> qaPage = new Page<>(page,size);
        qaMapper.selectPage(qaPage,new LambdaQueryWrapper<Qa>().eq(Qa::getUserId,userId).eq(Qa::getRole,role));

        PageVO<QaVO> resultPage = new PageVO<>();
        int total = (int) qaPage.getTotal();
        resultPage.setTotal(total);
        resultPage.setPage(page);
        resultPage.setSize(size);
        int totalPage = (total/size)+1;
        resultPage.setTotalPage(totalPage);
        resultPage.setCheckFirst(page==1);
        resultPage.setCheckLast(page==totalPage);

        int num = 0;
        for(Qa qa:qaPage.getRecords()){
            QaVO vo = new QaVO();
            vo.setQaId(qa.getId());
            vo.setQuestionText(qa.getQuestion());
            vo.setAnswerText(qa.getAnswer());
            vo.setAskTime(qa.getAskTime());
            vo.setAnswerTime(qa.getAnswerTime());
            resultPage.put(vo);
        }
        resultPage.setNum(num);

        return resultPage;
    }

    @Override
    public boolean saveQa(String userId, Integer checkAdult, String question, String answer, ZonedDateTime askTime, ZonedDateTime answerTime) {
        Qa qa = new Qa();
        qa.setUserId(userId);
        if(checkAdult==1){
            qa.setRole(Role.LIGHT_APP);
        }
        if(checkAdult==2){
            qa.setRole(Role.CHILD_WATCH);
        }
        qa.setQuestion(question);
        qa.setAnswer(answer);
        qa.setAskTime(askTime);
        qa.setAnswerTime(answerTime);

        return qaMapper.insert(qa)>0;
    }
}
