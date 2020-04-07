package cn.why.blog.utils;

import cn.why.blog.dto.ImgHolder;
import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ImageUtils {

    private static final SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random random = new Random();

    /**
     *
     * @param imgHolder 图片名和图片流寄存类
     * @param absAttr 绝对地址
     * @param isScale 是否压缩
     * @return
     */
    public static String generateThumbnail(ImgHolder imgHolder, String absAttr, Boolean isScale){
        String fileName = getRandomFileName();
        String extension = getFileExtension(imgHolder.getImgName());
        makeDirPath(absAttr);//判断有无文件夹，无就创建
        String realAttr = absAttr+fileName+extension;
        File desk = new File(PathUtils.getImgBasePath()+realAttr);
        if(isScale){
            try{
                /*
                outputQuality(0.8f)控制图片压缩质量
                outputFormat("png")图片格式转换
                */
                Thumbnails.of(imgHolder.getImg()).size(200,200)
                        .outputQuality(0.8f).toFile(desk);
            }catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }else{
            try{
                /*scale(1)缩放比例，大于1就是变大，小于1就是缩小*/
                Thumbnails.of(imgHolder.getImg()).scale(1).toFile(desk);
            }catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }
        return  realAttr;
    }


    /**
     * 获取输入文件名的拓展名
     * @param fileName
     * @return获取到的后缀名
     */
    private static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 随机生成文件名，当前年月日小时分钟秒数+5位随机数
     * @return 日期与随机数相加的字符串
     */
    private static String getRandomFileName(){
        int randNum = random.nextInt(89999)+10000;
        String time = dataFormat.format(new Date());
        return time+randNum;
    }

    /**
     * 创建目标路径所涉及到的目录
     * 如果为空就自动创建
     * @param targetAddr
     */
    private static void makeDirPath(String targetAddr) {
        String realParh = PathUtils.getImgBasePath()+targetAddr;
        File dir = new File(realParh);
        if(!dir.exists()){
            dir.mkdirs();
        }
    }


    /**
     * 先判断path是文件路径还是目录路径
     * 文件路径就删除文件
     * 目录路径就删除该目录下的所有文件
     * @param path
     */
    public static void deleteFileOrPath(String path){
        File fileOrPath = new File(PathUtils.getImgBasePath()+path);
        if(fileOrPath.exists()){
            if(fileOrPath.isDirectory()){
                File file[] = fileOrPath.listFiles();
                for(int i = 0; i < file.length; i++){
                    file[i].delete();
                }
            }
            fileOrPath.delete();
        }
    }


}
