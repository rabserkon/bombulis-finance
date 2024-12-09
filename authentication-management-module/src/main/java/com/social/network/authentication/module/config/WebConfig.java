package com.social.network.authentication.module.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;

@Configuration
@PropertySource("classpath:application-${spring.profiles.active}.properties")
public class WebConfig implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

    }
/*
    private String DISPATCHER = "dispatcher";
    @Value("${spring.servlet.multipart.location}")
    private String LOCATION;
    private final long maxFileSize = 26214400;
    private long maxRequestSize = 30000000;
    private int fileSizeThreshold = 10000000;*/


   /* @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
            AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
            MultipartConfigElement multipartConfig = new  MultipartConfigElement (LOCATION, maxFileSize, maxRequestSize, fileSizeThreshold);
            ctx.register(ModuleConfig.class);
            servletContext.addListener(new ContextLoaderListener(ctx));
            DispatcherServlet dispatcherServlet = new DispatcherServlet(ctx);
            ServletRegistration.Dynamic servlet = servletContext.addServlet(DISPATCHER, dispatcherServlet);
            servlet.setAsyncSupported(true);
            servlet.setMultipartConfig (multipartConfig);
            servlet.addMapping("/");
            servlet.setLoadOnStartup(1);
            FilterRegistration.Dynamic multipartFilter = servletContext.addFilter("multipartFilter", MultipartFilter.class);
            multipartFilter.addMappingForUrlPatterns(null, true, "/*");
    }*/

}
