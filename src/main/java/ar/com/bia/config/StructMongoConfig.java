package ar.com.bia.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "ar.com.bia.pdb", mongoTemplateRef = "mongoTemplateStruct")
public class StructMongoConfig {

}
