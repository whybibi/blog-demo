package cn.why.blog.utils;

import com.google.code.kaptcha.Constants;

import javax.servlet.http.HttpServletRequest;

public class VerifyCheck {

    public static boolean checkVerify(HttpServletRequest request){
        String Scode = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        String Ucode = HttpServletRequestUtils.getString(request,"verifyCode");
        if(Ucode == null || !Scode.equalsIgnoreCase(Ucode)){
            return false;
        }
        return true;
    }

}
