package com.wms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wms.pojo.entity.Goods;
import com.wms.pojo.entity.Record;
import com.wms.pojo.entity.Sale;
import com.wms.mapper.RecordMapper;
import com.wms.mapper.SaleMapper;
import com.wms.service.GoodsService;
import com.wms.service.SaleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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

        // 查询该天的所有出库记录
        List<Record> outRecord = recordMapper.getOutRecord(startOfDay, endOfDay);

        // 查询该天的所有入库记录
        List<Record> inRecord = recordMapper.getInRecord(startOfDay, endOfDay);

        //查询数据库里面有没有当天的sale记录
        LambdaQueryWrapper<Sale> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Sale::getDate, startOfDay);
        Sale one = this.getOne(wrapper);

        Sale record = new Sale();
        record.setDate(startOfDay);
        record.setOutbound(calculate(outRecord, true));
        record.setInbound(calculate(inRecord, false));

        //有记录，复制id
        if (one != null) {
            record.setId(one.getId());
        }
        this.saveOrUpdate(record);
        log.info("{} 入库总额为：{}, 出库总额为：{}", startOfDay, record.getInbound(), record.getOutbound());
    }

    private BigDecimal calculate(List<Record> records, Boolean isOutRecord) {
        // 如果没有销售记录，返回0
        if (records == null || records.isEmpty()) {
            return BigDecimal.ZERO;
        }

        // 有数据时，计算总销售额
        return records.stream()
                .map(record -> {
                    // 获取商品信息，防止空指针
                    Goods goods = goodsService.getById(record.getGoods());
                    Integer num;
                    if (isOutRecord) {
                        num = -record.getCount();
                    } else {
                        num = record.getCount();
                    }
                    return goods.getPrice().multiply(new BigDecimal(num));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
