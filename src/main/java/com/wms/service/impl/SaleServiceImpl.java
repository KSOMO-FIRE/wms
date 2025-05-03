package com.wms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wms.pojo.dto.StartAndEnd;
import com.wms.pojo.entity.Goods;
import com.wms.pojo.entity.Goodstype;
import com.wms.pojo.entity.Record;
import com.wms.pojo.entity.Sale;
import com.wms.mapper.RecordMapper;
import com.wms.mapper.SaleMapper;
import com.wms.pojo.vo.Category;
import com.wms.pojo.vo.ReturnResult;
import com.wms.service.GoodsService;
import com.wms.service.GoodstypeService;
import com.wms.service.RecordService;
import com.wms.service.SaleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SaleServiceImpl extends ServiceImpl<SaleMapper, Sale> implements SaleService {

    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RecordService recordService;

    @Autowired
    private GoodstypeService goodstypeService;

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

    @Override
    public ReturnResult get(StartAndEnd time) {
        ReturnResult res = new ReturnResult();

        //计算OutboundTotal，InboundTotal
        //从sale表查询指定时间期限中记录
        LambdaQueryWrapper<Sale> saleLambdaQueryWrapperwrapper = new LambdaQueryWrapper<>();
        saleLambdaQueryWrapperwrapper.between(Sale::getDate, time.getStart(), time.getEnd());
        //没记录返回空值
        List<Sale> list = this.list(saleLambdaQueryWrapperwrapper);
        if (list.isEmpty()) {
            res.setOutboundTotal(0);
            res.setInboundTotal(0);
            res.setCategories(null);
            return res;
        }

        int totalOutbound = list.stream()
                .mapToInt(s -> s.getOutbound().intValue())
                .sum();
        res.setOutboundTotal(totalOutbound);

        int totalInbound = list.stream()
                .mapToInt(s -> s.getOutbound().intValue())
                .sum();
        res.setInboundTotal(totalInbound);

        //从record中查询
        LambdaQueryWrapper<Record> recordLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //位于指定时间内的数据
        recordLambdaQueryWrapper.between(Record::getCreatetime, time.getStart(), time.getEnd());
        //数量小于0即出库记录
        recordLambdaQueryWrapper.lt(Record::getCount, 0);
        List<Record> records = recordService.list(recordLambdaQueryWrapper);

        List<Category> categories = records.stream()
                //将每条记录映射为 Goods 和 count 的键值对
                .map(record -> {
                    Goods goods = goodsService.getById(record.getGoods());
                    return new AbstractMap.SimpleEntry<>(goods, record.getCount());
                })
                //过滤掉 goods 为空的无效记录
                .filter(entry -> entry.getKey() != null)
                //映射为 Category 对象
                .map(entry -> {
                    Goods goods = entry.getKey();
                    Integer goodsTypeId = goods.getGoodstype(); // 获取商品分类 ID
                    Goodstype goodstype = goodstypeService.getById(goodsTypeId);  // 查询分类详情
                    String typeName = (goodstype != null) ? goodstype.getName() : "其它";  // 获取分类名
                    int count = Math.abs(entry.getValue()); //取正数
                    return new Category(typeName, count);
                })
                //按分类名称进行分组求和
                .collect(Collectors.groupingBy(
                        Category::getCategoryName,
                        Collectors.summingInt(Category::getAmount)
                ))
                //将 Map 转换回 Stream<Entry<String, Integer>>，准备转换为 Category 列表
                .entrySet().stream()
                //将每个 Entry 转换为 Category 对象
                .map(e -> new Category(e.getKey(), e.getValue()))
                //按照出库数量从高到低排序
                .sorted(Comparator.comparing(Category::getAmount).reversed()) // 按数量从高到低排序
                .collect(Collectors.toList());

        res.setCategories(categories);
        return res;
    }

    /**
     * 计算指定日期的出/入库总额
     * @param records 当天出/入库记录
     * @param isOutRecord 是否为出库操作
     * @return 总额
     */
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
