package com.wms.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.util.pattern.PathPattern;

import java.util.HashMap;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryPageParam {
    //默认
    private static int PAGE_SIZE =20;
    private static int PAGE_NUM =1;


    private int pageSize =PAGE_SIZE;
    private int pageNum =PAGE_NUM;

    private HashMap param = new HashMap();
}
