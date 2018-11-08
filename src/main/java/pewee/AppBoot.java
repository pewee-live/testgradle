package pewee;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableTransactionManagement
@SpringBootApplication
@EnableScheduling
//@EnableWebMvc
public class AppBoot {
	
	public static void main(String[] args) {
		 SpringApplication.run(AppBoot.class, args);
	}
	
}
