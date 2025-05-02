package com.wms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Client;
import com.wms.entity.Client;
import com.wms.service.impl.ClientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientServiceImpl clientService;

    /**
     * 获取所有的客户信息
     */
    @GetMapping("/list")
    public Result getAll(){
        List<Client> results = clientService.list();
        return Result.success(results);
    }

    /**
     * 获取供应商信息
     */
    @GetMapping("/list-supplier")
    public Result getSupplier(){
        LambdaQueryWrapper<Client> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Client::getType, 0);
        List<Client> results = clientService.list(wrapper);
        return Result.success(results);
    }

    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Client client){
        return clientService.save(client)?Result.success():Result.fail();
    }
    //更新
    @PostMapping("/update")
    public Result update(@RequestBody Client client){
        return clientService.updateById(client)?Result.success():Result.fail();
    }
    //删除
    @GetMapping("/del")
    public Result del(@RequestParam String id){
        return clientService.removeById(id)?Result.success():Result.fail();
    }


    /**
     * 客户信息分页查询
     * @param query 查询参数
     */
    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();
        String name = (String)param.get("name");
        String type = (String)param.get("type");


        Page<Client> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<Client> lambdaQueryWrapper = new LambdaQueryWrapper();
        if(StringUtils.isNotBlank(name) && !"null".equals(name)){
            lambdaQueryWrapper.like(Client::getName,name);
        }

        if(StringUtils.isNotBlank(type) && !"null".equals(type)){
            lambdaQueryWrapper.like(Client::getType,type);
        }

        IPage result = clientService.page(page,lambdaQueryWrapper);
        return Result.success(result.getRecords(),result.getTotal());
    }
}
