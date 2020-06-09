package ar.com.bia.config;

import ar.com.bia.backend.dao.impl.UserRepositoryImpl;
import ar.com.bia.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.context.ApplicationContext;

@Configuration
@ComponentScan(basePackages = {
        "ar.com.bia.services",
        "ar.com.bia.backend.dao.impl"})
@PropertySource("classpath:xomeq.properties")
public class ContextConfig {

    //@Autowired
    //private UserRepositoryImpl userRepository;

    @Autowired
    private ApplicationContext context;

    @Value("${mongo-port}")
    private Integer port;

    @Value("${mongo-host}")
    private String host;

    @Value("${mongo-database}")
    private String database_name;

    @Bean
    public Mongo mongo() throws Exception {
        return new MongoClient(host, port);
    }


    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongo(), database_name);


    }

    @Bean(autowire = Autowire.BY_NAME, name = "mongoTemplateStruct")
    public MongoTemplate mongoTemplateStruct() throws Exception {
        return new MongoTemplate(new MongoClient(host, port), "pdb");
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public ObjectMapper mapperJson() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return mapper;
    }

    @Bean
    public UserService userService() {
        UserRepositoryImpl userRepository = null;
        userRepository = context.getBean(UserRepositoryImpl.class) ;
        return new UserService(userRepository);
    }


}
