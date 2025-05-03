package com.wms.controller;

import com.wms.pojo.dto.StartAndEnd;
import com.wms.pojo.entity.Sale;
import com.wms.pojo.vo.Result;
import com.wms.pojo.vo.ReturnResult;
import com.wms.service.SaleService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 营业额相关卡纸类
 */
@RestController
@RequestMapping("/sale")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping("/get")
    public Result get(@RequestBody StartAndEnd time) {
        ReturnResult res = saleService.get(time);
        return Result.success(res);
    }
}
