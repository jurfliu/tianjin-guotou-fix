package com.bonc.tianjin.guotou.model;


import java.io.Serializable;
import java.util.Date;

//@Document(indexName = "#{esMyConfig.indexName}",type="#{esMyConfig.typeName}")
public class CalcDpStore implements Serializable {
    private String METERNAME;
    private Date DATETIME;
    private Double READING;

    public CalcDpStore() {
    }


    public String getMETERNAME() {
        return METERNAME;
    }

    public void setMETERNAME(String METERNAME) {
        this.METERNAME = METERNAME;
    }

    public Date getDATETIME() {
        return DATETIME;
    }

    public void setDATETIME(Date DATETIME) {
        this.DATETIME = DATETIME;
    }

    public Double getREADING() {
        return READING;
    }

    public void setREADING(Double READING) {
        this.READING = READING;
    }
}
