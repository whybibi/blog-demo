package cn.why.blog.core.controller;

import cn.why.blog.basic.entity.BasicUser;
import cn.why.blog.basic.entity.UserRole;
import cn.why.blog.basic.entity.base.GlobalMessage;
import cn.why.blog.basic.entity.enums.IsDeleteEnum;
import cn.why.blog.basic.entity.enums.SimpleError;
import cn.why.blog.basic.service.BasicUserService;
import cn.why.blog.basic.service.UserRoleService;
import cn.why.blog.config.shiro.ShiroUtils;
import cn.why.blog.core.service.UserManagerService;
import cn.why.blog.dto.ImgUrlHead;
import cn.why.blog.dto.LayuiPage;
import cn.why.blog.dto.LayuiRequest;
import cn.why.blog.utils.PathUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.Assert;
import com.baomidou.mybatisplus.extension.api.R;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户后台管理
 */

@RequestMapping("admin")
@Controller
public class UserManageController {

    @Autowired
    private BasicUserService basicUserService;
    @Autowired
    private UserManagerService userManagerService;
    @Autowired
    private UserRoleService userRoleService;

    /**
     * 分页查询用户
     * @return LayuiPage
     */
    @GetMapping("user_list")
    @RequiresPermissions("user:view")
    @ResponseBody
    public LayuiPage getUserMap(LayuiRequest layuiRequest){
        PageHelper.startPage(layuiRequest.getPage() <= 0 ? 1 : layuiRequest.getPage(),layuiRequest.getLimit());
        List<BasicUser> list = basicUserService.list(/*new QueryWrapper<BasicUser>().lambda().eq(BasicUser::getIsDelete, IsDeleteEnum.ONE.getValue())*/);
        if(list != null && list.size() > 0){
            PageInfo<BasicUser> pageInfo = new PageInfo<>(list);
            for(BasicUser user : list){
                if(user.getHeadImg() != null && user.getHeadImg().indexOf(ImgUrlHead.url()) == -1)
                    user.setHeadImg(ImgUrlHead.url() + user.getHeadImg());
            }
            return new LayuiPage<BasicUser>(pageInfo.getTotal(),pageInfo.getList());
        }else{
            return new LayuiPage<BasicUser>(0l,list);
        }
    }

 /*   public Map getUserMap(@RequestParam(value = "pageNum", defaultValue = "1", required = false) int pageNum,@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize){
        Map<String,Object> map = new HashMap<>();
        PageHelper.startPage(pageNum <= 0 ? 1 : pageNum,pageSize);
        List<BasicUser> list = basicUserService.list(new QueryWrapper<BasicUser>().lambda().eq(BasicUser::getIsDelete, IsDeleteEnum.ONE.getValue()));
        if(list != null && list.size() > 0){
            PageInfo<BasicUser> pageInfo = new PageInfo<>(list);
            for(BasicUser user : list){
                if(user.getHeadImg() != null && user.getHeadImg().indexOf(ImgUrlHead.url()) == -1)
                    user.setHeadImg(ImgUrlHead.url() + user.getHeadImg());
            }
            map.put("success",true);
            map.put("pageInfo",pageInfo);
        }else{
            map.put("success",false);
        }
        return map;
    }
*/


    /**
     *更新用户信息
     * @param user
     * @return
     */
    @PostMapping("update_user")
    @ResponseBody
    public R updateUserById(BasicUser user){
        if(userManagerService.updateUserById(user))
            return R.ok(GlobalMessage.TIP_SAVE_SUCCESS);
        return R.failed(SimpleError.DELETE_ERROR);
    }


    /**
     * 更新用户状态（是否启用）
     * @param user
     * @return R
     */
    @PostMapping("update_user_status")
    @ResponseBody
    public R updateUserStatusById(BasicUser user){
        if(userManagerService.updateUserById(user))
            return R.ok(GlobalMessage.TIP_SAVE_SUCCESS);
        return R.failed(SimpleError.DELETE_ERROR);
    }


    /**
     * 用户批量删除
     * @param id 用户id数组
     * @return
     */
    @PostMapping("del_user")
    @ResponseBody
    public R delUserById(Long[] id){
        if(id != null && id.length > 0){
            for(int i = 0; i < id.length; i++){
                R r = basicUserService.deleteUserById(id[i]);
                if(!r.ok())
                    return r;
                if(!userRoleService.remove(new QueryWrapper<UserRole>().lambda().eq(UserRole::getUserId,id[i])))
                    return R.failed(SimpleError.DELETE_ERROR);
            }
            return R.ok(GlobalMessage.TIP_DELETE_SUCCESS);
        }
        return R.failed(SimpleError.DELETE_ERROR);
    }

    /**
     * 通过id查询用户
     * @param id
     * @return
     */
    @PostMapping("get_user_by_id")
    @ResponseBody
    public Map getUserById(Long id){
        Map<String,Object> map = new HashMap<>();
        if (id != null){
            BasicUser user = basicUserService.getBasicUserById(id);
            map.put("success",true);
            map.put("user",user);
        }else{
            map.put("success",false);
        }
        return map;
    }


    /**
     * 重置用户密码为123456
     * @param ids 用户id数组，允许多条数据同时提交
     * @return R
     */
    @PostMapping("recover_pwd")
    @ResponseBody
    public R recoverPwd(Long[] ids){
        if(ids != null && ids.length > 0){
            for(int i = 0; i < ids.length; i++){
                if(!userManagerService.recoverPassWord(ids[i]))
                    return R.failed(SimpleError.PASSWORD_RECOVER_FAIL);
            }
            return R.ok(GlobalMessage.TIP_RECOVER_SUCCESS);
        }
        return R.failed(SimpleError.NULL_POINT_ERROR);
    }


    /**
     * 取消删除用户
     * @param id 用户id
     * @return R
     */
    @PostMapping("recover_user")
    @ResponseBody
    public R recoverUser(Long id){
        if(id != null){
            BasicUser user = new BasicUser();
            user.setId(id);
            user.setIsDelete(IsDeleteEnum.ONE.getValue());
            if(!userManagerService.updateUserById(user))
                return R.failed(SimpleError.OPERATION_ERROR);
            return R.ok(GlobalMessage.TIP_RECOVER_SUCCESS);
        }
        return R.failed(SimpleError.NULL_POINT_ERROR);
    }



}
