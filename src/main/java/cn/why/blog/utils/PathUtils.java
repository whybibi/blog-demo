package cn.why.blog.utils;

import java.util.Properties;

public class PathUtils {

    public static String separator = System.getProperty("file.separator");//系统分隔符，防止windows或linux下分隔符不同

    //获取基本地址
    public static String getImgBasePath(){
        Properties props = System.getProperties();
        String os = props.getProperty("os.name");
        String basePath = "";
        if(os.toLowerCase().startsWith("win")){
            basePath = "D:/ProjectUploadImg/blog/image/";
        }else{
            basePath = "/home/ProjectUploadImg/blog/image/";
        }
        return basePath.replace("/",separator);
    }

    //用户头像存放地址
    public static String getUserHeadImgPath(Long userId){
        return ("user/"+userId+"/").replace("/",separator);
    }

    //文章图片存放地址
    public static String getShareImgPath(Long userId, Long id){
        return ("user/"+userId+"/share/"+id+"/").replace("/",separator);
    }

}
