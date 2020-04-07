package cn.why.blog.basic.service.impl;

import cn.why.blog.basic.entity.Dynamics;
import cn.why.blog.basic.entity.DynamicsImg;
import cn.why.blog.basic.entity.base.GlobalMessage;
import cn.why.blog.basic.entity.base.GlobalPasswordVariable;
import cn.why.blog.basic.entity.enums.SimpleError;
import cn.why.blog.basic.mapper.DynamicsMapper;
import cn.why.blog.basic.service.DynamicsImgService;
import cn.why.blog.basic.service.DynamicsService;
import cn.why.blog.config.shiro.ShiroUtils;
import cn.why.blog.dto.ImgHolder;
import cn.why.blog.utils.HttpServletRequestUtils;
import cn.why.blog.utils.ImageUtils;
import cn.why.blog.utils.PathUtils;
import com.baomidou.mybatisplus.extension.api.Assert;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class DynamicsServiceImpl extends ServiceImpl<DynamicsMapper,Dynamics> implements DynamicsService{

    @Autowired
    private DynamicsImgService dynamicsImgService;

    /**
     * 提交动态
     * @param request
     * @return
     */
    @Override
    @Transactional
    public R submitDynamics(HttpServletRequest request) {
        String text = HttpServletRequestUtils.getString(request,"text");
        Integer isEnable = HttpServletRequestUtils.getInt(request,"isEnable");
        if(text == null || "".equals(text))
            Assert.fail(SimpleError.DYNAMICS_INFO_EMPTY);
        Dynamics dynamics = new Dynamics();
        dynamics.setDynamicsMsg(text);
        dynamics.setIsEnable(isEnable);
        dynamics.setCreateDate(new Date());
        dynamics.setCreateUser(ShiroUtils.getCurrentUser().getId());
        if(!this.save(dynamics))
            Assert.fail(SimpleError.SAVE_ERROR);
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if(commonsMultipartResolver.isMultipart(request)){
            MultipartHttpServletRequest servletRequest = (MultipartHttpServletRequest) request;
            List<MultipartFile> imgs = (servletRequest.getFiles("img") != null ? servletRequest.getFiles("img") : null);
            if(imgs != null){
                try{
                    for(MultipartFile file : imgs){
                        ImgHolder imgHolder = new ImgHolder(file.getOriginalFilename(),file.getInputStream());
                        if(!saveImg(dynamics,imgHolder))
                            Assert.fail(SimpleError.SAVE_DYNAMICS_IMG_ERR);
                    }
                }catch(Exception e){
                    System.out.println(e.getMessage());
                    Assert.fail(SimpleError.SAVE_DYNAMICS_IMG_ERR);
                }
            }
        }
        return R.ok(GlobalMessage.TIP_SAVE_SUCCESS);
    }

    /*保存图片到硬盘*/
    private Boolean saveImg(Dynamics dynamics, ImgHolder imgHolder){
        DynamicsImg dynamicsImg = new DynamicsImg();
        dynamicsImg.setDynamicsId(dynamics.getId());
        dynamicsImg.setCreateUser(dynamics.getCreateUser());
        dynamicsImg.setDynamicsImgUrl(ImageUtils.generateThumbnail(imgHolder,PathUtils.getShareImgPath(dynamics.getCreateUser(), dynamics.getId()),false));
        return dynamicsImgService.save(dynamicsImg);
    }


    /*获取最新的前十条数据*/
    @Override
    public List<Dynamics> getDynamicsTopTen() {
        return baseMapper.getDynamicsTopTen();
    }


}
