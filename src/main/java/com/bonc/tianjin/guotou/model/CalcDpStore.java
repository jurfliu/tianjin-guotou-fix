package com.bonc.tianjin.guotou.model;


import java.io.Serializable;
import java.util.Date;
/**
* @author liujianfu
* @description       es的type和index
* @date 2020/7/31 0031 下午 4:58
* @param
* @return
*/

//@Document(indexName = "#{esMyConfig.indexName}",type="#{esMyConfig.typeName}")
public class CalcDpStore implements Serializable {
    private String METERNAME;
    private long DATETIME;
    private Double READING;

    public CalcDpStore() {
    }


    public String getMETERNAME() {
        return METERNAME;
    }

    public void setMETERNAME(String METERNAME) {
        this.METERNAME = METERNAME;
    }

    public long getDATETIME() {
        return DATETIME;
    }

    public void setDATETIME(long DATETIME) {
        this.DATETIME = DATETIME;
    }

    public Double getREADING() {
        return READING;
    }

    public void setREADING(Double READING) {
        this.READING = READING;
    }
}
