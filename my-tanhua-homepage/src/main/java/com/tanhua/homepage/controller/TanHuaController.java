package com.tanhua.homepage.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @description: some desc
 * @author: isolate
 * @email: 15071340963@163.com
 * @date: 2021/4/12 9:08
 */
@RequestMapping("/tanhua")
public class TanHuaController {

    @GetMapping("/todayBest")
    public ResponseEntity todayBest(){
        return null;
    }
}
