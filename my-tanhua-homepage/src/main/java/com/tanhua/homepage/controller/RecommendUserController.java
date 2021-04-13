package com.tanhua.homepage.controller;

import com.tanhua.homepage.service.RecommendUserService;
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
public class RecommendUserController {

    @Autowired
    private RecommendUserService recommendUserService;

    @GetMapping("/todayBest")
    public ResponseEntity todayBest(){
        recommendUserService.queryTodayBest();
        return null;
    }
}
