package space.hideaway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;


@EnableAutoConfiguration(
        exclude = {DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        SecurityAutoConfiguration.class})
@ComponentScan(basePackages = {"space"})
@EnableJpaRepositories(basePackages = {"space"})
@EntityScan(basePackages = {"space"})
@ImportResource("classpath:/spring/spring-config.xml")
@EnableAsync
@EnableScheduling
public class CoCoTempApplication extends AsyncConfigurerSupport
{

    public static void main(String[] args)
    {
        SpringApplication.run(CoCoTempApplication.class, args);
    }

    @Override
    public Executor getAsyncExecutor()
    {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("CoCoTemp-");
        executor.initialize();
        return executor;
    }
}
