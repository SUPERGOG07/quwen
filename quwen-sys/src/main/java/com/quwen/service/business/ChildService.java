package com.quwen.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quwen.entity.business.Child;
import com.quwen.vo.ChildVO;

import java.util.List;

public interface ChildService extends IService<Child> {

    List<ChildVO> childrenOfParent(String parentId);

    boolean childExist(String childId);

    String findParent(String childId);
}
