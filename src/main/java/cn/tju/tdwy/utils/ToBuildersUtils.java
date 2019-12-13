package cn.tju.tdwy.utils;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.json.JSONException;

import java.util.*;

import static cn.tju.tdwy.utils.JsonToMapUtils.strToMap;
import static cn.tju.tdwy.utils.NLPUtils.textToMap;

public class ToBuildersUtils {

    public static SearchSourceBuilder queryTextToBuilder (String type, String value, boolean ifPrepara, String preparaString, Map<String,Integer> sizeMap) throws JSONException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder builder0;
        QueryBuilder builderAdd;

        // {0:车牌检索, 1:推荐, 2:图片检索, 3:自然语言检索, 4:首次入城车辆, 5:昼伏夜出车辆, 6:伴随车辆, 7:碰撞分析}
        if (ifPrepara == false || preparaString.equals("{}")) {
            if (type.equals("0")) {
                builder0 = QueryBuilders.matchQuery("carNum", value);
                searchSourceBuilder.from(0)
                        .size(sizeMap.get("false0"))
                        .query(builder0)
                        .sort("day",SortOrder.DESC);
            } else if (type.equals("1")) {
                builder0 = QueryBuilders.matchAllQuery();
                searchSourceBuilder.from(0)
                        .size(sizeMap.get("false1"))
                        .query(builder0)
                        .sort("day",SortOrder.DESC);
            } else if (type.equals("2")) {
                //builder0 = QueryBuilders.matchQuery("hashcode", value);
                //用车牌检索
                builder0 = QueryBuilders.matchQuery("carNum", value);
                searchSourceBuilder.from(0)
                        .size(sizeMap.get("false2"))
                        .query(builder0)
                        .sort("day",SortOrder.DESC);
            } else if (type.equals("3")) {
                //value = "津JHT808在2019年6月30号4点30分到2019年6月30号4点30分的行车记录";
                Map map = textToMap(value);
                System.out.println("自然语言先用模板处理代替");
                builder0 = QueryBuilders.boolQuery()
                        .must(QueryBuilders.matchQuery("carNum", map.get("carNum")))
                        .must(QueryBuilders.rangeQuery("accessTime")
                                .from(map.get("beginTime"))
                                .to(map.get("endTime")));
                searchSourceBuilder.from(0)
                        .size(sizeMap.get("false3"))
                        .sort("accessTime",SortOrder.ASC)
                        .query(builder0);
            } else if (type.equals("4")) {
                builder0 = QueryBuilders.rangeQuery("countIn")
                        .from(1);
                searchSourceBuilder.from(0)
                        .size(sizeMap.get("false4"))
                        .query(builder0)
                        .sort("day",SortOrder.DESC);
            } else if (type.equals("5")) {
                builder0 = QueryBuilders.rangeQuery("nightTimeNum")
                        .from(1);
                searchSourceBuilder.from(0)
                        .size(sizeMap.get("false5"))
                        .query(builder0)
                        .sort("day",SortOrder.DESC);
            } else if (type.equals("6")) {
                builder0 = QueryBuilders.rangeQuery("count")
                        .from(3);
                searchSourceBuilder.from(0)
                        .size(sizeMap.get("false6"))
                        .query(builder0);
            } else {
                // value = "['黑RJT353', '鲁A75020']";
                //System.out.println(value);
                String[] carArray = value.replace("[\"","").replace("\"]","").split("\",\"");
                List<String> carList= Arrays.asList(carArray);
                String carAName = "";
                String carBName = "";
                try {
                    carAName = carList.get(0);
                    carBName = carList.get(1);
                } catch (Exception e){
                    e.printStackTrace();
                }
                builder0 = QueryBuilders.boolQuery()
                        .should(QueryBuilders.boolQuery()
                                .must(QueryBuilders.matchQuery("carA",carAName))
                                .must(QueryBuilders.matchQuery("carB",carBName)))
                        .should(QueryBuilders.boolQuery()
                                .must(QueryBuilders.matchQuery("carA",carBName))
                                .must(QueryBuilders.matchQuery("carB",carAName)));
                searchSourceBuilder.from(0)
                        .size(sizeMap.get("false7"))
                        .query(builder0);
            }
        } else { // 只筛选0、1、2、4、5 其中0、2只筛选时间 // {0:车牌检索, 1:推荐, 2:图片检索, 3:自然语言检索, 4:首次入城车辆, 5:昼伏夜出车辆, 6:伴随车辆, 7:碰撞分析}
            if (type.equals("0")) {
                builder0 = QueryBuilders.matchQuery("carNum", value);
                builderAdd = addFilterBuilder(builder0, preparaString);
                searchSourceBuilder.from(0)
                        .size(sizeMap.get("true0"))
                        .query(builderAdd)
                        .sort("day",SortOrder.DESC);
            } else if (type.equals("1")) {
                builder0 = QueryBuilders.matchAllQuery();
                builderAdd = addFilterBuilder(builder0, preparaString);
                searchSourceBuilder.from(0)
                        .size(sizeMap.get("true1"))
                        .query(builderAdd)
                        .sort("day",SortOrder.DESC);
            } else if (type.equals("2")) {
                builder0 = QueryBuilders.matchAllQuery();
                builderAdd = addFilterBuilder(builder0, preparaString);
                searchSourceBuilder.from(0)
                        .size(sizeMap.get("true2"))
                        .query(builderAdd)
                        .sort("day",SortOrder.DESC);
            } else if (type.equals("4")) {
                builder0 = QueryBuilders.rangeQuery("countIn")
                        .from(1);
                builderAdd = addFilterBuilder(builder0, preparaString);
                searchSourceBuilder.from(0)
                        .size(sizeMap.get("true4"))
                        .query(builderAdd)
                        .sort("day",SortOrder.DESC);
            } else {
                builder0 = QueryBuilders.rangeQuery("nightTimeNum")
                        .from(1);
                builderAdd = addFilterBuilder(builder0, preparaString);
                searchSourceBuilder.from(0)
                        .size(sizeMap.get("true5"))
                        .query(builderAdd)
                        .sort("day",SortOrder.DESC);
            }
        }

