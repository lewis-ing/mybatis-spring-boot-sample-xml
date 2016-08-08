package sample.mybatis.config;

import java.io.IOException;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 
 * Mybatis 基础功能配置
 * 
 */
@Configuration
@EnableTransactionManagement
public class InfrastructureConfiguration{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public String MYBATISPROPERTIES = "mybatis.properties";
	
	public String MYBATISMAPPER = "mybatis.mapper.path";
	
	public String ALIASESPACKAGE = "mybatis.aliases.package";

	@Inject
	private Environment env;
	
	/**
	 * 数据源配置
	 * @param dataSource
	 * @return
	 * @throws Exception
	 */
	@Bean
	@Autowired
	PlatformTransactionManager transactionManager(DataSource dataSource) throws Exception {
		return new DataSourceTransactionManager(dataSource);
	}
	
	/**
	 * SqlSessionTemplate的配置
	 * @param sqlSessionFactory
	 * @return
	 * @throws Exception
	 */
	@Bean
	@Autowired
	SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory,ExecutorType.BATCH);
	}
	
	/**
	 * mybatis SqlSessionFactory管理
	 * @param dataSource
	 * @param resourceLoader
	 * @return
	 * @throws Exception
	 */
	@Bean
	@Autowired
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource, ResourceLoader resourceLoader) throws Exception {
		String aliases = env.getProperty(ALIASESPACKAGE);
		String mapper = env.getProperty(MYBATISMAPPER);
		logger.info("mybatis.aliases.package:{}",aliases);
		logger.info("mybatis.mapper.path:{}",mapper);
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);
		sessionFactory.setTypeAliasesPackage(aliases);
		sessionFactory.setMapperLocations(getResources(resourceLoader, mapper));
		return sessionFactory.getObject();
	}
	
	
	/**
	 * 加载mybatis的xml配置文件资源
	 * @param resourceLoader
	 * @param packagePath
	 * @return
	 * @throws IOException
	 */
	private org.springframework.core.io.Resource[] getResources(ResourceLoader resourceLoader, String packagePath) throws IOException {
		ResourcePatternResolver resourceResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
		return resourceResolver.getResources(packagePath);
	}
}
