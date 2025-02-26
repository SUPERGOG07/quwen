package com.quwen.entity.test;

import com.quwen.entity.base.BaseEntity;
import com.quwen.entity.business.Child;

import javax.validation.constraints.NotBlank;

public class Test extends BaseEntity<Test> {

    private static final long serialVersionUID = 1L;

    @NotBlank
    private String test;

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

        sb.append(", test=").append(test);

        sb.append("]");
        return sb.toString();
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }


}
