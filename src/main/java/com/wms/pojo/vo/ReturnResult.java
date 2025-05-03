package com.wms.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnResult {

    Integer outboundTotal;

    Integer inboundTotal;

    List<Category> categories;
}
