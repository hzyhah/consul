package com.cs.apiSer.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("cs_user")
public class UserRole {
    Long uid;
    Long rid;
}
