package sample.mybatis.config;

import java.lang.reflect.InvocationTargetException;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
public class DataSourceConfiguration {

	private Logger logger =  LoggerFactory.getLogger(getClass());
	
	@Inject
	private Environment environment;
	
	@Bean
	public DataSource dataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		logger.info("Druid 数据库连接池配置开始....");
		// Connection settings
		((AbstractEnvironment) environment).getPropertySources().forEach((ps) -> {
			if (ps instanceof MapPropertySource) {
				String[] keys = ((MapPropertySource) ps).getPropertyNames();
				String urlKey = "jdbc.url";
				if(ArrayUtils.isNotEmpty(keys)&&ArrayUtils.contains(keys, urlKey)){
					for (String key : keys) {
						if (StringUtils.contains(key, "jdbc.property")) {
							String name = StringUtils.remove(key, "jdbc.property.");
							Object value = ps.getProperty(key);
							try {
								if (value != null) {
									boolean isNumber = NumberUtils.isNumber(value.toString());
									if (isNumber) {
										value = NumberUtils.createInteger(value.toString());
									}
									Boolean isBoolean = BooleanUtils.toBooleanObject(value.toString());
									if (isBoolean != null) {
										value = BooleanUtils.toBoolean(value.toString());
									}
									logger.info("数据库连接池属性:{} == 属性值:{}", name, value);
									PropertyUtils.setProperty(dataSource, name, value);
								}
							} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
								logger.error(e.getMessage(), e);
							}
						}
					}
					String url = environment.getProperty(urlKey);
					String username = environment.getProperty("jdbc.username");
					String password = environment.getProperty("jdbc.password");
					String driver = environment.getProperty("jdbc.driver");
					logger.info("数据库连接地址:{}",url);
					logger.info("数据库连接用户名:{}",username);
					logger.info("数据库连接密码:{}",password);
					logger.info("数据库连接驱动:{}",driver);
					dataSource.setUrl(url);
					dataSource.setUsername(username);
					dataSource.setPassword(password);
					dataSource.setDriverClassName(driver);
				}
			}
		});
		logger.info("Druid 数据库连接池配置结束....");
		return dataSource;
	}
}
