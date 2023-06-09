package com.example.mianshi.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *这里是解答《打标签--单量》这道题的
 * productName是商品名称
 */

//方法：提前当量
@Service
@Slf4j
public class VolumeExtractorService {
    private static final String VOLUME_REGEX = "(\\d+(?:\\.\\d+)?)(mL|ml|毫升|ML|L|升)";

    public static int extractVolume(String productName) {
        // 创建 Pattern 对象
        Pattern regex = Pattern.compile(VOLUME_REGEX);

        // 在商品名称中查找匹配的模式
        Matcher matcher = regex.matcher(productName);

        // 如果找到匹配的模式
        if (matcher.find()) {
            // 获取匹配到的数字
            String number = matcher.group(1);

            // 获取匹配到的单位
            String unit = matcher.group(2);

            // 根据单位进行转换
            if (unit != null) {
                if (unit.equalsIgnoreCase("mL") || unit.equalsIgnoreCase("ml") || unit.equalsIgnoreCase("毫升")) {
                    return (int) Double.parseDouble(number);
                } else if (unit.equalsIgnoreCase("L") || unit.equalsIgnoreCase("升")) {
                    return (int) (Double.parseDouble(number) * 1000);
                }
            }

            // 默认返回匹配到的数字
            return (int) Double.parseDouble(number);
        }

        // 没有找到匹配的模式，默认返回0
        return 0;
    }

    //  ※※※单元测试※※※
    // 如果想要单元测试请按下面这个函数的启动键
    public static void main(String[] args) {
        String[] productNames = {
                "黑狮啤酒 啤酒黑狮白啤听装 500mL*12听罐整箱 雪花匠心营造500ml*12罐",
                "SNOW雪花纯生啤酒8度500ml*12罐匠心营造易拉罐装整箱黄啤酒 500mL*12瓶",
                "【啤酒周边纪念品】百威啤酒5款355毫升红色经典限量版空铝瓶",
                "北京 广州仓直发 坦克伯爵精酿 百香果味 10度百香果小麦白啤酒330ML×12瓶 整箱装 果香十足",
                "泰山传说官方经典正品德式进口工艺酿造世涛黑啤酒1L*12桶装包邮泡沫丰富饱满口味持久浓郁甘甜爽口营养丰富",
                "青西金琥珀拉格啤酒青岛特产精酿啤酒5L装熟啤酒节青岛特产全麦大桶装精酿拉格啤酒",
                "青岛特产精酿原浆啤酒全麦白啤蓝宝石酿酒师浑浊2升桶装促销包邮",
                "(1.35L*6桶)俄罗斯进口波罗的海远东古典啤酒 远东烈性啤酒 大麦黄啤整箱啤酒 远东古典1.35升*6桶(口感适中)",
                "自由落体哈密瓜水果艾尔国产精酿果啤微醺酒果味酒女士低度酒饮料"
        };

        for (String productName : productNames) {
            int volume = extractVolume(productName);
            System.out.println("Product Name: " + productName);
            System.out.println("Volume: " + volume);
            System.out.println("-----------------------");
        }
    }
}
