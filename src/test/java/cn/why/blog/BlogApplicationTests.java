package cn.why.blog;

import cn.why.blog.basic.entity.BasicUser;
import cn.why.blog.basic.entity.Name;
import cn.why.blog.basic.entity.base.GlobalPasswordVariable;
import cn.why.blog.basic.service.BasicUserService;
import cn.why.blog.basic.service.NameService;
import cn.why.blog.utils.PassWordUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogApplicationTests {
	@Autowired
	private NameService nameService;
	@Autowired
    private BasicUserService basicUserService;

	@Test
	public void contextLoads() {
	}

	@Test
    public void insertUser(){
        BasicUser user = new BasicUser();
        user.setUserName("123");
        user.setPassWord("123");
    }


    @Test
    public void pwd(){
        String salt =  PassWordUtils.generateSalt(GlobalPasswordVariable.SALT_NUM);
        String password = PassWordUtils.encryptPassword("MD5","123456",salt,1);
        System.out.println("得到的盐salt:"+salt);
        System.out.println("最终的密码password:"+password);
    }


}
