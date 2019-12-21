package com.llipter.sigdict;

import com.llipter.sigdict.storage.StorageProperties;
import com.llipter.sigdict.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @SpringBootApplication is a convenience annotation that adds all of the following:
 * - @Configuration: Tags the class as a source of bean definitions for the application context.
 * - @EnableAutoConfiguration: Tells Spring Boot to start adding beans based on classpath settings,
 * other beans, and various property settings.
 * For example, if spring-webmvc is on the classpath,
 * this annotation flags the application as a web application and activates key behaviors,
 * such as setting up a DispatcherServlet.
 * - @ComponentScan: Tells Spring to look for other components,
 * configurations, and services in the hello package,
 * letting it find the controllers.
 */
@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class SigdictApplication {


    public static void main(String[] args) {
        SpringApplication.run(SigdictApplication.class, args);
    }

    /**
     * CommandLineRunner is a simple Spring Boot interface with a run method.
     * Spring Boot will automatically call the run method of all beans implementing this interface
     * after the application context has been loaded.
     */
    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
//            System.out.println("Working Directory = " + System.getProperty("user.dir"));
            storageService.deleteAll();
            storageService.init();
        };
    }


}
