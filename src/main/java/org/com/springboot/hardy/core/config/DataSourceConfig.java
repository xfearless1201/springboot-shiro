package org.com.springboot.hardy.core.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.com.springboot.hardy.core.datasource.DataBase;
import org.com.springboot.hardy.core.datasource.DynamicDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 
 * @ClassName DataSourceConfig
 * @Description 数据源配置类
 * @author Hardy
 * @Date 2018年12月20日 下午3:16:46
 * @version 1.0.0
 */
@Configuration
@MapperScan(basePackages = "org.com.springboot.hardy.core.mapper")
public class DataSourceConfig {
    //xml文件
    static final String MAPPER_LOCATION = "classpath:mapper/*.xml";
    
    @Value("${jdbc.master.data.driver}")
    private String masterDriverClassName;

    @Value("${jdbc.master.data.url}")
    private String masterUrl;

    @Value("${jdbc.master.data.username}")
    private String masterUsername;

    @Value("${jdbc.master.data.password}")
    private String masterPassword;
    
    @Value("${jdbc.slave.data.driver}")
    private String slaveDriverClassName;

    @Value("${jdbc.slave.data.url}")
    private String slaveUrl;

    @Value("${jdbc.slave.data.username}")
    private String slaveUsername;

    @Value("${jdbc.slave.data.password}")
    private String slavePassword;

    @Bean(name="masterDataSource",initMethod = "init")
    public DataSource masterDataSource() {
        DruidDataSource masterDataSource = new DruidDataSource();
        masterDataSource.setUsername(masterUsername);
//        masterDataSource.setPassword(masterPassword);
        masterDataSource.setDriverClassName(masterDriverClassName);
        masterDataSource.setUrl(masterUrl);
        masterDataSource.setInitialSize(5);
        masterDataSource.setMinIdle(0);
        masterDataSource.setMaxActive(100);
        masterDataSource.setMaxWait(100000);
        return masterDataSource;
    }
    
    @Bean(name="slaveDataSource",initMethod = "init")
    public DataSource slaveDataSource() {
        DruidDataSource slaveDataSource = new DruidDataSource();
        slaveDataSource.setUsername(slaveUsername);
//        slaveDataSource.setPassword(masterPassword);
        slaveDataSource.setDriverClassName(slavePassword);
        slaveDataSource.setUrl(slaveUrl);
        slaveDataSource.setInitialSize(5);
        slaveDataSource.setMinIdle(0);
        slaveDataSource.setMaxActive(100);
        slaveDataSource.setMaxWait(100000);
        return slaveDataSource;
    }

    @Bean
    @Primary
    public DynamicDataSource dataSource(@Qualifier("masterDataSource") DataSource masterDataSource,
                                        @Qualifier("slaveDataSource") DataSource slaveDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
        targetDataSources.put(DataBase.MASTAR_DATA.getType(), masterDataSource);
        targetDataSources.put(DataBase.SLAVE_DATA.getType(), slaveDataSource);
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(masterDataSource);
        return dynamicDataSource;
    }

    /**
     * 根据数据源创建SqlSessionFactory
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(DynamicDataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);//指定数据源(这个必须有，否则报错)
        // 下边两句仅仅用于*.xml文件，如果整个持久层操作不需要使用到xml文件的话（只用注解就可以搞定），则不加
        sqlSessionFactory.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));//
        sqlSessionFactory.setConfigLocation(
                new PathMatchingResourcePatternResolver().getResource("classpath:conf/mybatis-config.xml"));
        return sqlSessionFactory.getObject();
    }

    /**
     * 配置事务管理器
     */
    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) throws Exception {
        return new DataSourceTransactionManager(dataSource);
    }
}
