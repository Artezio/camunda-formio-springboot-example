package com.artezio.forms.rest;

import org.camunda.bpm.engine.rest.security.auth.ProcessEngineAuthenticationFilter;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.servlet.Filter;

@EnableProcessApplication
@ServletComponentScan
@Configuration
@PropertySource("classpath:application.yml")
@ComponentScan("com.artezio")
public class SpringConfig {

    private String formsApiPath;

    @Value("${forms.rest.api.path}")
    public void setFormsApiPath(String formsApiPath) {
        this.formsApiPath = formsApiPath.endsWith("/")
                ? formsApiPath.substring(0, formsApiPath.length() - 1)
                : formsApiPath;
    }

    @Bean
    public ServletRegistrationBean getServletRegistrationBean() {
        ResourceConfig resourceConfig = new ResourceConfig()
                .register(RestService.class)
                .register(RestApplication.class);
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new ServletContainer(resourceConfig));
        servletRegistrationBean.addUrlMappings(String.format("%s/*", formsApiPath));
        servletRegistrationBean.setName("FormsApi");
        servletRegistrationBean.setLoadOnStartup(0);
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean processEngineAuthenticationFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setName("camunda-auth");
        registration.setFilter(getProcessEngineAuthenticationFilter());
        registration.addInitParameter("authentication-provider",
                "org.camunda.bpm.engine.rest.security.auth.impl.HttpBasicAuthenticationProvider");
        registration.addUrlPatterns("/*");
        return registration;
    }

    @Bean
    public Filter getProcessEngineAuthenticationFilter() {
        return new ProcessEngineAuthenticationFilter();
    }
}
