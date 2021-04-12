package com.tanhua.homepage.controller;

import com.tanhua.common.pojo.User;
import com.tanhua.common.utils.UserThreadLocal;
import com.tanhua.homepage.service.TodayBestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: some desc
 * @author: isolate
 * @email: 15071340963@163.com
 * @date: 2021/4/12 9:08
 */
@RestController
@RequestMapping("/tanhua")
public class TodayBestController {

    @Autowired
    private TodayBestService todayBestService;

    @GetMapping("/todayBest")
    public ResponseEntity todayBest(){
        //1.因为我们已经在拦截器中进行了token校验,这里只需要获取用户的user对象
        User user = UserThreadLocal.get();
        todayBestService.queryTodayBest(user.getId());
        return null;
    }
}
