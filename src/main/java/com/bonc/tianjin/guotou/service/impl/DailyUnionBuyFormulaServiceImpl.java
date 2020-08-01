package com.bonc.tianjin.guotou.service.impl;

import com.bonc.tianjin.guotou.dao.DailyUnionBuyFormulaDao;
import com.bonc.tianjin.guotou.model.DailyUnionBuyFormula;
import com.bonc.tianjin.guotou.service.DailyUnionBuyFormulaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "ailyUnionBuyFormulaService")
public class DailyUnionBuyFormulaServiceImpl implements DailyUnionBuyFormulaService {
    @Autowired
    private DailyUnionBuyFormulaDao dailyUnionBuyFormulaDao;

    /**
     * 查询需要分析的日购网，日并网公式
     * @return
     */
    public List<DailyUnionBuyFormula>  queryDailyUnionBuyFormulaList(){
       return  dailyUnionBuyFormulaDao.queryDailyUnionBuyFormulaList();
    }
    /**
    * @author liujianfu
    * @description      记录上一天最大电量值的执行时间
    * @date 2020/8/1 0001 上午 11:16
    * @param [id]
    * @return void
    */

    @Override
    public void updateYesterdayMaxPowerTime(DailyUnionBuyFormula dubf) {
        System.out.println("update:"+dubf.getFormuaPointId()+" dubf:"+dubf.getTotalYesterdayMaxPowerTime());
         dailyUnionBuyFormulaDao.updateYesterdayMaxPowerTime(dubf);
    }
}
