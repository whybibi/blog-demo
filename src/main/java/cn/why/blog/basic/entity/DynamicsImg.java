package cn.why.blog.basic.entity;

import cn.why.blog.basic.entity.base.DataEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("dynamics_img")
public class DynamicsImg extends DataEntity<DynamicsImg>{

    private Long dynamicsId;

    private String dynamicsImgUrl;

}
