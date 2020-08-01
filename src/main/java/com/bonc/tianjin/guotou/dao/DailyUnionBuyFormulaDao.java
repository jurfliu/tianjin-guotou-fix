package com.bonc.tianjin.guotou.dao;

import com.bonc.tianjin.guotou.model.DailyUnionBuyFormula;

import java.util.List;

public interface DailyUnionBuyFormulaDao {
    //查询需要分析的日购网，日并网公式
    public List<DailyUnionBuyFormula> queryDailyUnionBuyFormulaList();
    //记录上一天最大电量值的执行时间
    public void updateYesterdayMaxPowerTime(DailyUnionBuyFormula dailyUnionBuyFormula);
}
