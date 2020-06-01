package Bean;

import java.io.Serializable;

/**
 * 日志信息实体类
 */
public class StartUpLog implements Serializable {
    private String mid;
    private String uid;
    private String appid;
    private String area;
    private String os;
    private String ch;
    private String logType;
    private String vs;
    private String logDate;
    private String logHour;
    private String logHourMinute;
    private Long ts;

    public StartUpLog() {
    }

    @Override
    public String toString() {
        return "StartUpLog{" +
                "mid='" + mid + '\'' +
                ", uid='" + uid + '\'' +
                ", appid='" + appid + '\'' +
                ", area='" + area + '\'' +
                ", os='" + os + '\'' +
                ", ch='" + ch + '\'' +
                ", logType='" + logType + '\'' +
                ", vs='" + vs + '\'' +
                ", logDate='" + logDate + '\'' +
                ", logHour='" + logHour + '\'' +
                ", logHourMinute='" + logHourMinute + '\'' +
                ", ts=" + ts +
                '}';
    }

    public StartUpLog(String mid, String uid, String appid, String area, String os, String ch, String logType, String vs, Long ts) {
        this.mid = mid;
        this.uid = uid;
        this.appid = appid;
        this.area = area;
        this.os = os;
        this.ch = ch;
        this.logType = logType;
        this.vs = vs;
        this.ts = ts;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getCh() {
        return ch;
    }

    public void setCh(String ch) {
        this.ch = ch;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getVs() {
        return vs;
    }

    public void setVs(String vs) {
        this.vs = vs;
    }

    public String getLogDate() {
        return logDate;
    }

    public void setLogDate(String logDate) {
        this.logDate = logDate;
    }

    public String getLogHour() {
        return logHour;
    }

    public void setLogHour(String logHour) {
        this.logHour = logHour;
    }

    public String getLogHourMinute() {
        return logHourMinute;
    }

    public void setLogHourMinute(String logHourMinute) {
        this.logHourMinute = logHourMinute;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }
}
