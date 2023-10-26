package com.dyrnq.stream.kafka;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.TopicPartitionInfo;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * 获取kafka某个topic的Partition以及Replicas
 */
public class KafkaTopicPartitionInfoExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // Kafka bootstrap servers
        String bootstrapServers = KafkaConstant.BOOTSTRAP_SERVERS;

        // Topic to get partition information
        String topic = "mytopic";

        // Properties for creating admin client
        Properties adminProps = new Properties();
        adminProps.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        // Create admin client
        try (AdminClient adminClient = AdminClient.create(adminProps)) {
            // Describe the topic
            DescribeTopicsResult describeTopicsResult = adminClient.describeTopics(
                    java.util.Collections.singleton(topic), new DescribeTopicsOptions());
            TopicDescription topicDescription = describeTopicsResult.values().get(topic).get();

            // Print partition information
            for (TopicPartitionInfo partitionInfo : topicDescription.partitions()) {
                int partition = partitionInfo.partition();
                Node leader = partitionInfo.leader();
                List<Node> reps = partitionInfo.replicas();
                System.out.println("Partition: " + partition + ", Replicas " + reps + ", Leader: " + leader);
            }
        }
    }
}
