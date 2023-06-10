package com.example.mianshi.Service;

import com.example.mianshi.TagSeries;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagSeriesTest {
    @Test
    public void testTagSeries() {
        TagSeries tagSeries = new TagSeries();
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

        String series = tagSeries.tagSeries("雪花", "SNOW雪花纯生啤酒8度500ml*12罐匠心营造易拉罐装整箱黄啤酒 500mL*12瓶", mappingDict);
        Assert.assertEquals("清爽", series);

        series = tagSeries.tagSeries("雪花", "雪花啤酒8°清爽啤酒330ml*24听 罐装整箱麦芽酿制 武汉满百包邮", mappingDict);
        Assert.assertEquals("清爽", series);

        series = tagSeries.tagSeries("迷失海岸", "进口精酿啤酒迷失海岸花生酱牛奶世涛卡斯四料特浓巧克力组合装", mappingDict);
        Assert.assertEquals("花生巧克力牛奶世涛", series);
    }
}