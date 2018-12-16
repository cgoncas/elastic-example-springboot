package example.springboot.elastic;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;


import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
@EnableElasticsearchRepositories(basePackages = "example.springboot.elastic.repository")
@ComponentScan(basePackages = { "example.springboot.elastic.service" })
public class ElasticSearchConfig {

    @Value("${elasticsearch.cloud.host}")
    private String host;

    @Value("${elasticsearch.cloud.port}")
    private Integer port;

    @Value("${elasticsearch.cloud.credentials}")
    private String credentials;

    @Bean
    public TransportClient secureClient() throws Exception {
        String clusterName = host.split("\\.", 2)[0];

        logger.info("Connecting to cluster: [{}] via [{}:{}]", clusterName, host, port);

        // Build the settings for our client.
        Settings settings = Settings.builder()
                .put("client.transport.nodes_sampler_interval", "5s")
                .put("client.transport.sniff", false)
                .put("transport.tcp.compress", true)
                .put("cluster.name", clusterName)
                .put("xpack.security.transport.ssl.enabled", true)
                .put("request.headers.X-Found-Cluster", "${cluster.name}")
                .put("xpack.security.user", credentials)
                .put("xpack.security.transport.ssl.verification_mode", "full")
                .build();

        // Instantiate a TransportClient and add the cluster to the list of addresses to connect to.
        // Only port 9343 (SSL-encrypted) is currently supported. The use of x-pack security features is required.
        TransportClient client = new PreBuiltXPackTransportClient(settings);
        try {
            for (InetAddress address : InetAddress.getAllByName(host)) {
                client.addTransportAddress(new TransportAddress(address, port));

            }
        } catch (UnknownHostException e) {
            logger.error("Unable to get the host", e.getMessage());
        }

        logger.info("Connected to cluster: [{}] via [{}:{}]", clusterName, host, port);

        return client;
    }

    Logger logger = LoggerFactory.getLogger(ElasticSearchConfig.class);

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() throws Exception {
        return new ElasticsearchTemplate(secureClient());
    }
}
