package com.wms.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wms.pojo.entity.ExendGoods;
import com.wms.pojo.entity.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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

    @Select("SELECT goods.id, goods.name, goods.storage, goods.goodsType, goods.count, goods.remark, goods.imageUrl, goods.productionDate, goods.specification, goods.shelfDate, goods.price, goods.isNearExpiry, goods.isLowStock, client.name AS clientName FROM goods INNER JOIN client ON goods.clientId = client.id ${ew.customSqlSegment}")
    IPage<ExendGoods> pageCC(IPage<ExendGoods> page, @Param(Constants.WRAPPER) Wrapper<ExendGoods> wrapper);

    @Select("SELECT productionDate, shelfDate FROM goods WHERE id = #{id}")
    Goods getProductionDateAndShelfDate(Integer id);
}
