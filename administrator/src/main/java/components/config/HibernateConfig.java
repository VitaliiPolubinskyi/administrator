package components.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.Properties;


@Configuration
@ComponentScan("components")
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
public class HibernateConfig {

    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource() throws PropertyVetoException {

        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(env.getProperty("hibernate.connection.driver_class"));
        dataSource.setJdbcUrl(env.getProperty("hibernate.connection.url"));
        dataSource.setUser(env.getProperty("hibernate.connection.username"));
        dataSource.setPassword(env.getProperty("hibernate.connection.password"));

        // Настройки C3P0
        dataSource.setMinPoolSize(env.getProperty("c3p0.minSize", Integer.class));
        dataSource.setMaxPoolSize(env.getProperty("c3p0.maxSize", Integer.class));
        dataSource.setAcquireIncrement(env.getProperty("c3p0.acquireIncrement", Integer.class));
        dataSource.setIdleConnectionTestPeriod(env.getProperty("c3p0.idleConnectionTestPeriod", Integer.class));
        dataSource.setAcquireRetryAttempts(env.getProperty("c3p0.acquireRetryAttempts", Integer.class));
        dataSource.setAcquireRetryDelay(env.getProperty("c3p0.acquireRetryDelay", Integer.class));
        dataSource.setCheckoutTimeout(env.getProperty("c3p0.timeout", Integer.class));
        dataSource.setMaxStatements(env.getProperty("c3p0.maxStatements", Integer.class));

        return dataSource;
    }

    @Bean
    public SessionFactory sessionFactory() throws PropertyVetoException, IOException {

        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("components.entities");
        sessionFactory.setHibernateProperties(hibernateProperties());
        sessionFactory.afterPropertiesSet();

        return sessionFactory.getObject();
    }

    @Bean
    public PhysicalNamingStrategy physicalNamingStrategy() {
        return new PhysicalNamingStrategyStandardImpl();
    }

    private Properties hibernateProperties() {

        Properties properties = new Properties();
        properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.physical_naming_strategy", env.getProperty("hibernate.physical_naming_strategy"));

        return properties;
    }

    @Bean
    public HibernateTransactionManager transactionManager() throws PropertyVetoException, IOException {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory());
        return transactionManager;
    }
}
