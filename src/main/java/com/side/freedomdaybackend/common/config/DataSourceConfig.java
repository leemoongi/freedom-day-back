//package com.side.freedomdaybackend.common.config;
//
//import com.zaxxer.hikari.HikariDataSource;
//import lombok.RequiredArgsConstructor;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.SqlSessionTemplate;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
//import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
//import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import javax.sql.DataSource;
//import java.util.Map;
//
//@RequiredArgsConstructor
//@Configuration
//@EnableJpaRepositories(
//        basePackages = "com.side.freedomdaybackend.domain",
//        entityManagerFactoryRef = "mariaJpaEntityManager",
//        transactionManagerRef = "mariaJpaTransactionManager"
//)
//@MapperScan(value = "com.side.freedomdaybackend.mapper")
//public class DataSourceConfig {
//
//    private final JpaProperties jpaProperties;
//
//    private final HibernateProperties hibernateProperties;
//
//    @Bean
//    @Primary
//    @ConfigurationProperties(prefix = "spring.maria-datasource") // 데이터소스 경로
//    public DataSource mariaJpaDataSource() {
//        return DataSourceBuilder.create()
//                .type(HikariDataSource.class)
//                .build();
//    }
//
//    /** JPA **/
//    @Bean
//    @Primary
//    public LocalContainerEntityManagerFactoryBean mariaJpaEntityManager() {
//        Map<String, Object> properties = hibernateProperties.determineHibernateProperties(jpaProperties.getProperties(), new HibernateSettings());
//        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//        em.setDataSource(mariaJpaDataSource());
//        em.setPackagesToScan(new String[]{"kr.co.salt.domain"});
//        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//        em.setJpaPropertyMap(properties); // application.yml 에서 jpa 설정 적용
//
//        return em;
//    }
//
//    @Bean
//    @Primary
//    public PlatformTransactionManager mariaJpaTransactionManager() {
//        JpaTransactionManager transactionManager = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(mariaJpaEntityManager().getObject());
//
//        return transactionManager;
//    }
//
//
//    /** 마이바티스 **/
//    @Bean(name = "mariaMybatisSqlsessionFactory")
//    public SqlSessionFactory mariaMybatisSqlsessionFactory(@Qualifier("mariaJpaDataSource") DataSource dataSource) throws Exception {
//        final PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
//        final SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
//        sqlSessionFactory.setDataSource(dataSource);
//        sqlSessionFactory.setMapperLocations(pathMatchingResourcePatternResolver.getResources("classpath:/mapper/*.xml")); // mapper 위치
//        sqlSessionFactory.setConfigLocation(pathMatchingResourcePatternResolver.getResource("classpath:/mybatis/mybatis-config.xml")); // mybatis config 위치
//        return sqlSessionFactory.getObject();
//    }
//
//    @Bean(name = "SessionTemplate")
//    public SqlSessionTemplate SqlSessionTemplate(@Qualifier("mariaMybatisSqlsessionFactory") SqlSessionFactory sqlSessionFactory) {
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }
//}
