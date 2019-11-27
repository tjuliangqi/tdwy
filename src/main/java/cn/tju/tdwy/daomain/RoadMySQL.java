package cn.tju.tdwy.daomain;

public class RoadMySQL {
    private int id;
    private String roadNum;
    private String roadText;
    private String lng;
    private String lat;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoadNum() {
        return roadNum;
    }

    public void setRoadNum(String roadNum) {
        this.roadNum = roadNum;
    }

    public String getRoadText() {
        return roadText;
    }

    public void setRoadText(String roadText) {
        this.roadText = roadText;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}
