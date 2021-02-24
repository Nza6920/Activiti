package org.activiti.app.ui;

import org.activiti.app.conf.ApplicationConfiguration;
import org.activiti.app.servlet.ApiDispatcherServletConfiguration;
import org.activiti.app.servlet.AppDispatcherServletConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * 启动类
 *
 * @author [nza]
 * @version 1.0 [2021/02/23 16:01]
 * @createTime [2021/02/23 16:01]
 */
@SpringBootApplication(exclude = {
        SecurityAutoConfiguration.class,
        org.activiti.spring.boot.SecurityAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
@Import({ApplicationConfiguration.class})
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }

    @Bean
    public ServletRegistrationBean apiDispatcher() {
        DispatcherServlet api = new DispatcherServlet();
        api.setContextClass(AnnotationConfigWebApplicationContext.class);
        api.setContextConfigLocation(ApiDispatcherServletConfiguration.class.getName());
        ServletRegistrationBean registrationBean = new ServletRegistrationBean();
        registrationBean.setServlet(api);
        registrationBean.addUrlMappings("/api/*");

        // api下面的所有内容都访问到这里
        registrationBean.setLoadOnStartup(1);
        registrationBean.setAsyncSupported(true);

        // 不能重复，重复则以最后一个设置的为准
        registrationBean.setName("api");
        return registrationBean;
    }

    @Bean
    public ServletRegistrationBean appDispatcher() {
        DispatcherServlet api = new DispatcherServlet();
        api.setContextClass(AnnotationConfigWebApplicationContext.class);
        api.setContextConfigLocation(AppDispatcherServletConfiguration.class.getName());
        ServletRegistrationBean registrationBean = new ServletRegistrationBean();
        registrationBean.setServlet(api);

        // app下面的所有内容都访问到这里
        registrationBean.addUrlMappings("/app/*");
        registrationBean.setLoadOnStartup(1);
        registrationBean.setAsyncSupported(true);

        // 不能重复，重复则以最后一个设置的为准
        registrationBean.setName("app");

        return registrationBean;
    }
}
