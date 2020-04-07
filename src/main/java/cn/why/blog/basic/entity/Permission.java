package cn.why.blog.basic.entity;

import cn.why.blog.basic.entity.base.DataEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@TableName("PERMISSION")
public class Permission extends DataEntity<Permission>{

    @NotNull
    private String permissionCode;
    @NotNull
    private String permissionName;

    private Integer isSystem;

    private Integer isEnable;

    private String description;


}
