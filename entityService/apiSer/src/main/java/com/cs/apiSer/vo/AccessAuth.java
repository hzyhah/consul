package com.cs.apiSer.vo;

import com.baomidou.mybatisplus.annotation.*;
import com.cs.apiSer.config.BaseEntity;
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
 * @since 2021-05-28
 */
@Data
@TableName("access_auth")
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class AccessAuth extends com.cs.api.pojo.AccessAuth {

    private static final long serialVersionUID = 1L;

    private Long sub;

    private String type;
    @TableField(value = "access_auth")
    private String accessAuth;
    private String public_key;
    private String private_key;

    @TableId(type = IdType.ID_WORKER) //mybatis-plus主键注解
    @IsKey
    //actable主键注解
    Long id;
    /**
     * 创建时间
     */
    @TableField(value = "addTime",fill = FieldFill.INSERT)  // name指定数据库字段名，comment为备注
    Date addTime;

    boolean deleteStatus = false;

}
