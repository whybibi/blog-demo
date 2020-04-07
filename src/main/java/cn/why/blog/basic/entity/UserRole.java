package cn.why.blog.basic.entity;

import cn.why.blog.basic.entity.base.DataEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@TableName("USER_ROLE")
public class UserRole extends DataEntity<UserRole>{

    @NotNull
    private Long userId;
    @NotNull
    private Integer roleId;

    private Integer isEnable;

    public UserRole() {
    }

    public UserRole(@NotNull Long userId, @NotNull Integer roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}
