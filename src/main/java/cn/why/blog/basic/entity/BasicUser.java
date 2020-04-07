package cn.why.blog.basic.entity;

import cn.why.blog.basic.entity.base.DataEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@TableName("BASIC_USER")
@JsonInclude(value = JsonInclude.Include.NON_NULL)//后台返回json，数据为空的字段不显示
public class BasicUser extends DataEntity<BasicUser>{

    @NotNull
    @Length(min = 1,max = 25,message = "用户名必须大于一位小于25位")
    private String userName;
    @NotNull
    @Length(min = 6,max = 15,message = "密码必须大于等于6位小于15位")
    private String passWord;

    private String salt;

    private String birthday;

    private Integer sex;

    private String realName;

    private String phone;

    private String email;

    @Length(max = 50,message = "自我描述必须小于50字")
    private String description;

    private Integer isDelete;

    private Integer isEnable;

    private String headImg;

    @DateTimeFormat
    private Date LastLoginTime;

    @TableField(exist = false)
    private Role role;


}
