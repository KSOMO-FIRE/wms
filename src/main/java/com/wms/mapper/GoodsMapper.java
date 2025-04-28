package com.wms.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wms.entity.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wms.entity.Storage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wms
 * @since 2022-10-15
 */
@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {
    @Select("SELECT COUNT(*) FROM (SELECT id, name, storage, goodsType, count, remark FROM goods  ${ew.customSqlSegment}) TOTAL")
    int countCC(@Param(Constants.WRAPPER) Wrapper<Goods> wrapper);

    @Select("SELECT goods.id, goods.name, goods.storage, goods.goodsType, goods.count, goods.remark, goods.imageUrl, goods.productionDate, goods.specification, goods.shelfDate, goods.price, goods.isNearExpiry, goods.isLowStock, client.name AS clientName FROM goods  ${ew.customSqlSegment}")
    IPage<Goods> pageCC(IPage<Goods> page, @Param(Constants.WRAPPER) Wrapper<Goods> wrapper);

    @Select("SELECT productionDate, shelfDate FROM goods WHERE id = #{id}")
    Goods getProductionDateAndShelfDate(Integer id);
}
