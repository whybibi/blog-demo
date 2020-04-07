package cn.why.blog.utils;

import cn.why.blog.basic.entity.base.GlobalPasswordVariable;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;

public class PassWordUtils {


    /**
     * 生成salt
     * @param length
     * @return
     */
    public static String generateSalt(int length){
        int byteLength = length >>  1;
        SecureRandomNumberGenerator random = new SecureRandomNumberGenerator();
        return random.nextBytes(byteLength).toHex();
    }


    /**
     * 获取加密后的密码，需要指定 hash迭代的次数
     * @param hashAlgorithm  hash算法名称 MD2、MD5、SHA-1、SHA-256、SHA-384、SHA-512、etc。
     * @param password       需要加密的密码
     * @param salt           盐
     * @param hashIterations hash迭代的次数
     * @return 加密后的密码
     */
    public static String encryptPassword(String hashAlgorithm, String password, String salt, int hashIterations) {
        SimpleHash hash = new SimpleHash(hashAlgorithm, password, salt, hashIterations);
        return hash.toString();
    }


}
