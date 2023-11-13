package com.dyrnq.stream.kafka;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.DeleteConsumerGroupsResult;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class DeleteConsumerGroupExample {
    public static void main(String[] args) {
        // 设置Kafka集群的地址
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstant.BOOTSTRAP_SERVERS);

        // 创建AdminClient
        try (AdminClient adminClient = AdminClient.create(properties)) {
            // 要删除的Consumer Group的名称
            String consumerGroup = "group-2298";

            // 执行删除Consumer Group的操作
            DeleteConsumerGroupsResult deleteResult = adminClient.deleteConsumerGroups(Collections.singleton(consumerGroup));

            // 等待操作完成
            deleteResult.all().get();
            System.out.println("Consumer Group " + consumerGroup + " has been deleted.");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
