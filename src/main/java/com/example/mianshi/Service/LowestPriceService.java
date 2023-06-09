package com.example.mianshi.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**

*这里是解答《基于促销信息 计算最低到手价》题
pattern匹配满减规则
 pattern匹配数量门槛
 quantity是购买数量
 lowestPrice是当前满减规则下的最低价
 最后的最低价从各种满减规则下的最低价中找出最低的
 pagePrice是页面价格
 promotionInfo是促销信息
 threshold是满减的金额门槛
 reduceAmount是满减减去的金额


 */
@Service
@Slf4j
public class LowestPriceService {
    public static int calculateLowestPrice(double pagePrice, String promotionInfo) {
        double lowestPrice = pagePrice;
        int quantity = 1;

        Pattern pattern = Pattern.compile("满(\\d+)(?:元)?减(\\d+)(?:元)?");
        Pattern pattern2 = Pattern.compile("购买(至少\\d+|\\d+-\\d+)件");
        Matcher matcher = pattern.matcher(promotionInfo);
        Matcher matcher2 = pattern2.matcher(promotionInfo);

        List<Double> calculatedPrices = new ArrayList<>();

        while (matcher.find()) {    //找满减规则
            for(int i =1;i<=matcher.groupCount();i+=2) { //遍历所有满减规则
                double threshold = Double.parseDouble(matcher.group(i));
                double reduceAmount = Double.parseDouble(matcher.group(i+1));
                    if(matcher2.find(1)) {       //如果有购买数量门槛
                        while (matcher2.find()) {        //找购买数量门槛
                            String quantityInfo = matcher2.group(1);
                            if (quantityInfo.contains("-")) {
                                // 处理购买1-3件这类的情况
                                String[] range = quantityInfo.split("-");
                                int minQuantity = Integer.parseInt(range[0]);
                                int maxQuantity = Integer.parseInt(range[1]);
                                //不到门槛就加购买数量
                                while (quantity < minQuantity) {
                                    quantity++;
                                }
                                while (quantity <= maxQuantity && pagePrice * quantity < threshold) {
                                    quantity++;
                                }
                            } else {
                                // 处理购买至少X件这类的情况
                                int minQuantity = Integer.parseInt(quantityInfo.replaceAll("\\D+", ""));
                                while (pagePrice * quantity < threshold || quantity < minQuantity) {   //不到满减门槛就加购买数量
                                    quantity++;
                                }
                            }

                            double calculatedPrice = (pagePrice * quantity - reduceAmount) / quantity;
                            System.out.println(calculatedPrice);
                            calculatedPrices.add(calculatedPrice);
                        }
                    }
                    else{   //没有数量门槛时，只考虑满减金额门槛
                        while (pagePrice * quantity < threshold) {
                            quantity++;
                        }
                    }

                double calculatedPrice = (pagePrice * quantity - reduceAmount) / quantity;
                calculatedPrices.add(calculatedPrice);
                    }
                quantity = 1;   //重置购买数量
                }
        // 找出最低到手价
        for (double price : calculatedPrices) {
            if (price < lowestPrice) {
                lowestPrice = price;
            }
        }

        return (int) Math.ceil(lowestPrice);
    }

    public static void main(String[] args) {
        // 执行单元测试-按上面一行的启动按钮就可以进行单元测试
        runUnitTests();
    }

    private static void runUnitTests() {
        // 单元测试示例
        runUnitTest(2069.00, "购买至少1件时可享受优惠,满2149元减130元", 2004);
        runUnitTest(1969.00, "购买1-3件时享受单件价¥1969，超出数量以结算价为准,满399减10", 1959);
        runUnitTest(1899.00, "满4999减150,满3999减120,满2999减90,满1999减60,满999减30", 1849);
        runUnitTest(440.00, "购买至少1件时可享受优惠", 440);
        runUnitTest(529.00, "无", 529);
    }

    private static void runUnitTest(double pagePrice, String promotionInfo, int expectedLowestPrice) {
        int lowestPrice = calculateLowestPrice(pagePrice, promotionInfo);

        System.out.println("Page Price: " + pagePrice);
        System.out.println("Promotion Info: " + promotionInfo);
        System.out.println("Expected Lowest Price: " + expectedLowestPrice);
        System.out.println("Calculated Lowest Price: " + lowestPrice);
        System.out.println();
    }
}
