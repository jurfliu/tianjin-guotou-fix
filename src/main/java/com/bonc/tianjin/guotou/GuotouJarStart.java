package com.bonc.tianjin.guotou;

import com.bonc.tianjin.guotou.handler.Executable;
import com.bonc.tianjin.guotou.annotaion.Expired;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.lang.annotation.Annotation;

/**
 * jar包形式的启动程序
 */
@SpringBootApplication(exclude = {FreeMarkerAutoConfiguration.class, DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = "com.bonc.tianjin.guotou")
@MapperScan(value = {"com.bonc.tianjin.guotou.dao"})
public class GuotouJarStart {
    private static Logger logger = LoggerFactory.getLogger(GuotouJarStart.class);
    public static void main(String args[]){
        logger.info("===============开始执行======================="+args);
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        try{
            ApplicationContext context= SpringApplication.run(GuotouJarStart.class);
            //第一个参数应该是要执行的class全名
            //后续参数为该方法参数
            if (args.length == 0) {
                logger.info("请指定要执行的类全名");
                System.exit(1);
            }
            String className = args[0];
            System.out.println("className:"+className);
            Class type = null;
            try {
                type = Class.forName(className);
            } catch (ClassNotFoundException e) {
                logger.info(className + "不存在");
                System.exit(1);
            }
            Object obj = context.getBean(type);
            if (obj == null || !Executable.class.isAssignableFrom(obj.getClass())) {
                logger.info("请指定 com.bonc.tianjin.guotou.handlers.Executable 接口的实现类");
                System.exit(1);
            }
            Executable target = (Executable) obj;
            Annotation[] annotations = target.getClass().getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == Expired.class) {
                    logger.info(className + " 已失效");
                    System.exit(1);
                }
            }
            if (args.length > 1) {
                String params[] = new String[args.length - 1];
                System.arraycopy(args, 1, params, 0, args.length - 1);
                target.execute(params);
            } else {
                target.execute(null);
            }
        }catch (Throwable e){
            logger.error("", e);
            System.exit(1);
        }
        logger.info("===============执行完毕ok=======================");
        System.exit(0);
    }
}
