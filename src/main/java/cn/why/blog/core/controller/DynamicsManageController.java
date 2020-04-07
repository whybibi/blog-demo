package cn.why.blog.core.controller;

import cn.why.blog.basic.entity.BasicUser;
import cn.why.blog.basic.entity.Dynamics;
import cn.why.blog.basic.entity.DynamicsImg;
import cn.why.blog.basic.entity.base.GlobalMessage;
import cn.why.blog.basic.entity.enums.IsDeleteEnum;
import cn.why.blog.basic.entity.enums.SimpleError;
import cn.why.blog.basic.service.BasicUserService;
import cn.why.blog.basic.service.DynamicsImgService;
import cn.why.blog.basic.service.DynamicsService;
import cn.why.blog.config.shiro.ShiroUtils;
import cn.why.blog.dto.DynamicsHolder;
import cn.why.blog.dto.ImgUrlHead;
import cn.why.blog.dto.LayuiPage;
import cn.why.blog.dto.LayuiRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 用户动态后台管理
 */
@RequestMapping("admin")
@Controller
public class DynamicsManageController {

    @Autowired
    private DynamicsService dynamicsService;
    @Autowired
    private DynamicsImgService dynamicsImgService;
    @Autowired
    private BasicUserService basicUserService;

    /*分页获取所有动态*/
    @GetMapping("dynamics_list")
    @RequiresPermissions("dynamics:view")
    @ResponseBody
    public LayuiPage<DynamicsHolder> dynamicsList(LayuiRequest layuiRequest){
        PageHelper.startPage(layuiRequest.getPage() <= 0 ? 1 : layuiRequest.getPage(),layuiRequest.getLimit());
        List<Dynamics> list = dynamicsService.list(new QueryWrapper<Dynamics>().lambda()
                /*.eq(Dynamics::getIsDelete, IsDeleteEnum.ONE.getValue())*/
                .orderByDesc(Dynamics::getCreateDate));
        if(list != null && list.size() > 0){
            List<DynamicsHolder> holderList = new ArrayList<>();
            PageInfo<Dynamics> pageInfo = new PageInfo<>(list);
            for(Dynamics dynamics : list){
                List<DynamicsImg> imgList = dynamicsImgService.list(new QueryWrapper<DynamicsImg>().lambda().eq(DynamicsImg::getDynamicsId,dynamics.getId()));
                BasicUser user = basicUserService.getById(dynamics.getCreateUser());
                BasicUser u = new BasicUser();
                u.setUserName(user.getUserName());
                if(imgList != null && imgList.size() > 0){
                    for(DynamicsImg img : imgList){
                        if(img.getDynamicsImgUrl() != null && img.getDynamicsImgUrl().indexOf(ImgUrlHead.url()) == -1)
                            img.setDynamicsImgUrl(ImgUrlHead.url() + img.getDynamicsImgUrl());
                    }
                }
                holderList.add(new DynamicsHolder(dynamics,imgList,u));
            }
            PageInfo<DynamicsHolder> pageHolder = new PageInfo<>(holderList);
            return new LayuiPage<DynamicsHolder>(pageInfo.getTotal(),pageHolder.getList());
        }else{
            return new LayuiPage<DynamicsHolder>(0l,-1,null,null);
        }
    }

    /*public Map dynamicsList(@RequestParam(value = "pageNum", defaultValue = "1", required = false) int pageNum, @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize){
        Map<String,Object> map = new HashMap<>();
        PageHelper.startPage(pageNum <= 0 ? 1 : pageNum,pageSize);
        List<Dynamics> list = dynamicsService.list(new QueryWrapper<Dynamics>().lambda().eq(Dynamics::getIsDelete, IsDeleteEnum.ONE.getValue()));
        if(list != null && list.size() > 0){
            List<DynamicsHolder> holderList = new ArrayList<>();
            PageInfo<Dynamics> pageInfo = new PageInfo<>(list);
            for(Dynamics dynamics : list){
                List<DynamicsImg> imgList = dynamicsImgService.list(new QueryWrapper<DynamicsImg>().lambda().eq(DynamicsImg::getDynamicsId,dynamics.getId()));
                BasicUser user = basicUserService.getById(dynamics.getCreateUser());
                BasicUser u = new BasicUser();
                u.setUserName(user.getUserName());
                if(imgList != null && imgList.size() > 0){
                    for(DynamicsImg img : imgList){
                        if(img.getDynamicsImgUrl() != null && img.getDynamicsImgUrl().indexOf(ImgUrlHead.url()) == -1)
                            img.setDynamicsImgUrl(ImgUrlHead.url() + img.getDynamicsImgUrl());
                    }
                }
                holderList.add(new DynamicsHolder(dynamics,imgList,u));
            }
            PageInfo<DynamicsHolder> pageHolder = new PageInfo<>(holderList);
            map.put("success",true);
            map.put("pageInfo",pageHolder);
        }else{
            map.put("success",false);
        }
        return map;
    }*/


    /**
     * 逻辑删除动态文章
     * @param id 文章id
     * @return
     */
    @PostMapping("delete")
    @RequiresPermissions("dynamics:delete")
    @ResponseBody
    public R delDynamic(Long id){
        Dynamics dynamics = new Dynamics();
        dynamics.setId(id);
        dynamics.setIsDelete(IsDeleteEnum.ZERO.getValue());
        dynamics.setUpdateDate(new Date());
        dynamics.setUpdateUser(ShiroUtils.getCurrentUser().getId());
       if(dynamicsService.updateById(dynamics))
           return R.ok(GlobalMessage.TIP_DELETE_SUCCESS);
       return R.failed(SimpleError.ERROR);
    }


    /**
     * 逻辑恢复删除的动态文章
     * @param id 文章id
     * @return
     */
    @PostMapping("recover")
    @RequiresPermissions("dynamics:recover")
    @ResponseBody
    public R recoverDynamic(Long id){
        Dynamics dynamics = new Dynamics();
        dynamics.setId(id);
        dynamics.setIsDelete(IsDeleteEnum.ONE.getValue());
        dynamics.setUpdateDate(new Date());
        dynamics.setUpdateUser(ShiroUtils.getCurrentUser().getId());
       if(dynamicsService.updateById(dynamics))
           return R.ok(GlobalMessage.TIP_RECOVER_SUCCESS);
       return R.failed(SimpleError.ERROR);
    }




}
