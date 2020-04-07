package cn.why.blog.basic.controller;

import cn.why.blog.basic.entity.BasicUser;
import cn.why.blog.basic.entity.Dynamics;
import cn.why.blog.basic.entity.DynamicsImg;
import cn.why.blog.basic.entity.base.GlobalMessage;
import cn.why.blog.basic.entity.enums.SimpleError;
import cn.why.blog.basic.service.BasicUserService;
import cn.why.blog.basic.service.DynamicsImgService;
import cn.why.blog.basic.service.DynamicsService;
import cn.why.blog.config.shiro.ShiroUtils;
import cn.why.blog.dto.DynamicsHolder;
import cn.why.blog.dto.ImgUrlHead;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("dynamics")
public class DynamicsController {

    @Autowired
    private DynamicsService dynamicsService;
    @Autowired
    private DynamicsImgService dynamicsImgService;
    @Autowired
    private BasicUserService userService;

    /**
     * 提交动态
     * @param request 前端传入的请求
     * @return R
     */
    @PostMapping("submit")
    @ResponseBody
    public R submitActiveInfo(HttpServletRequest request){
        return dynamicsService.submitDynamics(request);
    }


    /*老版本加载前十条动态，已废弃*/
    @PostMapping("load")
    @ResponseBody
    public Map loadDynamics(){
        Map<String,Object> map = new HashMap<>();
        try{
            List<DynamicsHolder> dynamicsHolderList = new ArrayList<>();
            List<Dynamics> dynamicsList = dynamicsService.getDynamicsTopTen();
            for(Dynamics dynamics : dynamicsList){
                List<DynamicsImg> dynamicsImgList = dynamicsImgService.list(new QueryWrapper<DynamicsImg>().lambda().eq(DynamicsImg::getDynamicsId,dynamics.getId()));
                if(dynamicsImgList != null && dynamicsImgList.size() > 0) {
                    for (DynamicsImg img : dynamicsImgList) {
                        if (img.getDynamicsImgUrl() != null && img.getDynamicsImgUrl().indexOf(ImgUrlHead.url()) == -1)
                            img.setDynamicsImgUrl(ImgUrlHead.url() + img.getDynamicsImgUrl());
                    }
                }
                BasicUser user = new BasicUser();
                user.setId(dynamics.getCreateBasicUser().getId());
                user.setUserName(dynamics.getCreateBasicUser().getUserName());
                user.setHeadImg(ImgUrlHead.url()+dynamics.getCreateBasicUser().getHeadImg());
                dynamics.setCreateBasicUser(null);
                dynamicsHolderList.add(new DynamicsHolder(dynamics,dynamicsImgList,user));
            }
            map.put("success",true);
            map.put("dynamicsList",dynamicsHolderList);
        }catch(Exception e){
            System.out.println(e);
            map.put("success",false);
        }
        return map;
    }


    /**
     *移动端主页流加载请求接口
     * @param page 当前页数
     * @return
     */
    @GetMapping("flow")
    @ResponseBody
    public Map loadDynamicsFlow(@RequestParam(value = "page",defaultValue = "1") Integer page){
        Map<String,Object> map = new HashMap<>();
        try{
            List<DynamicsHolder> dynamicsHolderList = new ArrayList<>();
            PageHelper.startPage(page,10);
            List<Dynamics> dynamicsList = dynamicsService.list(new QueryWrapper<Dynamics>().lambda().orderByDesc(Dynamics::getCreateDate));
            PageInfo<Dynamics> pageInfo = new PageInfo<>(dynamicsList);
            for(Dynamics dynamics : dynamicsList){
                List<DynamicsImg> dynamicsImgList = dynamicsImgService.list(new QueryWrapper<DynamicsImg>().lambda().eq(DynamicsImg::getDynamicsId,dynamics.getId()));
                if(dynamicsImgList != null && dynamicsImgList.size() > 0) {
                    for (DynamicsImg img : dynamicsImgList) {
                        if (img.getDynamicsImgUrl() != null && img.getDynamicsImgUrl().indexOf(ImgUrlHead.url()) == -1)
                            img.setDynamicsImgUrl(ImgUrlHead.url() + img.getDynamicsImgUrl());
                    }
                }
                BasicUser u = userService.getById(dynamics.getCreateUser());
                BasicUser user = new BasicUser();
                user.setId(u.getId());
                user.setUserName(u.getUserName());
                user.setHeadImg(ImgUrlHead.url()+u.getHeadImg());
                dynamicsHolderList.add(new DynamicsHolder(dynamics,dynamicsImgList,user));
            }
            map.put("pages",pageInfo.getPages());
            map.put("data",dynamicsHolderList);
        }catch(Exception e){
            System.out.println(e);
            map.put("pages",0);
        }
        return map;
    }


    /**
     * 修改文章可见状态
     * @param dynamics
     * @return
     */
    @PostMapping("update_dynamics_status")
    @ResponseBody
    public R changeIsEnable(Dynamics dynamics){
        dynamics.setUpdateDate(new Date());
        dynamics.setUpdateUser(ShiroUtils.getCurrentUser().getId());
        if(dynamicsService.updateById(dynamics))
            return R.ok(GlobalMessage.TIP_SAVE_SUCCESS);
        return R.failed(SimpleError.ERROR);
    }




}
