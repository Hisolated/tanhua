package com.tanhua.homepage.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.tanhua.common.pojo.User;
import com.tanhua.common.pojo.UserInfo;
import com.tanhua.common.utils.UserThreadLocal;
import com.tanhua.homepage.pojo.RecommendUser;
import com.tanhua.homepage.service.RecommendUserService;
import com.tanhua.homepage.service.UserInfoService;
import com.tanhua.homepage.vo.TodayBest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 * @description: some desc
 * @author: isolate
 * @email: 15071340963@163.com
 * @date: 2021/4/13 10:52
 */
@Service
public class RecommendUserServiceImpl implements RecommendUserService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserInfoService userInfoService;

    @Value("${tanhua.sso.default.user}")
    private Long defaultUser;

    /**
     * 查询mongdb对数据进行拼凑成一个todaybest对象
     * @return
     */

    @Override
    public TodayBest queryTodayBest() {
        /**
         * 需要的字段id(默认今日佳人,及新用户注册后还没有大数据统计喜好默认),avatar(头像),nickname(昵称),
         * gender(性别),tags(用户标签),fateValue(缘分值),
         * 由参数可看出
         *  1. 我们先查询todaybest表中是否存在该用户的数据,有就直接返回用户的今日佳人和缘分值,
         *      然后拼接从userinfo获取的数据
         *  2. 我们需要查询我们mysql的userinfo表,将avatar(头像),nickname(昵称),gender(性别),tags(用户标签)
         *      返回出来,并拼接到todaybest对象中
         *      而我们的userinfo表以及提取到了common模块系统中,所以我们可以通过创建一个userinfoservice
         *      对common模块进行调用Mapper进行调用,获取所需值
         *  3. 到这里说明是新用户了,id,fateValue可以设置默认值,然后再拼接userinfo数据
         */

        /**
         * 我们已经通过common模块对token进行统一解析和将解析数据存储到localThread中了
         * 所以我们是通过获取当前线程来获取user数据,并
         */
        User user = UserThreadLocal.get();

        /**
         * 获取拼接todayBest表中数据,由于我们是用mongDB存储数据的,而且并不存在一个todayBest表,
         * 我们所需的数据(id,fateValue)都在一个recommend_user表中
         * 需要通过mongDB查询最大缘分值的人
         */
        //1. 先查询缘分最大的人
        RecommendUser RecommendUser = queryWithMaxScore(user.getId());
        //2. 定义一个todayBest对象,用于拼接我们的结果集
        TodayBest todayBest =new TodayBest();
        if(ObjectUtil.isEmpty(RecommendUser)){
            // 2.1 没有,这表示是新用户,直接拼接几个默认值,默认最佳为配置文件配置,默认缘分值为80
            todayBest.setId(defaultUser);
            todayBest.setFateValue(80L);
        }
        //2.2 有则直接拼接
        todayBest.setId(RecommendUser.getUserId());
        //2.2.1 longValue()方法只取整,小数后面不管是什么,只取小数点前面的数字
        todayBest.setFateValue(RecommendUser.getScore().longValue());

        //3. 查询我们的mysql中userinfo表获取数据
        UserInfo userInfo = userInfoService.getById(user.getId());
        //3.1 判断userinfo是否存在,不存在直接返回null
        if(ObjectUtil.isEmpty(userInfo)){
            //不存在直接返回null,todo:这里以后可抛出自定义异常
            return null;
        }
        //4. 将查询的数据拼接到todayBest中,avatar(头像),nickname(昵称),gender(性别),tags(用户标签)
        todayBest.setAvatar(userInfo.getLogo());
        todayBest.setNickname(userInfo.getNickName());
        todayBest.setGender(userInfo.getSex().getValue() == 1 ? "man":"women");
        todayBest.setTags(StringUtils.split(userInfo.getTags(),","));

        return todayBest;
    }


    @Override
    public RecommendUser queryWithMaxScore(Long userId) {
        //查询得分最高的用户，按照得分倒序排序
        Query query = Query.query(Criteria.where("toUserId").is(userId))
                .with(Sort.by(Sort.Order.desc("score"))).limit(1);
        return this.mongoTemplate.findOne(query, RecommendUser.class);
    }

}
