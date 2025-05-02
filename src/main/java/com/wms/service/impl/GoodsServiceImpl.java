package com.wms.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wms.entity.ExendGoods;
import com.wms.entity.Goods;
import com.wms.entity.User;
import com.wms.mapper.GoodsMapper;
import com.wms.mapper.UserMapper;
import com.wms.service.GoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wms
 * @since 2022-10-15
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {
    @Resource
    private GoodsMapper goodsMapper;
    @Override
    public IPage pageCC(IPage<ExendGoods> page, Wrapper wrapper) {
        return goodsMapper.pageCC(page,wrapper);
    }

    @Override
    public Goods getProductionDateAndShelfDate(Integer id) {
        return goodsMapper.getProductionDateAndShelfDate(id);
    }


    @Override
    public void updateAlerts() {
        // 查询所有商品
        List<Goods> goodsList = this.list();

        // 遍历商品列表，更新库存告急和临期提醒状态
        for (Goods goods : goodsList) {
            // 计算库存告急
            if (goods.getCount() != null && goods.getCount() < 10) {
                goods.setIsLowStock(true);
            } else {
                goods.setIsLowStock(false);
            }

            // 计算临期提醒
            if (goods.getProductionDate() != null && goods.getShelfDate() != null) {
                LocalDate expiryDate = goods.getProductionDate().plusDays(goods.getShelfDate());
                LocalDate today = LocalDate.now();

                if (expiryDate.isBefore(today) || expiryDate.minusDays(goods.getShelfDate() / 4).isBefore(today)) {
                    goods.setIsNearExpiry(true);
                } else {
                    goods.setIsNearExpiry(false);
                }
            }

            // 更新商品记录
            this.updateById(goods);
        }
    }
}
