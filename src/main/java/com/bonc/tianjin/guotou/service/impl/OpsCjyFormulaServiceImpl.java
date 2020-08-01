package com.bonc.tianjin.guotou.service.impl;


import com.bonc.tianjin.guotou.config.EsParamConfig;
import com.bonc.tianjin.guotou.dao.OpsCjyFormulaDao;
import com.bonc.tianjin.guotou.model.OpsCjyFormula;
import com.bonc.tianjin.guotou.service.OpsCjyFormulaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "opsCjyFormulaService")
public class OpsCjyFormulaServiceImpl implements OpsCjyFormulaService {
    @Autowired
    private OpsCjyFormulaDao opsCjyFormulaDao;
    /**
     * 查询公式表中的任务模板
     * @return
     */
    @Override
    public List<OpsCjyFormula> queryInfoList() {
        List<OpsCjyFormula> ocfList=    opsCjyFormulaDao.queryInfoList();
        for(OpsCjyFormula ocf :ocfList){
            //System.out.println("ID:"+ocf.getFormulaId()+"create:"+ocf.getCreateTime());
           // System.out.println("UPDATE:"+ocf.getUpdateTime());
            ocf.setCreateTime(ocf.getCreateTime().replace(".0",""));
            ocf.setUpdateTime(ocf.getUpdateTime().replace(".0",""));
        }
        return ocfList;
    }
}
