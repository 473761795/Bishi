package com.example.mianshi.response;

import lombok.Data;

import java.util.List;
/**
 * 分页结果类
 * @param <T>
 */
@Data
public class PageResult<T> {
    private String title;
    private List<T> items;

    public PageResult(String title, List<T> items) {
        this.title = title;
        this.items = items;
    }

    public PageResult() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}