package com.wms.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wms.pojo.entity.Record;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wms
 * @since 2022-10-16
 */
@Mapper
public interface RecordMapper extends BaseMapper<Record> {
    IPage pageCC(IPage<Record> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * 查询指定时间段里出库的商品
     * @param start 开始时间
     * @param end 结束时间
     * @return 所有符合条件的记录
     */
    @Select("SELECT * FROM record WHERE createtime BETWEEN #{start} AND #{end} AND count < 0")
    List<Record> getOutRecord(LocalDateTime start, LocalDateTime end);

    @Select("SELECT * FROM record WHERE createtime BETWEEN #{start} AND #{end} AND count > 0")
    List<Record> getInRecord(LocalDateTime start, LocalDateTime end);
}