package com.testinium.test.automation.abstracts;


import org.apache.log4j.Logger;

public abstract class TestAbstracts {
    final public Logger logger;

    public TestAbstracts(Class sourceClass){
        logger=Logger.getLogger(sourceClass);
    }
}
