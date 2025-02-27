package cn.tju.tdwy.daomain;

import java.util.List;
import java.util.Map;

public class CarBean {
    //private String day;
    private String carNum;
    private String carNumType;
    private String carNumColor;
    private String carColor;
    private String carType;
    private String picURL;
    private List fields_bind_time;


    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getCarNumType() {
        return carNumType;
    }

    public void setCarNumType(String carNumType) {
        this.carNumType = carNumType;
    }

    public String getCarNumColor() {
        return carNumColor;
    }

    public void setCarNumColor(String carNumColor) {
        this.carNumColor = carNumColor;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getPicURL() {
        return picURL;
    }

    public void setPicURL(String picURL) {
        this.picURL = picURL;
    }

    public List getFields_bind_time() {
        return fields_bind_time;
    }

    public void setFields_bind_time(List fields_bind_time) {
        this.fields_bind_time = fields_bind_time;
    }

    @Override
    public String toString() {
        return "CarBean{" +
                "carNum='" + carNum + '\'' +
                ", carNumType='" + carNumType + '\'' +
                ", carNumColor='" + carNumColor + '\'' +
                ", carColor='" + carColor + '\'' +
                ", carType='" + carType + '\'' +
                ", picURL='" + picURL + '\'' +
                ", fields_bind_time='" + fields_bind_time + '\'' +
                '}';
    }

}
