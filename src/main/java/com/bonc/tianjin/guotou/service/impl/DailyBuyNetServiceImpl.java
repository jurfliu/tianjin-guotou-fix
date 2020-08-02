package com.bonc.tianjin.guotou.service.impl;

import com.bonc.tianjin.guotou.dao.DailyBuyNetDao;
import com.bonc.tianjin.guotou.dao.DailyUnionNetDao;
import com.bonc.tianjin.guotou.model.DailyUnionBuyFormula;
import com.bonc.tianjin.guotou.service.DailyBuyNetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: DailyBuyNetServiceImpl
 * @Description: TODO
 * @Author: liujianfu
 * @Date: 2020/08/02 16:52:13
 * @Version: V1.0
 **/
@Service(value = "dailyBuyNetService")
public class DailyBuyNetServiceImpl  implements DailyBuyNetService {
    @Autowired
    private DailyBuyNetDao dailyBuyNetDao;
    /**
    * @author liujianfu
    * @description       记录上一次执行日购网公式的时间
    * @date 2020/8/2 0002 下午 4:54
    * @param []
    * @return java.util.List<com.bonc.tianjin.guotou.model.DailyUnionBuyFormula>
    */
    @Override
    public List<DailyUnionBuyFormula> queryDailyBuyDataList() {
        return dailyBuyNetDao.queryDailyBuyDataList();
    }
   /**
   * @author liujianfu
   * @description       记录上一次执行日购网公式的时间
   * @date 2020/8/2 0002 下午 4:55
   * @param [dubf]
   * @return void
   */

    @Override
    public void updateLastBuyNetCalulateTime(DailyUnionBuyFormula dubf) {
        dailyBuyNetDao.updateLastBuyNetCalulateTime(dubf);
    }
}
