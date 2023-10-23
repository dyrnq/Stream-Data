package com.dyrnq.stream.kafka;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;

import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * kafka如何用代码获取所有topic
 */
public class KafkaListTopicExample {


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 设置Kafka的连接参数
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstant.BOOTSTRAP_SERVERS);

        // 创建AdminClient
        AdminClient adminClient = AdminClient.create(props);

        Set topics = adminClient.listTopics().names().get();
        System.out.println(topics);
    }
}
