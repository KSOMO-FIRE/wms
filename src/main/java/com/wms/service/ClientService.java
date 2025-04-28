package com.wms.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wms.entity.Client;
import com.wms.entity.Goods;

public interface ClientService extends IService<Client> {

    IPage page(IPage page, Wrapper wrapper);
}