        return searchSourceBuilder;
    }

    /**
     * @ description this function add builder0 and filterbuilder
     * @param builder0
     * @param preparaString
     * @return builderAdd
     * @throws JSONException
     */
    // 过滤carNumType，carNumColor，carColor，carBrand
    public static QueryBuilder addFilterBuilder(QueryBuilder builder0, String preparaString) throws JSONException {
        System.out.println("start Filter.....");
        Map map = strToMap(preparaString);
        // try filtrate carNumType
        try {
            String carNumType = map.get("号牌种类").toString().replace("[\"", "").replace("\"]", "");
            String[] carNumTypeList = carNumType.split("\",\"");
            QueryBuilder builder1 = QueryBuilders.matchQuery("carNumType", carNumTypeList[0]);
            for(int i =1;i<carNumTypeList.length;i++){
                builder1 = QueryBuilders.boolQuery()
                        .should(builder1)
                        .should(QueryBuilders.matchQuery("carNumType",carNumTypeList[i]));
            }
            builder0 = QueryBuilders.boolQuery()
                    .must(builder0)
                    .must(builder1);
        }catch (Exception e){
            System.out.println("No filtration carNumType");
        }
        // try filtrate carNumColor
        try {
            String carNumColor = map.get("号牌颜色").toString().replace("[\"", "").replace("\"]", "");
            String[] carNumColorList = carNumColor.split("\",\"");
            QueryBuilder builder1 = QueryBuilders.matchQuery("carNumColor", carNumColorList[0]);
            for(int i =1;i<carNumColorList.length;i++){
                builder1 = QueryBuilders.boolQuery()
                        .should(builder1)
                        .should(QueryBuilders.matchQuery("carNumColor",carNumColorList[i]));
            }
            builder0 = QueryBuilders.boolQuery()
                    .must(builder0)
                    .must(builder1);
        }catch (Exception e){
            System.out.println("No filtration carNumColor");
        }
        // try filtrate carColor
        try {
            String carColor = map.get("车身颜色").toString().replace("[\"", "").replace("\"]", "");
            String[] carColorList = carColor.split("\",\"");
            QueryBuilder builder1 = QueryBuilders.matchQuery("carColor", carColorList[0]);
            for(int i =1;i<carColorList.length;i++){
                builder1 = QueryBuilders.boolQuery()
                        .should(builder1)
                        .should(QueryBuilders.matchQuery("carColor",carColorList[i]));
            }
            builder0 = QueryBuilders.boolQuery()
                    .must(builder0)
                    .must(builder1);
        }catch (Exception e){
            System.out.println("No filtration carColor");
        }
        // try filtrate carType
        try {
            String carBrand = map.get("车辆品牌").toString().replace("[\"", "").replace("\"]", "");
            String[] carBrandList = carBrand.split("\",\"");
            QueryBuilder builder1 = QueryBuilders.matchQuery("carBrand", carBrandList[0]);
            for(int i =1;i<carBrandList.length;i++){
                builder1 = QueryBuilders.boolQuery()
                        .should(builder1)
                        .should(QueryBuilders.matchQuery("carBrand",carBrandList[i]));
            }
            builder0 = QueryBuilders.boolQuery()
                    .must(builder0)
                    .must(builder1);
        }catch (Exception e){
            System.out.println("No filtration carType");
        }
        // try filtrate day
        try {
            String day = map.get("day").toString().replace("[\"", "").replace("\"]", "");
            String[] dayList = day.split("\",\"");

            QueryBuilder builder1 = QueryBuilders.rangeQuery("day")
                    .from(dayList[0])
                    .to(dayList[1]);

            builder0 = QueryBuilders.boolQuery()
                    .must(builder0)
                    .must(builder1);
        }catch (Exception e){
            System.out.println("No filtration carType");
        }

        return builder0;
    }


}
