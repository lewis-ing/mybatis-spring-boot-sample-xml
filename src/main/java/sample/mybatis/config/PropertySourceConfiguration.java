package sample.mybatis.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@MapperScan("com.ecmcloud.datura.siege")
@Configuration
@PropertySources({
	@PropertySource("classpath:/mybatis.properties"),
	@PropertySource("classpath:/db.properties")
})
@ComponentScan(basePackages= {"com.ecmcloud.datura.siege","com.ecmcloud.datura.repository","com.ecmcloud.datura.common"})
public class PropertySourceConfiguration {

}
