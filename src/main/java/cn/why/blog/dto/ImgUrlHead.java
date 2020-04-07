package cn.why.blog.dto;


import cn.why.blog.utils.ImageUtils;
import cn.why.blog.utils.PathUtils;

/**
 * 图片开头虚拟路径
 */
public class ImgUrlHead {

    public static String url(){
        return PathUtils.separator+"image"+PathUtils.separator;
    }

}
