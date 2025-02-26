package com.quwen.vo;

import java.util.ArrayList;
import java.util.List;

public class PageVO<T> {

    private int total;

    private int page;

    private int num;

    private int size;

    private int totalPage;

    private boolean checkFirst;

    private boolean checkLast;

    private List<T> list;

    public PageVO() {
        this.list = new ArrayList<>();
    }

    public void put(T item){
        this.list.add(item);
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public boolean isCheckFirst() {
        return checkFirst;
    }

    public void setCheckFirst(boolean checkFirst) {
        this.checkFirst = checkFirst;
    }

    public boolean isCheckLast() {
        return checkLast;
    }

    public void setCheckLast(boolean checkLast) {
        this.checkLast = checkLast;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
