package com.bonc.tianjin.guotou.model;

import java.util.Date;

/**
 * @ClassName: DailyUnionBuyFormula
 * @Description: 计算日并网、日购网的信息
 * @Author: liujianfu
 * @Date: 2020/07/31 16:59:00
 * @Version: V1.0
 **/
public class DailyUnionBuyFormula {
    /**
    * 主键id
    */

   private int formuaPointId;
   /**
    * 公式名称
   */

   private  String formulaName;
   /**
   *
   设备节点名称
   */
   private String deviceNodePoint;
    /**
    * 昨天最大值公式名称
    */
    private String yesterdayMaxFormulaName;

    /**
    * 计算时的公式名称
    */
    private String calculateFormulaName;
   /**
   * 查询es的索引的名称
   */
    private String esIndex;
    /**
    * 实时设备节点需要查询es的type名称
    */
    private String deviceNodeEsType;
    /**
    * 计算统计结果后查询es的type名称
    */
    private String totalEsType;
    /**
    * 公式类型，1为日并网，2为日购网',
    */
     private int formulaType;
     /**
     * 0默认值表示可以执行，1表示不可以执行
     */
     private int calcualteFormulaState;
   /**
   * 执行统计上一天最大电量值的时间
   */
     private Date totalYesterdayMaxPowerTime;
     /**
     * 上一次执行计算公式的时间
     */

     private Date lastCalculateFromulaTime;
     /**
     * 备注说明
     */
     private String remark;
     /**
     *常量系数
     */
      private int constantWeight;
    public DailyUnionBuyFormula() {
    }

    public DailyUnionBuyFormula(int formuaPointId, String formulaName, String deviceNodePoint, String yesterdayMaxFormulaName, String calculateFormulaName, String esIndex, String deviceNodeEsType, String totalEsType, int formulaType, int calcualteFormulaState, Date totalYesterdayMaxPowerTime, Date lastCalculateFromulaTime, String remark, int constantWeight) {
        this.formuaPointId = formuaPointId;
        this.formulaName = formulaName;
        this.deviceNodePoint = deviceNodePoint;
        this.yesterdayMaxFormulaName = yesterdayMaxFormulaName;
        this.calculateFormulaName = calculateFormulaName;
        this.esIndex = esIndex;
        this.deviceNodeEsType = deviceNodeEsType;
        this.totalEsType = totalEsType;
        this.formulaType = formulaType;
        this.calcualteFormulaState = calcualteFormulaState;
        this.totalYesterdayMaxPowerTime = totalYesterdayMaxPowerTime;
        this.lastCalculateFromulaTime = lastCalculateFromulaTime;
        this.remark = remark;
        this.constantWeight = constantWeight;
    }

    public int getFormuaPointId() {
        return formuaPointId;
    }

    public void setFormuaPointId(int formuaPointId) {
        this.formuaPointId = formuaPointId;
    }

    public String getFormulaName() {
        return formulaName;
    }

    public void setFormulaName(String formulaName) {
        this.formulaName = formulaName;
    }

    public String getDeviceNodePoint() {
        return deviceNodePoint;
    }

    public void setDeviceNodePoint(String deviceNodePoint) {
        this.deviceNodePoint = deviceNodePoint;
    }

    public String getYesterdayMaxFormulaName() {
        return yesterdayMaxFormulaName;
    }

    public void setYesterdayMaxFormulaName(String yesterdayMaxFormulaName) {
        this.yesterdayMaxFormulaName = yesterdayMaxFormulaName;
    }

    public String getCalculateFormulaName() {
        return calculateFormulaName;
    }

    public void setCalculateFormulaName(String calculateFormulaName) {
        this.calculateFormulaName = calculateFormulaName;
    }

    public String getEsIndex() {
        return esIndex;
    }

    public void setEsIndex(String esIndex) {
        this.esIndex = esIndex;
    }

    public String getDeviceNodeEsType() {
        return deviceNodeEsType;
    }

    public void setDeviceNodeEsType(String deviceNodeEsType) {
        this.deviceNodeEsType = deviceNodeEsType;
    }

    public String getTotalEsType() {
        return totalEsType;
    }

    public void setTotalEsType(String totalEsType) {
        this.totalEsType = totalEsType;
    }

    public int getFormulaType() {
        return formulaType;
    }

    public void setFormulaType(int formulaType) {
        this.formulaType = formulaType;
    }

    public int getCalcualteFormulaState() {
        return calcualteFormulaState;
    }

    public void setCalcualteFormulaState(int calcualteFormulaState) {
        this.calcualteFormulaState = calcualteFormulaState;
    }

    public Date getTotalYesterdayMaxPowerTime() {
        return totalYesterdayMaxPowerTime;
    }

    public void setTotalYesterdayMaxPowerTime(Date totalYesterdayMaxPowerTime) {
        this.totalYesterdayMaxPowerTime = totalYesterdayMaxPowerTime;
    }

    public Date getLastCalculateFromulaTime() {
        return lastCalculateFromulaTime;
    }

    public void setLastCalculateFromulaTime(Date lastCalculateFromulaTime) {
        this.lastCalculateFromulaTime = lastCalculateFromulaTime;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getConstantWeight() {
        return constantWeight;
    }

    public void setConstantWeight(int constantWeight) {
        this.constantWeight = constantWeight;
    }
}
