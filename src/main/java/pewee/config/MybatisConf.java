package pewee.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import pewee.AppBoot;

@Configuration
//@MapperScan("pewee.dao")//MapperScannerConfigurer 
public class MybatisConf implements ApplicationContextAware{
	
	private ApplicationContext applicationContext;
	
	/*@Value("${server.name}")
	String appname;*/
	
	@Bean
	public MapperScannerConfigurer getConfigur() {
		
		
		
		MapperScannerConfigurer configurer = new MapperScannerConfigurer();
		configurer.setBasePackage("pewee.dao");
		return configurer;
	}
	
	@Bean
	public AppBoot getLib() {
		//Environment environment = applicationContext.getEnvironment();
		//String string = environment.getProperty("server.name");
		//System.out.println(string);
		//System.out.println(appname);
		return new AppBoot();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		this.applicationContext = applicationContext;
	}
	
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
}
