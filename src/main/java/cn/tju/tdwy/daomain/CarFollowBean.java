package cn.tju.tdwy.daomain;

import java.util.List;
import java.util.Map;

public class CarFollowBean {

    private String day;
    private String carA;
    private String carB;
    private String carANumType;
    private String carBNumType;
    private String carANumColor;
    private String carBNumColor;
    private String carAColor;
    private String carBColor;
    private String carAType;
    private String carBType;
    private String carAPicUrl;
    private String carBPicUrl;
    private List carAList;
    private List carBList;
    private int count;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
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

    public String getCarANumType() {
        return carANumType;
    }

    public void setCarANumType(String carANumType) {
        this.carANumType = carANumType;
    }

    public String getCarBNumType() {
        return carBNumType;
    }

    public void setCarBNumType(String carBNumType) {
        this.carBNumType = carBNumType;
    }

    public String getCarANumColor() {
        return carANumColor;
    }

    public void setCarANumColor(String carANumColor) {
        this.carANumColor = carANumColor;
    }

    public String getCarBNumColor() {
        return carBNumColor;
    }

    public void setCarBNumColor(String carBNumColor) {
        this.carBNumColor = carBNumColor;
    }

    public String getCarAColor() {
        return carAColor;
    }

    public void setCarAColor(String carAColor) {
        this.carAColor = carAColor;
    }

    public String getCarBColor() {
        return carBColor;
    }

    public void setCarBColor(String carBColor) {
        this.carBColor = carBColor;
    }

    public String getCarAType() {
        return carAType;
    }

    public void setCarAType(String carAType) {
        this.carAType = carAType;
    }

    public String getCarBType() {
        return carBType;
    }

    public void setCarBType(String carBType) {
        this.carBType = carBType;
    }

    public String getCarAPicUrl() {
        return carAPicUrl;
    }

    public void setCarAPicUrl(String carAPicUrl) {
        this.carAPicUrl = carAPicUrl;
    }

    public String getCarBPicUrl() {
        return carBPicUrl;
    }

    public void setCarBPicUrl(String carBPicUrl) {
        this.carBPicUrl = carBPicUrl;
    }

    public List getCarAList() {
        return carAList;
    }

    public void setCarAList(List carAList) {
        this.carAList = carAList;
    }

    public List getCarBList() {
        return carBList;
    }

    public void setCarBList(List carBList) {
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
                "day='" + day + '\'' +
                ", carA='" + carA + '\'' +
                ", carB='" + carB + '\'' +
                ", carANumType='" + carANumType + '\'' +
                ", carBNumType='" + carBNumType + '\'' +
                ", carANumColor='" + carANumColor + '\'' +
                ", carBNumColor='" + carBNumColor + '\'' +
                ", carAColor='" + carAColor + '\'' +
                ", carBColor='" + carBColor + '\'' +
                ", carAType='" + carAType + '\'' +
                ", carBType='" + carBType + '\'' +
                ", carAPicUrl='" + carAPicUrl + '\'' +
                ", carBPicUrl='" + carBPicUrl + '\'' +
                ", carAList=" + carAList +
                ", carBList=" + carBList +
                ", count=" + count +
                '}';
    }


}
