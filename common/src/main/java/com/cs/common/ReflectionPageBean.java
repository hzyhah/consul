package com.cs.common;

import java.lang.reflect.Field;
import java.util.List;

public class ReflectionPageBean {

    public static PageBean init(Object obj) throws IllegalAccessException {
        Class<?> objClass = obj.getClass();
        Field[] fileds = objClass.getDeclaredFields();
        PageBean pageBean = new PageBean();
        for (int i = 0; i < fileds.length; i++) {
            Field filed = fileds[i];
            if (filed.getName().equals("total")){
                pageBean.setAllRow(filed.getLong(obj));
            }

            if (filed.getName().equals("current")){
                pageBean.setCurrentPage(filed.getLong(obj));
            }

            if (filed.getName().equals("size")){
                pageBean.setPageSize(filed.getLong(obj));
            }

            if (filed.getName().equals("records")){
                pageBean.setList((List) filed.get(obj));
            }

        }
        return pageBean;
    }

}
