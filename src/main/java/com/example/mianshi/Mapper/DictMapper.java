package com.example.mianshi.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface DictMapper {
    @Select("SELECT brand, keyword1, keyword2, keyword3, keyword4, series FROM mapping_dict")
    List<Map<String, String>> getMappingDict();

}
