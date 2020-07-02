package com.bonc.tianjin.guotou.dao;

import com.bonc.tianjin.guotou.model.OpsCjyFormula;

import java.util.List;

public interface OpsCjyFormulaDao {
    /**
     * @return
     */
    //查询公式表中的任务模板
    List<OpsCjyFormula> queryInfoList();
}
