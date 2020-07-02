package com.bonc.tianjin.guotou.handler;

import java.text.ParseException;

/**
 *
 */
public interface Executable  {
    void execute(String[] args) throws ParseException, InterruptedException;

}
