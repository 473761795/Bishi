package com.example.mianshi.Service;

import org.junit.Test;
import org.junit.Assert;

public class VolumeExtractorTest {

    @Test
    public void testExtractVolume() {
        VolumeExtractorService volumeExtractor = new VolumeExtractorService();


        int volume = volumeExtractor.extractVolume("雪花啤酒8°清爽啤酒330ml*24听 罐装整箱麦芽酿制 武汉满百包邮");
        Assert.assertEquals(330, volume);

        volume = volumeExtractor.extractVolume("进口精酿啤酒迷失海岸花生酱牛奶世涛卡斯四料特浓巧克力组合装");
        Assert.assertEquals(0, volume);

        volume = volumeExtractor.extractVolume("可口可乐碳酸饮料2L*6瓶整箱装");
        Assert.assertEquals(2000, volume);
    }
}
