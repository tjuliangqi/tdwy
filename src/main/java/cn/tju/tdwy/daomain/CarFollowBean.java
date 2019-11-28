package cn.tju.tdwy.daomain;

import org.json.JSONObject;

public class CarFollowBean {

    private String time;
    private String carA;
    private String carB;
    private JSONObject carAList;
    private JSONObject carBList;
    private int count;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCarA() {
        return carA;
    }

    public void setCarA(String carA) {
        this.carA = carA;
    }

    public String getCarB() {
        return carB;
    }

    public void setCarB(String carB) {
        this.carB = carB;
    }

    public JSONObject getCarAList() {
        return carAList;
    }

    public void setCarAList(JSONObject carAList) {
        this.carAList = carAList;
    }

    public JSONObject getCarBList() {
        return carBList;
    }

    public void setCarBList(JSONObject carBList) {
        this.carBList = carBList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "CarFollowBean{" +
                "time='" + time + '\'' +
                ", carA='" + carA + '\'' +
                ", carB='" + carB + '\'' +
                ", carAList='" + carAList + '\'' +
                ", carBList='" + carBList + '\'' +
                ", count=" + count +
                '}';
    }


}
