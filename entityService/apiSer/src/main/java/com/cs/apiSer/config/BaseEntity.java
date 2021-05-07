package com.cs.apiSer.config;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

public interface BaseEntity {
    /**
     * 主键
     */
    @TableId(type = IdType.ID_WORKER) //mybatis-plus主键注解
    @IsKey                         //actable主键注解
    //@IsAutoIncrement             //自增
    @Column 					 //对应数据库字段，不配置name会直接采用属性名作为字段名
    long id =0;
    /**
     * 创建时间
     */
    @TableField(value = "addTime",fill = FieldFill.INSERT)  // name指定数据库字段名，comment为备注
     Date addTime = null;

}
