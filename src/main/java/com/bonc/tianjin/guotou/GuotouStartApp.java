package com.bonc.tianjin.guotou;

import com.bonc.tianjin.guotou.config.EsParamConfig;
import com.bonc.tianjin.guotou.handler.HaHa;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
//@SpringBootApplication
//@MapperScan("com.bonc.tianjin.guotou.dao")
public class GuotouStartApp implements CommandLineRunner
{
    @Autowired
    private EsParamConfig esParamConfig;
    @Autowired
    private HaHa haha;
    public static void main( String[] args )
    {

        SpringApplication.run(GuotouStartApp.class, args);

    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("esParamConfig:"+esParamConfig.getIndexName());
        System.out.println("天津国投修订程序服务启动成功！！！！！！！");
    }
}
