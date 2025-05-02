package com.wms.task;

import com.wms.entity.Record;
import com.wms.entity.Sale;
import com.wms.mapper.RecordMapper;
import com.wms.service.GoodsService;
import com.wms.service.SaleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 统计销售额相关的定时任务
 */
@Component
@Slf4j
public class SaleTask {

    @Autowired
    private SaleService saleService;

    /**
     * 每天零点计算前一天的营业额
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void calculateDailySale() {

        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        saleService.calculateAndSaveDailySales(yesterday);

//        List<Record> sale = recordMapper.getSale(LocalDate.now().minusDays(1).atStartOfDay(), LocalDateTime.now());
//
//        //如果没有数据，当天的营业额值为0
//        if (sale.isEmpty()) {
//            Sale saleZero = new Sale();
//            saleZero.setSum(0L);
//            saleZero.setDate(LocalDate.now().minusDays(1).atStartOfDay());
//            saleService.save(saleZero);
//            log.info("【销售统计】{} 营业额为：0", LocalDate.now().minusDays(1).atStartOfDay());
//            return;
//        }
//
//        //有数据时
//        //计算中销量
//        Long totalSales = sale.stream()
//                .map(record -> goodsService.getById(record.getGoods()).getPrice().longValue())
//                .reduce(0L, Long::sum);
//
//        Sale saleSummary = new Sale();
//        saleSummary.setSum(totalSales);
//        saleSummary.setDate(LocalDate.now().minusDays(1).atStartOfDay());
//        saleService.save(saleSummary);
//
//        log.info("【销售统计】{} 营业额为：{}", LocalDate.now().minusDays(1).atStartOfDay(), totalSales);
    }

}
