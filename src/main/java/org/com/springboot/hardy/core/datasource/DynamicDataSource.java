package org.com.springboot.hardy.core.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 
 * @ClassName DynamicDataSource
 * @Description 动态获取数据源
 * @author Hardy
 * @Date 2018年12月20日 下午3:17:44
 * @version 1.0.0
 */
public class DynamicDataSource extends AbstractRoutingDataSource{

    private static final ThreadLocal<String> threadDatabase = new ThreadLocal<String>();
    
    public static String getDataSource(){
        return threadDatabase.get();
    }

    public static void setDataSource(String dataSourceType){
        threadDatabase.set(dataSourceType);
    }
    
    public static void clearDataSource() {
        threadDatabase.remove();
    }
    
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSource.getDataSource();
    }

}
