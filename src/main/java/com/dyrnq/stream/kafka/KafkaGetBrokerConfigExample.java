package com.dyrnq.stream.kafka;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.config.ConfigResource;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * kafka如何用代码获取broker的配置属性
 * ./bin/kafka-configs.sh --bootstrap-server <broker-host>:9092 --entity-type brokers --entity-name <broker-id> --all --describe
 * https://stackoverflow.com/questions/49406618/get-current-config-in-kafka
 */
public class KafkaGetBrokerConfigExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 设置Kafka的连接参数
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstant.BOOTSTRAP_SERVERS);

        // 创建AdminClient
        AdminClient adminClient = AdminClient.create(props);

        // 创建ConfigResource对象
        ConfigResource configResource = new ConfigResource(ConfigResource.Type.BROKER, "12");

        // 创建DescribeConfigsOptions对象
        DescribeConfigsOptions describeConfigsOptions = new DescribeConfigsOptions().includeSynonyms(false);

        // 执行DescribeConfigs请求
        DescribeConfigsResult describeConfigsResult = adminClient.describeConfigs(Collections.singleton(configResource), describeConfigsOptions);

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
