package cn.tju.tdwy;

import java.util.HashMap;
import java.util.Map;

public class Config {

    /**
     * 下面是关于ES的配置
     */

    public static final String ES_IP = "192.168.199.31";
    public static final String ROADINDEX = "tdwy_road";
    public static final String CONFERENCEINDEX = "conference";
    public static final String CAR_INDEX = "tdwy_car3";
    public static final String TDWY_INDEX = "tdwy";
    public static final String NIGHT_OUT_INDEX = "tdwy_night_out";
    public static final String CITY_IN_OUT_INDEX = "city_in_out";
    public static final String FOLLOW_INDEX = "tdwy_follow";
    public static int PORT = 9200;
    public static final String TDWYINDEX = "tdwy";
    public Map<String,String> carType = new HashMap<>();
    public Map<String,String> carColor = new HashMap<>();
    public Map<String,String> carNumColor = new HashMap<>();
    public Map<String,String> carNumType = new HashMap<>();
    public Map<String,String> ipMap = new HashMap<>();
    public Config(){
        carType.put("0","未知车型");
        carType.put("1","大型客车");
        carType.put("2","轿车");
        carType.put("3","中型货车");
        carType.put("4","面包车");
        carType.put("5","大货车");
        carType.put("6","小货车");
        carType.put("7","两轮车");
        carType.put("8","行人");
        carType.put("9","SUV");
        carType.put("10","中型客车");
        carType.put("11","挂车");
        carType.put("12","出租车");
        carType.put("13","摩托车");
        carType.put("10001","两厢轿车");
        carType.put("10003","轿跑");
        carType.put("10004","小型轿车");
        carType.put("10005","微型轿车");
        carType.put("10006","MPV");
        carType.put("10011","微型面包车");
        carType.put("10014","油罐车");
        carType.put("10015","吊车");
        carType.put("10016","渣土车");
        carType.put("10018","皮卡");
        carType.put("10019","微卡");
        carType.put("10021","三轮车");
        carType.put("10028","SUV/MPV");
        carColor.put("1","白色");
        carColor.put("2","灰色");
        carColor.put("3","黄色");
        carColor.put("4","粉色");
        carColor.put("5","红色");
        carColor.put("6","紫色");
        carColor.put("7","绿色");
        carColor.put("8","蓝色");
        carColor.put("9","棕色");
        carColor.put("10","黑色");
        carColor.put("11","银色");
        carColor.put("12","青色");
        carColor.put("26","未知");
        carNumColor.put("0","白牌");
        carNumColor.put("1","黄牌");
        carNumColor.put("2","蓝牌");
        carNumColor.put("3","黑牌");
        carNumColor.put("4","绿牌");
        carNumColor.put("99","未知");
        carNumColor.put("51","黄绿牌");
        carNumColor.put("52","渐变绿牌");
        carNumType.put("01","大型汽车");
        carNumType.put("02","小型汽车");
        carNumType.put("03","使馆汽车");
        carNumType.put("04","领馆汽车");
        carNumType.put("05","境外汽车");
        carNumType.put("06","外籍汽车");
        carNumType.put("07","两、三轮摩托车");
        carNumType.put("08","轻便摩托车");
        carNumType.put("09","使馆摩托车");
        carNumType.put("10","领馆摩托车");
        carNumType.put("11","境外摩托车");
        carNumType.put("12","外籍摩托车");
        carNumType.put("13","农用运输车");
        carNumType.put("14","拖拉机");
        carNumType.put("15","挂车");
        carNumType.put("16","教练汽车");
        carNumType.put("17","教练摩托车");
        carNumType.put("18","试验汽车");
        carNumType.put("19","试验摩托车");
        carNumType.put("20","临时入境汽车");
        carNumType.put("21","临时入境摩托车");
        carNumType.put("22","临时行驶车");
        carNumType.put("23","警用汽车");
        carNumType.put("24","警用摩托");
        carNumType.put("99","电动自行车");
        carNumType.put("00","未知");
        carNumType.put("25","新能源车");
        carNumType.put("-1","未选择");
        ipMap.put("17.56.42.96","tdwy/img96");
        ipMap.put("17.56.42.97","tdwy/img97");
        ipMap.put("17.56.42.98","tdwy/img98");
        ipMap.put("17.56.42.99","tdwy/img99");
        ipMap.put("17.56.42.100","tdwy/img100");
        ipMap.put("17.56.42.101","tdwy/img101");
        ipMap.put("17.56.42.102","tdwy/img102");
        ipMap.put("17.56.42.103","tdwy/img103");
        ipMap.put("17.56.42.104","tdwy/img104");
        ipMap.put("17.56.42.105","tdwy/img105");
        ipMap.put("17.56.42.106","tdwy/img106");
        ipMap.put("17.56.42.107","tdwy/img107");



    }
}


