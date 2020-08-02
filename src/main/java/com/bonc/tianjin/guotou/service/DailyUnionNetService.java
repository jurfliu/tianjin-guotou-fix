package com.bonc.tianjin.guotou.service;

import com.bonc.tianjin.guotou.model.DailyUnionBuyFormula;

import java.util.List;

public interface DailyUnionNetService {
    //查询所有的日并网公式
    public List<DailyUnionBuyFormula> queryDailyUnionDataList();
    //记录上一次执行日并网公式的时间
    public void updateLastUnionNetCalulateTime(DailyUnionBuyFormula dubf);
}
