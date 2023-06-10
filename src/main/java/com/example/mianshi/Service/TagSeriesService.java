package com.example.mianshi.Service;

import com.example.mianshi.Entity.MappingDict;
import com.example.mianshi.Entity.Product;
import com.example.mianshi.Mapper.DictMapper;
import com.example.mianshi.Mapper.ProductsMapper;
import com.example.mianshi.TagSeries;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import static com.example.mianshi.TagSeries.tagSeries;

/**
 * 分批查询商品表
 * 1. 分批查询商品表
 * 2. 逐个打标签 映射词典也从表中查询
 * 3. 批量更新商品表 即对系列字段赋值
 */
@Service
@Slf4j
public class TagSeriesService {
    @Autowired
    private ProductsMapper productsMapper;
    @Autowired
    private DictMapper dictMapper;

    /**
     * 分批查询商品表
     *
     * @param batchSize 每批查询的记录数
     * @return 分批查询的结果列表
     */
    public List<List<Product>> batchQueryProducts(int batchSize) {
        int offset = 0;
        List<Product> products;
        List<List<Product>> result = new ArrayList<>();
        do {
            products = productsMapper.batchQueryProducts(offset, batchSize);

            // 处理查询结果
            List<Product> batchResult = new ArrayList<>();
            for (Product product : products) {
                int productId = product.getProduct_id();
                String productName = product.getProduct_name();
                String brand = product.getBrand();

                // 获取映射词典数据
                List<Map<String, String>> mappingDict = dictMapper.getMappingDict();

                // 调用打标签方法进行标签处理，将映射词典作为参数传递
                String series = tagSeries(brand, productName, mappingDict);

                // 设置系列字段值
                product.setSeries(series);

                // 将处理后的商品添加到批次结果中
                batchResult.add(product);
            }

            // 批量更新商品表的系列字段
            batchUpdateProductSeries(products);

            offset += batchSize;

            // 将批次结果添加到最终结果列表中
            log.info(batchResult.toString());
            result.add(batchResult);
        } while (!products.isEmpty());
        log.info(result.toString());
        return result;
    }


    // 批量更新商品表的系列字段 products为商品列表
    private void batchUpdateProductSeries(List<Product> products) {
        for (Product product : products) {
            productsMapper.updateProductSeries(product.getProduct_id(), product.getSeries());
        }
    }


}
