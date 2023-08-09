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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan(basePackages = {
        "ar.com.bia.services",
        "ar.com.bia.backend.dao.impl"})
@PropertySource("classpath:xomeq.properties")
public class ContextConfig {

    @Value("${mongo-port}")
    private Integer port;

    @Value("${mongo-host}")
    private String host;

    @Value("${mongo-database}")
    private String database_name;

    @Value("${mysql_user}")
    private String mysql_user;

    @Value("${mysql_pass}")
    private String mysql_pass;


    @Value("${wplogin_url}")
    private String wplogin_url;


    @Autowired
    private UserRepositoryImpl userRepository;

    @Bean(name = "wpData")
    public Map<String, Object> wpData() {
        Map<String, Object> data = new HashMap<>();
        data.put("wplogin_url", wplogin_url);
        return data;
    }


    @Bean
    public DataSource dataSource() {
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            DataSource ds = new DriverManagerDataSource("jdbc:mysql://localhost:3306/targetwp", mysql_user, mysql_pass);

            return ds;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }


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
        return new UserService(dataSource(),wpData(),userRepository);
    }



}
