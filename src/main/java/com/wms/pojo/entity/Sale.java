package com.wms.pojo.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "营业额", description = "")
public class Sale implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private Integer id;

    @ApiModelProperty(value = "当天出库总额")
    private BigDecimal outbound;

    @ApiModelProperty(value = "时间")
    private LocalDateTime date;

    @ApiModelProperty(value = "当天出库总额")
    private BigDecimal inbound;
}
