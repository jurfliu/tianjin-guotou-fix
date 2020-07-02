package com.bonc.tianjin.guotou.model;

import java.sql.Timestamp;
import java.util.Date;

public class OpsCjyFormula {
    private int formulaId;//公式ID
    private String formulaName;//公式名称
    private String formulaStr;//公式字符串
    private int orderNo;//顺序号
    private String inInffo;//计算输入信息
    private String outInfo;//计算输出信息
    private int outState;//计算结果状态
    private long outTime;//计算时间
    private int state;//状态。0无效，1有效
    private String createTime;//创建时间
    private String updateTime;//修改时间

    public OpsCjyFormula() {
    }

    public OpsCjyFormula(int formulaId, String formulaName, String formulaStr, int orderNo, String inInffo, String outInfo, int outState, long outTime, int state, String createTime, String updateTime) {
        this.formulaId = formulaId;
        this.formulaName = formulaName;
        this.formulaStr = formulaStr;
        this.orderNo = orderNo;
        this.inInffo = inInffo;
        this.outInfo = outInfo;
        this.outState = outState;
        this.outTime = outTime;
        this.state = state;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public int getFormulaId() {
        return formulaId;
    }

    public void setFormulaId(int formulaId) {
        this.formulaId = formulaId;
    }

    public String getFormulaName() {
        return formulaName;
    }

    public void setFormulaName(String formulaName) {
        this.formulaName = formulaName;
    }

    public String getFormulaStr() {
        return formulaStr;
    }

    public void setFormulaStr(String formulaStr) {
        this.formulaStr = formulaStr;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public String getInInffo() {
        return inInffo;
    }

    public void setInInffo(String inInffo) {
        this.inInffo = inInffo;
    }

    public String getOutInfo() {
        return outInfo;
    }

    public void setOutInfo(String outInfo) {
        this.outInfo = outInfo;
    }

    public int getOutState() {
        return outState;
    }

    public void setOutState(int outState) {
        this.outState = outState;
    }

    public long getOutTime() {
        return outTime;
    }

    public void setOutTime(long outTime) {
        this.outTime = outTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
