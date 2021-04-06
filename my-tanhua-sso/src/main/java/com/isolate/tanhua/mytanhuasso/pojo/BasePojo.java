package com.isolate.tanhua.mytanhuasso.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.util.Date;

/**
 * @description: some desc
 * @author: isolate
 * @email: 15071340963@163.com
 * @date: 2021/4/6 19:51
 */
public class BasePojo {


    @TableField(fill = FieldFill.INSERT)
    public Date created;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    public Date updated;
}
