package com.geng.cpw.zlxsweb.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

//生命为java配置类
@Configuration
//开启spring事物支持
@EnableTransactionManagement
//配置mybatis mapper扫描
@MapperScan(value = {"com.geng.cpw.zlxsweb.dao.mapper","com.geng.cpw.zlxsweb.mapper"})
public class DataBaseConfiguration implements EnvironmentAware{

    private Environment environment;
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    //配置druidDataSource
    @Bean(name = "druidDataSource" ,initMethod = "init",destroyMethod = "close")
    public DruidDataSource dataSource(){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(environment.getProperty("spring.datasource.driverClassName"));
        druidDataSource.setUrl(environment.getProperty("spring.datasource.url"));
        druidDataSource.setUsername(environment.getProperty("spring.datasource.username"));
        druidDataSource.setPassword(environment.getProperty("spring.datasource.password"));
        druidDataSource.setMaxActive(20);
        druidDataSource.setInitialSize(1);
        druidDataSource.setMaxWait(60000);
        druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
        druidDataSource.setMinEvictableIdleTimeMillis(300000);
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);
        druidDataSource.setPoolPreparedStatements(true);
        druidDataSource.setMaxOpenPreparedStatements(20);
        druidDataSource.setValidationQuery("SELECT 'x'");
        return druidDataSource;
    }

    //配置mybatis sqlsessionFactory
    @Bean public SqlSessionFactory sqlSessionFactory(@Qualifier("druidDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        //配置mybatis分页插件
        Properties props = new Properties();
        props.setProperty("offsetAsPageNum", "false");
        props.setProperty("rowBoundsWithCount", "true");
        props.setProperty("pageSizeZero", "false");
        props.setProperty("reasonable", "false");
        props.setProperty("params", "pageNum=pageNum;pageSize=pageSize;count=countSql;reasonable=reasonable;pageSizeZero=pageSizeZero");
        props.setProperty("supportMethodsArguments","false");
        props.setProperty("autoRuntimeDialect","true");

        PageInterceptor pageInterceptor = new PageInterceptor();
        pageInterceptor.setProperties(props);
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{pageInterceptor});
        //配置mybatis xml文件路径
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] mapperLocations1=resolver.getResources("classpath*:com/geng/cpw/zlxsweb/mapping/*.xml");
        Resource[] mapperLocations2=resolver.getResources("classpath*:com/geng/cpw/zlxsweb/dao/mapping/*.xml");
        Resource[] mapperLocations=new Resource[mapperLocations1.length+mapperLocations2.length];
        for(int i=0;i<mapperLocations1.length;i++){
            mapperLocations[i]=mapperLocations1[i];
        }
        for(int i=0;i<mapperLocations2.length;i++){
            mapperLocations[mapperLocations1.length+i]=mapperLocations2[i];
        }
        sqlSessionFactoryBean.setMapperLocations(mapperLocations);
        return sqlSessionFactoryBean.getObject();
    }

    //配置spring 事物管理器
    @Bean public PlatformTransactionManager transactionManager() throws SQLException {
        return new DataSourceTransactionManager(dataSource());
    }

}
