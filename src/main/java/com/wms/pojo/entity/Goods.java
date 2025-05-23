package com.wms.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author wms
 * @since 2022-10-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Goods对象", description="")
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "货名")
    private String name;

    @ApiModelProperty(value = "仓库")
    private Integer storage;

    @ApiModelProperty(value = "分类")
    @TableField("goodsType")
    private Integer goodstype;

    @ApiModelProperty(value = "数量")
    private Integer count;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value="规格")
    private String specification;

    @ApiModelProperty(value="图片")
    private String imageUrl;

    @ApiModelProperty(value="保质期")
    private Integer shelfDate;

    @ApiModelProperty(value="生产日期")
    private LocalDate productionDate;

    @ApiModelProperty(value="价格")
    private BigDecimal price;

    @ApiModelProperty(value="临期提醒")
    private Boolean isNearExpiry;

    @ApiModelProperty(value="库存告警")
    private Boolean isLowStock;

    @ApiModelProperty(value="客户id")
    private Integer clientId;

}
