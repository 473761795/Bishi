package com.example.mianshi.Controller;

import com.example.mianshi.Entity.Product;
import com.example.mianshi.Service.LowestPriceService;
import com.example.mianshi.response.ResponseWrapper;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "基于促销信息 计算最低到手价")
@RestController
@RequestMapping(path = "api/Lowest")
@Slf4j
public class LowestPriceController {
    @Autowired
    private LowestPriceService lowestPriceService;

    @ApiOperation(value = "计算最低价", notes = "输入页面价格，促销信息，返回最低到手价")
    @RequestMapping(value = "all", method = RequestMethod.GET)
    @ApiResponses(value = {@ApiResponse(code = 0, message = "商品", response = Product.class)})
    public Object all(@ApiParam("页面价格") double pagePrice, @ApiParam("促销信息") String promotionInfo) {
        // 调用batchQueryProducts方法获取分批查询的结果
        int result = lowestPriceService.calculateLowestPrice(pagePrice,promotionInfo);

        return ResponseWrapper.markCustom("1", "200", result);
    }
}
