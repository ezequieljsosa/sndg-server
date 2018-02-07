package ar.com.bia;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;


@EnableWebMvc
@ComponentScan(basePackages = { "ar.com.bia.config",
		"ar.com.bia.backend.dao.impl","ar.com.bia.services" })
@PropertySource("classpath:xomeq.properties")
public class TestContextConfig extends WebMvcConfigurationSupport {

	@Value("${mongo-port}")
	private Integer port;

	@Value("${mongo-host}")
	private String host;

	@Value("${mongo-database}")
	private String database_name;
	
	@Bean
	public Mongo mongo() throws Exception {
		return new MongoClient(host,port);
	}

	@Bean
	public MongoTemplate mongoTemplate() {
		try {
			return new MongoTemplate(mongo(), database_name);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Bean
	public HttpServletRequest request() {
		return new MockHttpServletRequest();
	}

	@Bean
	public ServletContext servletContext() {
		return new MockServletContext();
	}

	@Override
	@Bean
	public HandlerMapping defaultServletHandlerMapping() {
		this.setServletContext(new MockServletContext());
		return super.defaultServletHandlerMapping();
	}
	
}
