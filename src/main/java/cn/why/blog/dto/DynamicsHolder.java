package cn.why.blog.dto;

import cn.why.blog.basic.entity.BasicUser;
import cn.why.blog.basic.entity.Dynamics;
import cn.why.blog.basic.entity.DynamicsImg;
import lombok.Data;

import java.util.List;

@Data
public class DynamicsHolder {

    private Dynamics dynamics;

    private List<DynamicsImg> dynamicsImgList;

    private BasicUser user;

    public DynamicsHolder() {
    }

    public DynamicsHolder(Dynamics dynamics, List<DynamicsImg> dynamicsImgList, BasicUser user) {
        this.dynamics = dynamics;
        this.dynamicsImgList = dynamicsImgList;
        this.user = user;
    }
}
