package com.example.mianshi.Mapper;

import com.example.mianshi.Entity.Product;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ProductsMapper {
    @Select("SELECT * FROM products LIMIT #{offset}, #{limit}")
    List<Product> batchQueryProducts(@Param("offset") int offset, @Param("limit") int limit);

    @Update("UPDATE products SET series = #{series} WHERE product_id = #{product_id}")
    void updateProductSeries(@Param("product_id") int product_id, @Param("series") String series);


}
