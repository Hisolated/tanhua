package com.tanhua.homepage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanhua.homepage.mapper.TodayBestMapper;
import com.tanhua.homepage.service.TodayBestService;
import com.tanhua.homepage.vo.TodayBest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description: some desc
 * @author: isolate
 * @email: 15071340963@163.com
 * @date: 2021/4/12 23:55
 */
@Service
public class TodayBestServiceImpl extends ServiceImpl<TodayBestMapper,TodayBest> implements TodayBestService {

    @Autowired
    private TodayBestMapper todayBestMapper;

    @Override
    public TodayBest queryTodayBest(Long id) {


        return null;
    }
}
