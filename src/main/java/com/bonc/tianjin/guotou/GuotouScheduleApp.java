package com.bonc.tianjin.guotou;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@MapperScan("com.bonc.tianjin.guotou.dao")
@EnableScheduling   //开启定时
public class GuotouScheduleApp
{
    public static void main( String[] args )
    {

        SpringApplication.run(GuotouScheduleApp.class, args);
        System.out.println("天津国投统计程序启动成功！！！！！！！");
    }
}
