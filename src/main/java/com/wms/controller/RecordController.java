package com.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.pojo.dto.QueryPageParam;
import com.wms.pojo.vo.Result;
import com.wms.pojo.entity.Goods;
import com.wms.pojo.entity.Record;
import com.wms.pojo.entity.Sale;
import com.wms.service.GoodsService;
import com.wms.service.RecordService;
import com.wms.service.SaleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wms
 * @since 2022-10-16
 */
@RestController
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private SaleService saleService;

    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();
        String name = (String)param.get("name");
        String goodstype = (String)param.get("goodstype");
        String storage = (String)param.get("storage");
        String roleId = (String)param.get("roleId");
        String userId = (String)param.get("userId");

        Page<Record> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        QueryWrapper<Record> queryWrapper = new QueryWrapper();
        queryWrapper.apply(" a.goods=b.id and b.storage=c.id and b.goodsType=d.id ");

        if("2".equals(roleId)){
           // queryWrapper.eq(Record::getUserid,userId);
            queryWrapper.apply(" a.userId= "+userId);
        }

        if(StringUtils.isNotBlank(name) && !"null".equals(name)){
            queryWrapper.like("b.name",name);
        }
        if(StringUtils.isNotBlank(goodstype) && !"null".equals(goodstype)){
            queryWrapper.eq("d.id",goodstype);
        }
        if(StringUtils.isNotBlank(storage) && !"null".equals(storage)){
            queryWrapper.eq("c.id",storage);
        }

        IPage result = recordService.pageCC(page,queryWrapper);
        return Result.success(result.getRecords(),result.getTotal());
    }
    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Record record){
        Goods goods = goodsService.getById(record.getGoods());
        int n = record.getAddNum();

        //查询sale表中是否有今天的销售额记录
        LambdaQueryWrapper<Sale> queryWrapper = new LambdaQueryWrapper<Sale>();
        queryWrapper.eq(Sale::getDate, LocalDate.now().atStartOfDay());
        Sale one = saleService.getOne(queryWrapper);

        //初始化
        Sale recordSale = new Sale();
        if(one != null){
            BeanUtils.copyProperties(one, recordSale);
        } else {
            recordSale.setDate(LocalDate.now().atStartOfDay());
            recordSale.setOutbound(BigDecimal.ZERO);
            recordSale.setInbound(BigDecimal.ZERO);
        }

        //出库 record.getAction()=2为出库，1为入库
        if("2".equals(record.getAction())){
             n = -n;
             //计算营业总额
             BigDecimal outSum = goodsService.getById(record.getGoods()).getPrice().multiply(BigDecimal.valueOf(record.getCount()));
             recordSale.setOutbound(outSum);
        } else {
            //计算营业总额
            BigDecimal InSum = goodsService.getById(record.getGoods()).getPrice().multiply(BigDecimal.valueOf(record.getCount()));
            recordSale.setInbound(InSum);
        }

        saleService.save(recordSale);

        record.setCount(n);
        int num = goods.getCount()+n;
        goods.setCount(num);

        //判断是否库存告急
        if (num < 10) {
            goods.setIsLowStock(true);
        }else {
            goods.setIsLowStock(false);
        }

        goodsService.updateById(goods);

        //记录操作时间
        record.setCreatetime(LocalDateTime.now());
        return recordService.save(record)?Result.success():Result.fail();
    }
}
