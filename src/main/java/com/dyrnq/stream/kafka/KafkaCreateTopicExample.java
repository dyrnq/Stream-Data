package com.dyrnq.stream.kafka;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * kafka如何用代码手工创建一个topic，并在创建的时候指定一些属性，比如 Min In Sync Replicas
 */
public class KafkaCreateTopicExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 设置Kafka的连接参数
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstant.BOOTSTRAP_SERVERS);

        // 创建AdminClient
        AdminClient adminClient = AdminClient.create(props);

        // 创建NewTopic对象，并设置属性
        Map<String, String> topicConfig = new HashMap<>();
        topicConfig.put(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, "2");

        NewTopic newTopic = new NewTopic("my-topic", 1, (short) 1);
        newTopic.configs(topicConfig);


        // 执行创建主题请求
        adminClient.createTopics(Collections.singleton(newTopic)).all().get();


        // 关闭AdminClient
        adminClient.close();
    }
}
