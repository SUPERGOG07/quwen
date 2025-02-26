package com.quwen.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quwen.entity.business.Child;
import com.quwen.mapper.business.ChildMapper;
import com.quwen.service.business.ChildService;
import com.quwen.vo.ChildVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class ChildServiceImpl extends ServiceImpl<ChildMapper, Child> implements ChildService {

    @Resource
    ChildMapper childMapper;

    @Override
    public List<ChildVO> childrenOfParent(String parentId) {
        List<Child> children = childMapper.selectList(new LambdaQueryWrapper<Child>().eq(Child::getParentId,parentId));
        if(null==children||children.size()==0){
            return null;
        }
        List<ChildVO> resultList = new ArrayList<>();
        for (Child child: children){
            ChildVO vo = new ChildVO();
            vo.setChildId(child.getId());
            vo.setNickname(child.getNickname());
            vo.setSchool(child.getSchool());
            vo.setGrade(child.getGrade());
            vo.setAge(child.getAge());
            vo.setGender(child.getGender());

            resultList.add(vo);
        }
        return resultList;
    }

    @Override
    public boolean childExist(String childId) {
        return childMapper.exists(new LambdaQueryWrapper<Child>().eq(Child::getId,childId));
    }

    @Override
    public String findParent(String childId) {
        Child child = childMapper.selectById(childId);
        if(null!=child){
            return child.getParentId();
        }
        return null;
    }
}
