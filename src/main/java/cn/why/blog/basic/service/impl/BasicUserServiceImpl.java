package cn.why.blog.basic.service.impl;

import cn.why.blog.basic.entity.BasicUser;
import cn.why.blog.basic.entity.UserRole;
import cn.why.blog.basic.entity.base.GlobalMessage;
import cn.why.blog.basic.entity.base.GlobalPasswordVariable;
import cn.why.blog.basic.entity.enums.IsDeleteEnum;
import cn.why.blog.basic.entity.enums.SimpleError;
import cn.why.blog.basic.mapper.BasicUserMapper;
import cn.why.blog.basic.service.BasicUserService;
import cn.why.blog.basic.service.UserRoleService;
import cn.why.blog.dto.ImgHolder;
import cn.why.blog.utils.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.Assert;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class BasicUserServiceImpl extends ServiceImpl<BasicUserMapper,BasicUser> implements BasicUserService{

    @Autowired
    private UserRoleService userRoleService;


    /**
     * 通过用户id查询用户
     * @param id 用户id
     * @return 用户实体
     */
    @Override
    public BasicUser getBasicUserById(Long id) {
        BasicUser user = baseMapper.selectById(id);
        user.setPassWord("");
        user.setSalt("");
        return user;
    }

    /**
     * 通过用户名查询
     * @param name 用户名
     * @return 用户实体
     */
    @Override
    public BasicUser getBasicUserByName(String name) {
        return baseMapper.selectOne(new QueryWrapper<BasicUser>().lambda().eq(BasicUser::getUserName,name.trim()));
    }

    /**
     * 用户注册模块
     * @param request
     * @return
     */
    @Override
    @Transactional
    public R registerUser(HttpServletRequest request) {
        //验证模块
        if(!VerifyCheck.checkVerify(request))
            return R.failed(SimpleError.VERIFY_ERR);
        BasicUser user =  null;
        try{
           user = (BasicUser) JSONObject.toBean(JSONObject.fromObject(HttpServletRequestUtils.getString(request,"userStr")),BasicUser.class);
        }catch(Exception e){Assert.fail(SimpleError.JSON_TO_BEAN_ERR);}
        if(user != null){
            //验证数据库中是否有相同用户名
            BasicUser basicUser = baseMapper.selectOne(new QueryWrapper<BasicUser>().lambda().eq(BasicUser::getUserName,user.getUserName()));
            if(basicUser != null){
                return R.failed(SimpleError.USER_IS_EXIST);
            }
            //数据库添加模块
            String salt = PassWordUtils.generateSalt(GlobalPasswordVariable.SALT_NUM);
            String password = PassWordUtils.encryptPassword(GlobalPasswordVariable.ENCRYPTION_TYPE, user.getPassWord(), salt, GlobalPasswordVariable.ENCRYPTION_NUM);
            user.setSalt(salt);
            user.setPassWord(password);
            user.setCreateDate(new Date());
            if(baseMapper.insert(user) != 1)
                Assert.fail(SimpleError.SAVE_ERROR);
            //图片上传判断
                CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
            if(commonsMultipartResolver.isMultipart(request)){
                MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
                MultipartFile headImg = (multipartHttpServletRequest.getFiles("headImg") != null ? multipartHttpServletRequest.getFile("headImg") : null);
                if(headImg != null){
                    try{
                        ImgHolder imgHolder = new ImgHolder(headImg.getOriginalFilename(),headImg.getInputStream());
                        user.setHeadImg(setHeadImg(user,imgHolder));//给实体类设置头像地址并把头像上传到服务器
                    }catch(Exception e){Assert.fail(SimpleError.SAVE_HEAD_IMG_ERR);}
                }else {
                    user.setHeadImg("default"+PathUtils.separator+"default.gif");//如果用户没有上传头像，就给默认头像
                }
            }
            return baseMapper.updateById(user) == 1 ? R.ok(GlobalMessage.TIP_SAVE_SUCCESS) : R.failed(SimpleError.SAVE_ERROR);
        }
        return R.failed(SimpleError.ERROR);
    }

    /*逻辑删除用户*/
    @Override
    @Transactional
    public R deleteUserById(Long id) {
        if(userRoleService.getOne(new QueryWrapper<UserRole>().lambda().eq(UserRole::getUserId,id)) != null){
            return R.failed(SimpleError.DELETE_BACK);
        }
        BasicUser user =  new BasicUser();
        user.setId(id);
        user.setIsDelete(IsDeleteEnum.ZERO.getValue());
        if(baseMapper.updateById(user) == 1){
            return R.ok(GlobalMessage.TIP_DELETE_SUCCESS);
        }
        return R.failed(SimpleError.DELETE_ERROR);
    }

    //保存头像图片
    private String setHeadImg(BasicUser user, ImgHolder imgHolder){
        String absPath = PathUtils.getUserHeadImgPath(user.getId());
        String realPath = ImageUtils.generateThumbnail(imgHolder,absPath,true);
        return realPath;
    }





}
