package com.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.pojo.dto.QueryPageParam;
import com.wms.pojo.vo.Result;
import com.wms.pojo.entity.Goods;
import com.wms.pojo.entity.ExendGoods;
import com.wms.service.GoodsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wms
 * @since 2022-10-15
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

   @Autowired
    private GoodsService goodsService;
    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Goods goods){
        return goodsService.save(goods)?Result.success():Result.fail();
    }
    //更新
    @PostMapping("/update")
    public Result update(@RequestBody Goods goods){
        return goodsService.updateById(goods)?Result.success():Result.fail();
    }
    //删除
    @GetMapping("/del")
    public Result del(@RequestParam String id){
        return goodsService.removeById(id)?Result.success():Result.fail();
    }

    /**
     * 根据查询条件分类显示商品信息
     * @param query 查询条件
     */
    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();
        String name = (String)param.get("name");
        String goodstype = (String)param.get("goodstype");
        String storage = (String)param.get("storage");
        String supplierId = (String)param.get("supplier");

        Page<ExendGoods> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<Goods> lambdaQueryWrapper = new LambdaQueryWrapper();
        if(StringUtils.isNotBlank(name) && !"null".equals(name)){
            lambdaQueryWrapper.like(Goods::getName,name);
        }
        if(StringUtils.isNotBlank(goodstype) && !"null".equals(goodstype)){
            lambdaQueryWrapper.eq(Goods::getGoodstype,goodstype);
        }
        if(StringUtils.isNotBlank(storage) && !"null".equals(storage)){
            lambdaQueryWrapper.eq(Goods::getStorage,storage);
        }
        if(StringUtils.isNotBlank(supplierId) && !"null".equals(supplierId)){
            lambdaQueryWrapper.eq(Goods::getClientId,Integer.parseInt(supplierId));
        }

        // 分页查询（返回 IPage<Goods>）
        IPage<ExendGoods> result = goodsService.pageCC(page, lambdaQueryWrapper);

// 转换为 List<ExtendGoods>
        List<ExendGoods> extendGoodsList = result.getRecords().stream()
                .map(goods -> {
                    // 创建 ExtendGoods 对象并拷贝属性
                    ExendGoods extendGoods = new ExendGoods();
                    BeanUtils.copyProperties(goods, extendGoods); // 使用 Spring 的 BeanUtils

                    // 计算剩余保质期天数
                    int days = calculateRemainingDays(
                            goods.getProductionDate(),
                            goods.getShelfDate()
                    );
                    extendGoods.setDays(days);

                    return extendGoods;
                })
                .collect(Collectors.toList());

// 返回结果
        return Result.success(extendGoodsList, result.getTotal());
    }

    /**
     * 计算剩余保质期
     * @param productionDate 生产日期
     * @param shelfDate 保质期
     * @return 剩余保质期天数 已过期为-1
     */
    private Integer calculateRemainingDays(LocalDate productionDate, Integer shelfDate) {
        if (productionDate == null || shelfDate == null || shelfDate < 0) {
            return -1; // 处理无效输入
        }

        // 计算过期日期
        LocalDate expirationDate = productionDate.plusDays(shelfDate);
        LocalDate currentDate = LocalDate.now();

        // 剩余天数 = 过期日期 - 当前日期
        long remainingDays = ChronoUnit.DAYS.between(currentDate, expirationDate);

        // 已过期则返回 -1，否则返回剩余天数
        return remainingDays >= 0 ? (int) remainingDays : -1;
    }
}
