package ProjetoTCC.TCC2.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Classe de configuração responsável por configurar a conexão com o MongoDB.
 */
@Configuration
public class MongoConfig {
    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb+srv://morgana:root@clustersimple.w7oo7.mongodb.net/?retryWrites=true&w=majority&appName=ClusterSimple");
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), "Simple");
    }
}
