package com.wms.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wms.entity.Client;
import com.wms.mapper.ClientMapper;
import com.wms.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl extends ServiceImpl<ClientMapper, Client> implements ClientService {

    @Autowired
    ClientMapper clientMapper;

    @Override
    public IPage page(IPage page, Wrapper wrapper) {
        return clientMapper.page(page, wrapper);
    }
}
