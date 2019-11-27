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
    public static final String INDEX_AFFILIATIONS = "affiliations";
    public static final String AUTHORINDEX = "authors";
    public static int PORT = 9200;
    public Map<String,String> roadNum = new HashMap<>();
    public Config(){
        roadNum.put("17056001031","7146洞庭路与无名路交口东北角");
        roadNum.put("1201116017030","0025泰兴路与欣达街交口");
        roadNum.put("1201116023070","3017第三大街与广场西路");
        roadNum.put("12011600001211050016","泰达大街洞庭路东向西");
        roadNum.put("120116001013","2770东海路与洞庭路交口东南角");
        roadNum.put("120116001017","7102第十三大街与清梅园交口路西北侧");
        roadNum.put("120116001021","7143洞庭路与半圆路南交口西南角");
        roadNum.put("120116001024","7144洞庭路与大学城半圆路北交口");
        roadNum.put("120116001028","7145洞庭路与无名路西南角");
        roadNum.put("120116001054","7101第十三大街与清梅园小区交口东侧");
        roadNum.put("120116001058","7103洞庭路与十三大街交口西南角");
        roadNum.put("120116001061","7104洞庭路与十三大街交口东北角");
        roadNum.put("120116001065","7105第十三大街与大学城东半圆交口");
        roadNum.put("120116001068","7106第十三大街与东半圆交口北侧东向");
        roadNum.put("120116001072","7107黄海与十三大街西南角");
        roadNum.put("120116001075","7108黄海与十三大街东北角");
        roadNum.put("120116001078","7141洞庭路与大学城半圆路南交口");
        roadNum.put("120116001081","7142洞庭与大学城半圆南西南角");
        roadNum.put("120116001093","7337大学城半圆路与小区路");
        roadNum.put("120116001096","7338区大学城半圆路与小区路");
        roadNum.put("120116001099","7339大学城半圆路与清兰园");
        roadNum.put("120116001102","7340大学城半圆路与清兰园");
        roadNum.put("120116001112","7109第十三大街与睦宁路");
        roadNum.put("120116001115","7110十三大街与睦宁东北角");
        roadNum.put("120116001118","7215海星街与黄海路交口");
        roadNum.put("120116001121","7216海星街与黄海路交口");
        roadNum.put("120116001124","7217海星街与睦宁路");
        roadNum.put("120116001128","7218海星街与睦宁路");
        roadNum.put("120116001131","7221海云街与黄海路");
        roadNum.put("120116001134","7222海云街与黄海路");
        roadNum.put("120116001137","7223海云大街与睦宁路");
        roadNum.put("120116001141","7224海云大街与睦宁路");
        roadNum.put("120116001144","7225海川街与黄海路");
        roadNum.put("120116001147","7226海川街与黄海路");
        roadNum.put("120116001152","7413南海路与科大北侧路");
        roadNum.put("120116001155","7414东海北路与黄海路交口西南角");
        roadNum.put("120116001164","7227中央大道高架桥下");
        roadNum.put("120116001167","7228西向段面");
        roadNum.put("120116001170","7220海星街与南海路");
        roadNum.put("120116001173","2709海云街与南海路交口");
        roadNum.put("120116002004","7125天津滨海新区第十二大街与睦宁路东北角");
        roadNum.put("120116002007","7126十二大街与睦宁西南角");
        roadNum.put("120116002010","7127十二大街与黄海西北角");
        roadNum.put("120116002014","7128天津滨海新区第十二大街与黄海路东南角");
        roadNum.put("120116002017","7129第十二大街与洞庭路");
        roadNum.put("120116002020","7130第十二大街与洞庭路");
        roadNum.put("120116002023","7139海通街与洞庭路");
        roadNum.put("120116002026","7140海通街与洞庭路");
        roadNum.put("120116002029","7211海通街与睦宁路");
        roadNum.put("120116002032","7212海通街与睦宁路");
    }
}


