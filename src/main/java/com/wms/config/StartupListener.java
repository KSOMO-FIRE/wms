package com.wms.config;

import com.wms.service.GoodsService;
import com.wms.service.SaleService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {

    private final GoodsService goodsService;
    private final SaleService saleService;
    private boolean executed = false; // 防止多次执行（如 Web 应用中父子容器场景）

    public StartupListener(GoodsService goodsService, SaleService saleService) {
        this.goodsService = goodsService;
        this.saleService = saleService;
    }

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        // 确保仅执行一次
        if (!executed) {
            goodsService.updateAlerts();
            saleService.calculateAndSaveDailySales(LocalDateTime.now());
            executed = true;
            log.info("执行了GoodsServiceImpl.updateAlerts()");
            log.info("执行了SaleServiceImpl.calculateAndSaveDailySales()");
        }
    }
}