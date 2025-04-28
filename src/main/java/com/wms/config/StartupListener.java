package com.wms.config;

import com.wms.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {

    private final GoodsService goodsService;
    private boolean executed = false; // 防止多次执行（如 Web 应用中父子容器场景）

    public StartupListener(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 确保仅执行一次
        if (!executed) {
            goodsService.updateAlerts();
            executed = true;
            log.info("执行了GoodsServiceImpl.updateAlerts()");
        }
    }
}