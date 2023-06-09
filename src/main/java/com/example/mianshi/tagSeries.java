package com.example.mianshi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class tagSeries {
    /**
     * 这里是解答《打标签-系列》问题的
     * 打系列标签方法
     *
     * @param brand       品牌值
     * @param productName 商品名称
     * @param mappingDict 映射词典，包含品牌、第一关键字、第二关键字、第三关键字、第四关键字和映射值
     * @return 系列标签
     */
    public static String tagSeries(String brand, String productName, List<Map<String, String>> mappingDict) {
        // 先识别品牌
        if (brand.equals("其他")) {
            return "其他";
        } else if (brand.isEmpty()) {
            // 品牌为空时，返回空系列标签
            return "";
        }

        // 根据品牌筛选映射词典中的候选项
        List<Map<String, String>> filteredDict = new ArrayList<>();
        for (Map<String, String> entry : mappingDict) {
            if (brand.equals(entry.get("brand"))) {
                filteredDict.add(entry);
            }
        }

        // 匹配第一关键字
        for (Map<String, String> entry : filteredDict) {
            if (productName.contains(entry.get("keyword1"))) {
                return entry.get("series");
            }
        }

        // 匹配第二、第三、第四关键字（2 3 4是 and关系，需要都匹配到才可以）
        for (Map<String, String> entry : filteredDict) {
            if ((entry.get("keyword2").isEmpty() || productName.contains(entry.get("keyword2"))) &&
                    (entry.get("keyword3").isEmpty() || productName.contains(entry.get("keyword3"))) &&
                    (entry.get("keyword4").isEmpty() || productName.contains(entry.get("keyword4")))) {
                if (entry.get("keyword2").isEmpty() && entry.get("keyword3").isEmpty() && entry.get("keyword4").isEmpty()) {
                    return brand + "其他";    //当心第二三四关键字都为空的特殊情况
                } else {
                    return entry.get("series");
                }
            }
        }

        // 品牌有值但系列没打上标签时，系列值为 品牌+其他
        return brand + "其他";
    }


    // 单元测试-验证逻辑正确性-方便自己测试上面的打标签方法是否好用
    public static void test() {
        // 创建映射词典
        List<Map<String, String>> mappingDict = new ArrayList<>();
        Map<String, String> entry1 = new HashMap<>();
        entry1.put("brand", "雪花");
        entry1.put("keyword1", "8度");
        entry1.put("keyword2", "8°");
        entry1.put("keyword3", "");
        entry1.put("keyword4", "");
        entry1.put("series", "清爽");
        mappingDict.add(entry1);

        Map<String, String> entry2 = new HashMap<>();
        entry2.put("brand", "迷失海岸");
        entry2.put("keyword1", "花生巧克力牛奶世涛");
        entry2.put("keyword2", "花生");
        entry2.put("keyword3", "巧克力");
        entry2.put("keyword4", "牛奶");
        entry2.put("series", "花生巧克力牛奶世涛");
        mappingDict.add(entry2);

        Map<String, String> entry3 = new HashMap<>();
        entry3.put("brand", "海妖精酿");
        entry3.put("keyword1", "海妖之泪");
        entry3.put("keyword2", "");
        entry3.put("keyword3", "");
        entry3.put("keyword4", "");
        entry3.put("series", "海妖之泪");
        mappingDict.add(entry3);

        // 示例2
        String brand = "海妖精酿";
        String productName = "海妖精酿啤酒瓶装比利时小麦白啤330ml12瓶包邮";

        // 调用打系列标签方法
        String series = tagSeries(brand, productName, mappingDict);
        System.out.println("单元测试↓\n系列标签: " + series);
    }


    //如果想开始单元测试请按下面一行的启动键
    public static void main(String[] args) {
        test();
    }

}
