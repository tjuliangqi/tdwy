package cn.tju.tdwy.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NLPUtils {
    /**
     *
     * @param text example:text = "津JHT808在2019-07-20T13:16到2019-07-25T13:16的行车记录";
     * @return
     */
    public static Map textToMap (String text){
        Map map = new HashMap();
        String carNum = "津JHT808";
        String beginTime = "2019-06-30";
        String endTime = "2019-07-30";
        Pattern pattern = Pattern.compile("(.*?)在(.*?)到(.*?)的行车记录");
        Matcher matcher = pattern.matcher(text);
        matcher.find();
        try {
            carNum = matcher.group(1);
        } catch (Exception e){
            System.out.println("nlp无法获取carNum，使用默认津JHT808");
        }
        try {
            beginTime = matcher.group(2);
        } catch (Exception e){
            System.out.println("nlp无法获取beginTime，使用默认2019-06-30");
        }
        try {
            endTime = matcher.group(3);
        } catch (Exception e){
            System.out.println("nlp无法获取endTime，使用默认2019-07-30");
        }
        map.put("carNum",carNum);
        map.put("beginTime",beginTime);
        map.put("endTime",endTime);

        return map;
    }

}
