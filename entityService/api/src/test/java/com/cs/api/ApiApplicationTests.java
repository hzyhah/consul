package com.cs.api;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class ApiApplicationTests {

    @Test
    void contextLoads() {
    }


    @Test
    void json() {

        List list = new ArrayList();
        Map map = new HashMap();
        map.put("date", "2020/04/25 21:19:07");

        Map map1 = new HashMap();
        map1.put("text","起床不");
        map.put("text",map1);
        map.put("mine",false);
        map.put("name","留恋人间不羡仙");
        map.put("img","/image/one.jpeg");

        list.add(map);

        System.out.println(JSON.toJSONString(list));
    }
}
