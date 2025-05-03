package com.wms;

import com.wms.service.SaleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootTest
class WmsApplicationTests {

    @Autowired
    private SaleService saleService;

    @Test
    void contextLoads() {

    }

    @Test
    public void testCalculateLastThreeMonthsSales() {
        // 获取今天日期
        LocalDate today = LocalDate.now();

        // 循环计算过去90天的数据
        for (int i = 1; i <= 90; i++) {
            LocalDate dateToCalculate = today.minusDays(i);
            LocalDateTime startOfDay = dateToCalculate.atStartOfDay();
            System.out.println("正在处理：" + dateToCalculate);

            // 调用你封装好的方法
            saleService.calculateAndSaveDailySales(startOfDay);
        }

        System.out.println("✅ 过去90天的营业额已全部统计完毕，请检查数据库！");
    }

}
