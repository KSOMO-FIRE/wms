package com.wms.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String account;
    private String username;
    private String password;
    private Integer age;
    private Integer sex;
    private Integer roleId;
    private String phone;
//    private Boolean isvalid;
}
