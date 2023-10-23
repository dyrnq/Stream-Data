package com.dyrnq.stream.kafka;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.config.ConfigResource;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * kafka如何用代码获取topic的配置属性
 */
public class KafkaGetTopicConfigExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 设置Kafka的连接参数
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstant.BOOTSTRAP_SERVERS);

        // 创建AdminClient
        AdminClient adminClient = AdminClient.create(props);

        // 指定要获取配置属性的topic
        String topicName = "test";

        // 创建ConfigResource对象
        ConfigResource configResource = new ConfigResource(ConfigResource.Type.TOPIC, topicName);

        // 创建DescribeConfigsOptions对象
        DescribeConfigsOptions describeConfigsOptions = new DescribeConfigsOptions().includeSynonyms(false);

        // 执行DescribeConfigs请求
        DescribeConfigsResult describeConfigsResult = adminClient.describeConfigs(Collections.singletonList(configResource), describeConfigsOptions);

        // 获取配置属性的结果
        Config config = describeConfigsResult.all().get().get(configResource);

        // 输出配置属性
        for (ConfigEntry configEntry : config.entries()) {
            System.out.println(configEntry.name() + " = " + configEntry.value());
        }

        // 关闭AdminClient
        adminClient.close();
    }
}
