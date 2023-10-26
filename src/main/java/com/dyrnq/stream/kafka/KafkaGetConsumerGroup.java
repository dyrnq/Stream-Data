package com.dyrnq.stream.kafka;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ListConsumerGroupOffsetsResult;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * 使用Kafka的AdminClient API來查詢Kafka中特定ConsumerGroup的offset
 * ref <a href="https://gist.github.com/erhwenkuo/bc4020112367af7abb78357963306ce0">...</a>
 */
public class KafkaGetConsumerGroup {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 设置Kafka的连接参数
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstant.BOOTSTRAP_SERVERS);

        // 创建AdminClient
        AdminClient adminClient = AdminClient.create(props);


        // 步驟3. 透過AdminClient的API來取得相關ConsumerGroup的訊息
        // *** 取得Kafka叢集裡ConsumerGroup基本資訊 ***  //
        // ListConsumerGroupsResult listConsumerGroupsResult = adminClient.listConsumerGroups();

        // 步驟4. 指定想要查找的ConsumerGroup
        String consumerGroupId = "group-2"; // <-- 替換你/妳的ConsumerGroup ID
        System.out.println("ConsumerGroup: " + consumerGroupId);

        ListConsumerGroupOffsetsResult listConsumerGroupOffsetsResult = adminClient.listConsumerGroupOffsets(consumerGroupId);
        // 取得這個ConsumerGroup曾經訂閱過的Topics的最後offsets
        Map<TopicPartition, OffsetAndMetadata> offsetAndMetadataMap = listConsumerGroupOffsetsResult.partitionsToOffsetAndMetadata().get();

        // 我們產生一個這個ConsumerGroup曾經訂閱過的TopicParition訊息
        List<TopicPartition> topicPartitions = new ArrayList<>();
        for (Map.Entry<TopicPartition, OffsetAndMetadata> entry : offsetAndMetadataMap.entrySet()) {
            TopicPartition topic_partition = entry.getKey(); // 某一個topic的某一個partition
            OffsetAndMetadata offset = entry.getValue(); // offset

            // 打印出來 (在API裡頭取到的offset都是那個partition最大的offset+1 (也就是下一個訊息會被assign的offset),
            // 因此我們減1來表示現在己經消費過的最大offset
            System.out.printf(" Topic: %s Partiton: %d Offset: %d%n", topic_partition.topic(), topic_partition.partition(), offset.offset());
            topicPartitions.add(topic_partition);
        }

        // 步驟5. 適當地釋放AdminClient的資源
        adminClient.close();

    }
}
