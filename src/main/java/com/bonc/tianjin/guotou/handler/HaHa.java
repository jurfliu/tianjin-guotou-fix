package com.bonc.tianjin.guotou.handler;

import com.bonc.tianjin.guotou.GuotouJarStart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
public class HaHa  implements Executable{
    private static Logger logger = LoggerFactory.getLogger(GuotouJarStart.class);
//java -jar tianjin-guotou-fix-1.0-SNAPSHOT.jar com.bonc.tianjin.guotou.Handler.HaHa
    @Override
    public void execute(String[] args) throws ParseException {
        for(int k=0;k<10;k++){
            System.out.println("haaaaaaaaaaaaaaaaaaaa");
            logger.info("aaaaaaaaaaaaaaaaaaaa,，执行第"+k+"次");
        }

    }
}
