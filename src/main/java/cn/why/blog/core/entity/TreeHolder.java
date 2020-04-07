package cn.why.blog.core.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @version: V1.0
 * @auther: whybibi
 * @className: TreeHolder
 * @date: 2019-06-10 16:56
 * @description: layui树形参数返回类
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TreeHolder {

    /*权限标题*/
    private String title;

    /*权限ID*/
    private Long id;

    /*是否展开*/
    private Boolean spread;

    /*子节点*/
    private List<TreeHolder> children;

    public TreeHolder() {
    }


    public TreeHolder(String title,Boolean spread, List<TreeHolder> children) {
        this.title = title;
        this.spread = spread;
        this.children = children;
    }

    public TreeHolder(String title, Long id) {
        this.title = title;
        this.id = id;
    }

    public TreeHolder(String title, Long id, Boolean spread, List<TreeHolder> children) {
        this.title = title;
        this.id = id;
        this.spread = spread;
        this.children = children;
    }
}
