package com.bonc.tianjin.guotou.controller;

import com.bonc.tianjin.guotou.service.OpsCjyFormulaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/testOps")
public class OpsCjyFormulaController
{
    @Autowired
    private OpsCjyFormulaService opsCjyFormulaService;
    @ResponseBody
    @GetMapping("/all")
    public Object findAllFormula(){
        System.out.println("进入contoller查询......");
        return opsCjyFormulaService.queryInfoList();
    }

}
