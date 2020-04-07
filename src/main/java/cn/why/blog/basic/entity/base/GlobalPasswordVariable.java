package cn.why.blog.basic.entity.base;

public class GlobalPasswordVariable {
    /**
     * 初始化密码加密方式
     */
    public static String ENCRYPTION_TYPE = "MD5";
    /**
     * 加密次数
     */
    public static int ENCRYPTION_NUM = 1;
    /**
     * salt盐的位数
     */
    public static int SALT_NUM = 25;
}
