package com.tanhua.homepage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tanhua.homepage.vo.TodayBest;

/**
 * @description: some desc
 * @author: isolate
 * @email: 15071340963@163.com
 * @date: 2021/4/12 23:54
 */
public interface TodayBestService extends IService<TodayBest> {
    TodayBest queryTodayBest(Long id);
}
