package com.tanhua.homepage.service;

import com.tanhua.homepage.pojo.RecommendUser;
import com.tanhua.homepage.vo.TodayBest;

/**
 * @description: some desc
 * @author: isolate
 * @email: 15071340963@163.com
 * @date: 2021/4/13 10:54
 */
public interface RecommendUserService {

    RecommendUser queryWithMaxScore(Long id);

    TodayBest queryTodayBest();
}
