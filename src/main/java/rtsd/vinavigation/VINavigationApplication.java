package rtsd.vinavigation;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import rtsd.vinavigation.service.MqttHandler;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Arrays;

//@Configuration
@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@MapperScan(value = {"rtsd.vinavigation.dao"}, sqlSessionFactoryRef = "org.mybatis.spring.SqlSessionFactoryBean")
public class VINavigationApplication {

	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	@Primary
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource());
	}

	@Primary
	@Bean(name = "org.mybatis.spring.SqlSessionFactoryBean")
	public SqlSessionFactoryBean getSqlSessionFactoryBean() {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource());
		// some stuff happens here
		return sqlSessionFactoryBean;
	}

	@Autowired
	MqttHandler mqttHandler;

	public static void main(String[] args) {
		SpringApplication.run(VINavigationApplication.class, args);
	}

	public VINavigationApplication() {
		System.out.println("\nAPP INIT!\n");
	}

	@PostConstruct
	public void init() {
		System.out.println("Calling after all beans ready");
		mqttHandler.run();
	}

	/*@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			System.out.println("Let's inspect the beans provided by Spring Boot:");

			String[] beanNames = ctx.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			for (String beanName : beanNames) {
				System.out.println(beanName);
			}

		};
	}*/
}
