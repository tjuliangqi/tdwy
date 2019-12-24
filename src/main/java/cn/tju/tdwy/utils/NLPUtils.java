package cn.tju.tdwy.utils;

import cn.tju.tdwy.dao.RoadMapper;
import cn.tju.tdwy.daomain.CarBean;
import org.json.JSONException;
import cn.tju.tdwy.service.roadService;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.tju.tdwy.service.CarService.carSearchList;

public class NLPUtils {
    /**
     *
     * @param text example:text = "津JHT808在2019-07-20T13:16到2019-07-25T13:16的行车记录";
     * @return
     */
    public static Map textToMap (String text, RoadMapper roadMapper) throws IOException, JSONException {
        Map map = new HashMap();
        String carNum = "津N88888";
        String beginTime = "2019-06-30";
        String endTime = "2019-07-30";
        String roadNum = "";

        if (text.contains("行车记录") && text.length()<14){
            Pattern pattern = Pattern.compile("(.*?)的行车记录");
            Matcher matcher = pattern.matcher(text);
            matcher.find();
            try {
                carNum = matcher.group(1).toUpperCase();
            } catch (Exception e) {
                System.out.println("nlp无法获取carNum，使用默认津N88888");
            }

            Object carBeans = carSearchList("0", carNum, false, "", true, roadMapper);
            map.put("nlpType","nlp2");
            map.put("carBeans",carBeans);
        }
        else if (text.contains("行车记录") && text.contains("在")){
            Pattern pattern = Pattern.compile("(.*?)在(.*?)到(.*?)的行车记录");
            Matcher matcher = pattern.matcher(text);
            matcher.find();
            try {
                carNum = matcher.group(1).toUpperCase();
            } catch (Exception e){
                System.out.println("nlp无法获取carNum，使用默认津N88888");
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

            Object carBeans = carSearchList("3", carNum+","+beginTime+","+endTime, false, "", true, roadMapper);
            map.put("nlpType","nlp2");
            map.put("carBeans",carBeans);
        }//首次入城车辆
        else if (text.contains("首次入城")){
            Pattern pattern = Pattern.compile("(.*?)是否是首次入城(.*?)");
            Matcher matcher = pattern.matcher(text);
            matcher.find();
            try {
                carNum = matcher.group(1).toUpperCase();
            } catch (Exception e) {
                System.out.println("nlp无法获取carNum，使用默认津N88888");
            }

            Object carBeans = carSearchList("nlp4", carNum, false, "", false, roadMapper);
            map.put("nlpType","nlp4");
            map.put("carBeans",carBeans);
        }
        else if (text.contains("昼伏夜出")){
            Pattern pattern = Pattern.compile("(.*?)是否是昼伏夜出(.*?)");
            Matcher matcher = pattern.matcher(text);
            matcher.find();
            try {
                carNum = matcher.group(1).toUpperCase();
            } catch (Exception e) {
                System.out.println("nlp无法获取carNum，使用默认津N88888");
            }
            Object carBeans = carSearchList("nlp5", carNum, false, "", false, roadMapper);
            map.put("nlpType","nlp5");
            map.put("carBeans",carBeans);
        }
        else if (text.contains("伴随车辆")){
            System.out.println(text);
            Pattern pattern = Pattern.compile("(.*?)是否有伴随车辆");
            Matcher matcher = pattern.matcher(text);
            matcher.find();
            try {
                carNum = matcher.group(1).toUpperCase();
            } catch (Exception e) {
                System.out.println("nlp无法获取carNum，使用默认津N88888");
            }
            Object carBeans = carSearchList("nlp6", carNum, false, "", false, roadMapper);
            map.put("nlpType","nlp6");
            map.put("carBeans",carBeans);
        }
        else if (text.contains("过车记录")){
            Pattern pattern = Pattern.compile("(.*?)的过车记录");
            Matcher matcher = pattern.matcher(text);
            matcher.find();
            try {
                roadNum = matcher.group(1);
            } catch (Exception e) {
                System.out.println("nlp无法获取roadNum，使用默认津N88888");
            }
            Object roadBeans = roadService.getRoadByFilter("1", roadNum, false, "", roadMapper, 1);
            map.put("nlpType","nlp7");
            map.put("carBeans",roadBeans);
        }
        else {
            map.put("nlpType","nlp-1");
            map.put("carBeans",new String[0]);
        }

        return map;
    }



}
