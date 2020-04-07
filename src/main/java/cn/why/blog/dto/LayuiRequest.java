package cn.why.blog.dto;

import lombok.Data;

import java.util.Map;

@Data
public class LayuiRequest {

    /*当前页数*/
    private Integer page;

    /*每页现实的条数，前台默认获取10条*/
    private Integer limit;

    /*前台传入的搜索项*/
    private Map<String,Object> params;

    /*前台传入的单条参数*/
    private String parameter;



}
