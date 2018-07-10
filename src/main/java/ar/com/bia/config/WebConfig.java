package ar.com.bia.config;

import ar.com.bia.DataTablesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.sql.DataSource;
import java.util.Locale;

@Configuration
@ComponentScan(basePackages = {"ar.com.bia.controllers",
        "ar.com.bia.config", "ar.com.bia.backend.dao.impl",
        "ar.com.bia.controllers.services"})
@PropertySource("classpath:xomeq.properties")
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

    private static final String VIEW_EXTENTION_JSP = ".jsp";
    private static final String VIEW_DIR = "/WEB-INF/views/";

    @Value("${lang}")
    private String lang;

    @Autowired(required = false)
    private DataSource dataSource;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/public/jbrowse/**").addResourceLocations("file:/data/xomeq/jbrowse/");
        registry.addResourceHandler("/public/**").addResourceLocations("/public/");


    }

    // @Bean
    // public ViewResolver getViewResolver() {
    // InternalResourceViewResolver viewResolver = new
    // InternalResourceViewResolver();
    // //resolver.setViewClass(JS)
    //
    // viewResolver.setPrefix(VIEW_DIR);
    // viewResolver.setSuffix(VIEW_EXTENTION_JSP);
    // return viewResolver;
    // }

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix(VIEW_DIR);
        viewResolver.setSuffix(VIEW_EXTENTION_JSP);
        return viewResolver;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
//        if (dataSource != null) {
//            registry.addRedirectViewController( "/login","../targetwp/wp-login.php"
//
//            );
//        } else {
        //registry.addRedirectViewController( "/","/logout");
            registry.addViewController("/login").setViewName("login");
            registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
//        }
    }

    @Bean
    public DataTablesUtils dataTablesUtils() {
        return new DataTablesUtils();
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        commonsMultipartResolver.setDefaultEncoding("utf-8");
        commonsMultipartResolver.setMaxUploadSize(50000000);
        return commonsMultipartResolver;
    }

    // http://www.concretepage.com/spring/spring-mvc/spring-handlerinterceptor-annotation-example-webmvcconfigureradapter

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("mylocale");
        registry.addInterceptor(interceptor);

        registry.addInterceptor(new NoCacheInterceptor());


        // registry.addInterceptor(new
        // TransactionInterceptor()).addPathPatterns("/person/save/*");
    }

    // http://www.concretepage.com/spring-4/spring-4-mvc-internationalization-i18n-and-localization-l10n-annotation-example
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("/WEB-INF/i18/usermsg");
        messageSource.setDefaultEncoding("ISO-8859-1");
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver resolver = new CookieLocaleResolver();
        resolver.setDefaultLocale(new Locale(this.lang));
        resolver.setCookieName("myLocaleCookie");
        resolver.setCookieMaxAge(4800);
        return resolver;
    }

}
