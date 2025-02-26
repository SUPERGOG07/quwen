package com.quwen.entity.business;

import com.quwen.entity.base.BaseEntity;

import javax.validation.constraints.NotBlank;

/**
 * 子女儿童类
 */
public class Child extends BaseEntity<Child> {

    private static final long serialVersionUID = 1L;

    /**
     * 家长标识码
     */
    @NotBlank
    private String parentId;

    /**
     * 昵称
     */
    @NotBlank
    private String nickname;

    /**
     * 学校
     */

    private String school;

    /**
     * 年级
     */
    private String grade;

    /**
     * 年龄
     */
    private int age;

    /**
     * 性别
     */
    private String gender;

    public Child build(){
        Child child = new Child();
        child.setSchool("unknown");
        child.setGrade("unknown");
        child.setAge(0);
        child.setGender("unknown");
        return child;
    }

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

        sb.append(", parentId=").append(parentId);
        sb.append(", nickname=").append(nickname);
        sb.append(", school=").append(school);
        sb.append(", grade=").append(grade);
        sb.append(", age=").append(age);
        sb.append(", gender=").append(gender);

        sb.append("]");
        return sb.toString();
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
