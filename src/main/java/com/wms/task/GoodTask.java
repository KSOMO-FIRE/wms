package com.wms.task;

import com.wms.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 商品相关定时任务
 */
@Component
@Slf4j
public class GoodTask {

    @Autowired
    GoodsService goodsService;

    /**
     * 每天零点更新商品保质期和数量提醒
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void update() {
        goodsService.updateAlerts();
    }
}
