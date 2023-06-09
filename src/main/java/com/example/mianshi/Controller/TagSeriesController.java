package com.example.mianshi.Controller;

import com.example.mianshi.Entity.Product;
import com.example.mianshi.Service.TagSeriesService;
import com.example.mianshi.response.ResponseWrapper;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "打标签-系列")
@RestController
@RequestMapping(path = "api/Product")
@Slf4j
public class TagSeriesController {

    @Autowired
    private TagSeriesService tagSeriesService;

    @ApiOperation(value = "对商品批量处理，返回更新后的分批查询商品表", notes = "分批查询商品表并进行逐个打标签和批量更新")
    @RequestMapping(value = "all", method = RequestMethod.GET)
    @ApiResponses(value = {@ApiResponse(code = 0, message = "商品", response = Product.class)})
    public Object all(@ApiParam("每批查询的记录数") int batchSize) {
        // 调用batchQueryProducts方法获取分批查询的结果
        List<List<Product>> result = tagSeriesService.batchQueryProducts(batchSize);

//        // 合并分批查询的结果列表为一个单一的商品列表
//        List<Product> mergedList = new ArrayList<>();
//        for (List<Product> productList : result) {
//            mergedList.addAll(productList);
//        }

        // 返回更新后的商品列表
        return ResponseWrapper.markCustom("1", "200", result);
    }
}
