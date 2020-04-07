package cn.why.blog.basic.entity;

import cn.why.blog.basic.entity.base.DataEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("dynamics")
public class Dynamics extends DataEntity<Dynamics>{

    private String dynamicsMsg;

    private Integer isDelete;

    private Integer isEnable;

}
