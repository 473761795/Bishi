package com.example.mianshi.Service;

import org.junit.Assert;
import org.junit.Test;

public class LowestPriceTest {

    @Test
    public void testCalculateLowestPrice() {
        // 创建 LowestPriceService 实例
        LowestPriceService lowestPriceService = new LowestPriceService();

        // 测试用例1
        double pagePrice1 = 2069.00;
        String promotionInfo1 = "购买至少1件时可享受优惠,满2149元减130元";
        int expectedLowestPrice1 = 2004;
        int calculatedLowestPrice1 = lowestPriceService.calculateLowestPrice(pagePrice1, promotionInfo1);
        Assert.assertEquals(expectedLowestPrice1, calculatedLowestPrice1);

        // 测试用例2
        double pagePrice2 = 1969.00;
        String promotionInfo2 = "购买1-3件时享受单件价¥1969，超出数量以结算价为准,满399减10";
        int expectedLowestPrice2 = 1959;
        int calculatedLowestPrice2 = lowestPriceService.calculateLowestPrice(pagePrice2, promotionInfo2);
        Assert.assertEquals(expectedLowestPrice2, calculatedLowestPrice2);

        // 测试用例3
        double pagePrice3 = 1899.00;
        String promotionInfo3 = "满4999减150,满3999减120,满2999减90,满1999减60,满999减30";
        int expectedLowestPrice3 = 1849;
        int calculatedLowestPrice3 = lowestPriceService.calculateLowestPrice(pagePrice3, promotionInfo3);
        Assert.assertEquals(expectedLowestPrice3, calculatedLowestPrice3);

        // 测试用例4
        double pagePrice4 = 440.00;
        String promotionInfo4 = "购买至少1件时可享受优惠";
        int expectedLowestPrice4 = 440;
        int calculatedLowestPrice4 = lowestPriceService.calculateLowestPrice(pagePrice4, promotionInfo4);
        Assert.assertEquals(expectedLowestPrice4, calculatedLowestPrice4);

        // 测试用例5
        double pagePrice5 = 529.00;
        String promotionInfo5 = "无";
        int expectedLowestPrice5 = 529;
        int calculatedLowestPrice5 = lowestPriceService.calculateLowestPrice(pagePrice5, promotionInfo5);
        Assert.assertEquals(expectedLowestPrice5, calculatedLowestPrice5);
    }
}
