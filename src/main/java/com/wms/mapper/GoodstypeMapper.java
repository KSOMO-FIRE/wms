package com.wms.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wms.pojo.entity.Goodstype;
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
public interface GoodstypeMapper extends BaseMapper<Goodstype> {
    @Select("SELECT COUNT(*) FROM (SELECT id, name, remark FROM goodstype WHERE ${ew.customSqlSegment}) TOTAL")
    int countCC(@Param(Constants.WRAPPER) Wrapper<Goodstype> wrapper);

    @Select("SELECT id, name, remark FROM goodstype ${ew.customSqlSegment}")
    IPage<Goodstype> pageCC(IPage<Goodstype> page, @Param(Constants.WRAPPER) Wrapper<Goodstype> wrapper);
}