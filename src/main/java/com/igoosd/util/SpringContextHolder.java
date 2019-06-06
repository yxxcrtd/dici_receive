package com.igoosd.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

/**
 * 2017/8/10.
 */
@Component
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext APPLICATIONCONTEXT;

    /**
     * 根据类型获取bean
     * @param clazz
     * @param <T>
     * @return
     */
    public static final <T> T getBean(Class<T> clazz) {
        return APPLICATIONCONTEXT.getBean(clazz);
    }

    public static final<T> Collection<T> getBeans(Class<T> clazz){
        Map<String,T> map =APPLICATIONCONTEXT.getBeansOfType(clazz);
        if(null != map){
            return map.values();
        }
        return null;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        APPLICATIONCONTEXT = applicationContext;
    }
}
