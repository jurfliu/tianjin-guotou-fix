package com.bonc.tianjin.guotou.service.impl;

import com.bonc.tianjin.guotou.dao.DailyUnionBuyFormulaDao;
import com.bonc.tianjin.guotou.dao.DailyUnionNetDao;
import com.bonc.tianjin.guotou.model.DailyUnionBuyFormula;
import com.bonc.tianjin.guotou.service.DailyUnionNetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: DailyUnionNetServiceImpl
 * @Description: TODO
 * @Author: liujianfu
 * @Date: 2020/08/01 18:13:57
 * @Version: V1.0
 **/
@Service(value = "dailyUnionNetService")
public class DailyUnionNetServiceImpl implements DailyUnionNetService {
    @Autowired
    private DailyUnionNetDao dailyUnionNetDao;
    /**
    * @author liujianfu
    * @description      查询所有的日并网公式 
    * @date 2020/8/1 0001 下午 6:23
    * @param []        
    * @return java.util.List<com.bonc.tianjin.guotou.model.DailyUnionBuyFormula>
    */
    @Override
    public List<DailyUnionBuyFormula> queryDailyUnionDataList() {
        return dailyUnionNetDao.queryDailyUnionDataList();
    }
    /**
    * @author liujianfu
    * @description       记录上一次执行日并网公式的时间
    * @date 2020/8/1 0001 下午 6:23
    * @param [dubf]        
    * @return void
    */
    
    @Override
    public void updateLastUnionNetCalulateTime(DailyUnionBuyFormula dubf) {
         dailyUnionNetDao.updateLastUnionNetCalulateTime(dubf);
    }
}
