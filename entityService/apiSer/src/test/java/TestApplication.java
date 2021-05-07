import com.cs.apiSer.ApiSerApplication;
import com.cs.apiSer.service.IRoleService;
import com.cs.apiSer.service.IUserService;
import com.cs.apiSer.vo.User;
import com.cs.common.MD5Util;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiSerApplication.class)
public class TestApplication {

    @Test
    public  void macb() {
        int MAXIMUM_CAPACITY = 1 << 30;
        System.out.println(MAXIMUM_CAPACITY);

        int a = 30;
        int b = 2;
        System.out.println(a&b);

    }
    @Test
    public void snowflake(){
        long a= 1;
        long b = 2;
        System.out.println(Integer.toBinaryString(1));
        System.out.println(Integer.toBinaryString(1<<6));
        System.out.println(1<<6);
    }

    @Autowired
    IUserService userService;
    @Autowired
    IRoleService roleService;

    @Test
    public void addusr(){
        User user = new User();
        user.setAccount("user");
        user.setName("obj");
        user.setPassword(MD5Util.getMd5("123456"));
        user.setSex(1);
        userService.save(user);
    }
}
