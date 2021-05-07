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

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author houzy
 * @since 2021-04-15
 */
@Data
@TableName("cs_user")
@AllArgsConstructor
@NoArgsConstructor
public class Doctor  {

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

    Boolean deleteStatus = false;
    private static final long serialVersionUID = 1L;

    private String name;

    private String title;

    private String unit;




}
