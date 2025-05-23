package com.wms.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wms.pojo.entity.ExendGoods;
import com.wms.pojo.entity.Goods;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wms
 * @since 2022-10-15
 */
public interface GoodsService extends IService<Goods> {
    IPage pageCC(IPage<ExendGoods> page, Wrapper wrapper);

    //根据id查询生产日期和保质期
    Goods getProductionDateAndShelfDate(Integer id);

    void updateAlerts();
}
