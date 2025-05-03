package com.wms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wms.pojo.entity.Goods;
import com.wms.pojo.entity.Record;
import com.wms.pojo.entity.Sale;
import com.wms.mapper.RecordMapper;
import com.wms.mapper.SaleMapper;
import com.wms.service.GoodsService;
import com.wms.service.SaleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Slf4j
public class SaleServiceImpl extends ServiceImpl<SaleMapper, Sale> implements SaleService {

    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private GoodsService goodsService;

    /**
     * 统计指定日期的营业额，并保存到数据库
     * @param dateToCalculate 要统计的日期（例如：2025-05-01）
     */
    public void calculateAndSaveDailySales(LocalDateTime dateToCalculate) {
        // 获取该天的开始时间和结束时间
        LocalDateTime startOfDay = dateToCalculate.with(LocalTime.MIN);
        LocalDateTime endOfDay = dateToCalculate.with(LocalTime.MAX);

        // 查询该天的所有销售记录
        List<Record> sale = recordMapper.getSale(startOfDay, endOfDay);

        // 如果没有销售记录，插入 sum=0 的记录
        if (sale == null || sale.isEmpty()) {
            Sale saleZero = new Sale();
            saleZero.setSum(BigDecimal.ZERO);
            saleZero.setDate(startOfDay);
            this.save(saleZero); // 保存
            log.info("【销售统计】{} 营业额为：0", startOfDay.toLocalDate());
            return;
        }

        // 有数据时，计算总销售额
        BigDecimal totalSales = sale.stream()
                .map(record -> {
                    // 获取商品信息，防止空指针
                    Goods goods = goodsService.getById(record.getGoods());
                    Integer num = -record.getCount();
                    BigDecimal sum = goods.getPrice().multiply(new BigDecimal(num));
                    return sum;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 构建并保存统计记录
        Sale saleSummary = new Sale();
        saleSummary.setSum(totalSales);
        saleSummary.setDate(startOfDay);
        this.save(saleSummary);

        // 日志输出
        log.info("【销售统计】{} 营业额为：{}", startOfDay.toLocalDate(), totalSales);
    }
}
