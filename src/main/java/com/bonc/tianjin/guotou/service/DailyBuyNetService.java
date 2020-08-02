package com.bonc.tianjin.guotou.service;

import com.bonc.tianjin.guotou.model.DailyUnionBuyFormula;

import java.util.List;

public interface DailyBuyNetService {
    //查询所有的日购网公式
    public List<DailyUnionBuyFormula> queryDailyBuyDataList();
    //记录上一次执行日购网公式的时间
    public void updateLastBuyNetCalulateTime(DailyUnionBuyFormula dubf);
}
