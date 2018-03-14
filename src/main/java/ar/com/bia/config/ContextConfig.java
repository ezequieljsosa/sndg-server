package ar.com.bia.config;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

import ar.com.bia.entity.BarcodeDoc;
import ar.com.bia.entity.ContigDoc;
import ar.com.bia.entity.GeneProductDoc;
import ar.com.bia.entity.SeqCollectionDoc;
import ar.com.bia.entity.ToolDoc;
import ar.com.bia.pdb.StructureDoc;

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





}
