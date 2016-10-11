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
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        SecurityAutoConfiguration.class})
@ComponentScan(basePackages = {"space"})
@EnableJpaRepositories(basePackages = {"space"})
@EntityScan(basePackages = {"space"})
@ImportResource("classpath:/spring/spring-config.xml")
@PropertySource("classpath:/spring/application.properties")
public class CoCoTempApplication {

    public static void main(String[] args) {

        //Start the Spring instance.
        SpringApplication.run(CoCoTempApplication.class, args);
    }
}
