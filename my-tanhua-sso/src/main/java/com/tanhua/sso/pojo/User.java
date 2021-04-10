package com.tanhua.sso.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户表
 * @TableName tb_user
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BasePojo implements Serializable {
    /**
     * 
     */
    @TableId
    private Long id;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 密码，需要加密
     */
    @JsonIgnore
    private String password;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}