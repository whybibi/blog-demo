package cn.why.blog.dto;

import java.util.List;

/**
 * @version: V1.0
 * @auther: whybibi
 * @className: LayuiPage
 * @date: 2019-05-29 09:20
 * @description: 分页返回数据
 */
public class LayuiPage<T> {

    private Long count;

    private Integer code = 0;

    private String msg;

    private List<T> data;


    public LayuiPage(Long count, List<T> data) {
        this.count = count;
        this.data = data;
    }

    public LayuiPage(Long count, String msg, List<T> data) {
        this.count = count;
        this.msg = msg;
        this.data = data;
    }

    public LayuiPage(Long count, Integer code, String msg, List<T> data) {
        this.count = count;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
