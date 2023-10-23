package com.dyrnq.stream.kafka;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.Config;
import org.apache.kafka.clients.admin.ConfigEntry;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.config.TopicConfig;

import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * kafka修改topic-level一些属性，比如 Min In Sync Replicas
 */
public class KafkaUpdateTopicConfigExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 设置Kafka的连接参数
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstant.BOOTSTRAP_SERVERS);

        // 创建AdminClient
        AdminClient adminClient = AdminClient.create(props);


        Config config = new Config(Arrays.asList(
                new ConfigEntry(TopicConfig.RETENTION_MS_CONFIG, "604800000"),
                new ConfigEntry(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, "10")
        ));


        String topicName = "my-topic";
        ConfigResource resource = new ConfigResource(ConfigResource.Type.TOPIC, topicName);
        adminClient.alterConfigs(Collections.singletonMap(resource, config));


        // 关闭AdminClient
        adminClient.close();
    }
}
