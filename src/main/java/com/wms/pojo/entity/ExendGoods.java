package com.wms.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExendGoods extends Goods{

    //商品剩余保质期，如果商品过期设置为为-1
    private Integer days;

    private String clientName;
}
