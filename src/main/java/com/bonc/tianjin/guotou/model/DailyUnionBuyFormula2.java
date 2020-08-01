package com.bonc.tianjin.guotou.model;

import java.util.Date;

public class DailyUnionBuyFormula2 {
private  int formulaId;//公式id
private String formulaName;//公式名称
private String  formulaContext;//公式字符串内容
private long lastTotalTime;//上一次最后执行的时间
private int totalState;//执行状态。
private Date createTime;//创建时间
private Date modifyTime; //修改时间

    public DailyUnionBuyFormula2() {
    }

    public DailyUnionBuyFormula2(int formulaId, String formulaName, String formulaContext, long lastTotalTime, int totalState, Date createTime, Date modifyTime) {
        this.formulaId = formulaId;
        this.formulaName = formulaName;
        this.formulaContext = formulaContext;
        this.lastTotalTime = lastTotalTime;
        this.totalState = totalState;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
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

    public String getFormulaContext() {
        return formulaContext;
    }

    public void setFormulaContext(String formulaContext) {
        this.formulaContext = formulaContext;
    }

    public long getLastTotalTime() {
        return lastTotalTime;
    }

    public void setLastTotalTime(long lastTotalTime) {
        this.lastTotalTime = lastTotalTime;
    }

    public int getTotalState() {
        return totalState;
    }

    public void setTotalState(int totalState) {
        this.totalState = totalState;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
