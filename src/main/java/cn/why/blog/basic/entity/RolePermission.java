package cn.why.blog.basic.entity;

import cn.why.blog.basic.entity.base.DataEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@TableName("ROLE_PERMISSION")
public class RolePermission extends DataEntity<RolePermission>{

    @NotNull
    private Integer roleId;
    @NotNull
    private Integer permissionId;

    private Integer isEnable;

    public RolePermission() {
    }

    public RolePermission(@NotNull Integer roleId, @NotNull Integer permissionId, Integer isEnable) {
        this.roleId = roleId;
        this.permissionId = permissionId;
        this.isEnable = isEnable;
    }
}
