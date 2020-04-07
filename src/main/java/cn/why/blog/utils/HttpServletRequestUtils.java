package cn.why.blog.utils;

import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestUtils {


    public static int getInt(HttpServletRequest request, String key){
        try{
            return Integer.parseInt(request.getParameter(key));
        }catch (Exception e){
            return -1;
        }
    }


    public static long getLong(HttpServletRequest request,String key){
        try{
            return Long.valueOf(request.getParameter(key));
        }catch(Exception e){
            return -1;
        }
    }

    public static Double getDouble(HttpServletRequest request,String key){
        try{
            return Double.valueOf(request.getParameter(key));
        }catch(Exception e){
            return -1d;
        }
    }

    public static boolean getBoolean(HttpServletRequest request,String key){
        try{
            return Boolean.valueOf(request.getParameter(key));
        }catch(Exception e){
            return false;
        }
    }


    public static String getString(HttpServletRequest request,String key){
        try{
            String result = request.getParameter(key);
            if(result != null){
                result = result.trim();
            }
            if("".equals(result)){
                result = null;
            }
            return result;
        }catch(Exception e){
            return null;
        }
    }


}
