package com.wms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wms.pojo.entity.Sale;

import java.time.LocalDateTime;

public interface SaleService extends IService<Sale> {

    /**
     * 统计指定日期的营业额，并保存到数据库
     * @param dateToCalculate 要统计的日期（例如：2025-05-01）
     */
    void calculateAndSaveDailySales(LocalDateTime dateToCalculate);
}
