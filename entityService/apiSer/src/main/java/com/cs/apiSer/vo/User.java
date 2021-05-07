package com.cs.apiSer.vo;

import com.baomidou.mybatisplus.annotation.*;
import com.cs.apiSer.config.BaseEntity;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author houzy
 * @since 2021-04-18
 */
@Data
@TableName("cs_user")
@EqualsAndHashCode(callSuper = true)

@AllArgsConstructor
@NoArgsConstructor
public class User extends com.cs.api.pojo.User  {

    @TableId(type = IdType.ID_WORKER) //mybatis-plus主键注解
    @IsKey                         //actable主键注解
    //@IsAutoIncrement             //自增
    @Column                     //对应数据库字段，不配置name会直接采用属性名作为字段名
    Long id;
    /**
     * 创建时间
     */
    @TableField(value = "addTime",fill = FieldFill.INSERT)  // name指定数据库字段名，comment为备注
    Date addTime;

    @TableField(exist = false)
    Boolean deleteStatus = false;

    String password;

    @TableField(exist = false)
    List<Role> roles = new ArrayList<Role>();
}
