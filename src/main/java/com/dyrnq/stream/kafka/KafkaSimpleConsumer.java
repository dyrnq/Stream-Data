package com.dyrnq.stream.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class KafkaSimpleConsumer {

    public static void main(String[] args) {


        Properties props = new Properties();
        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstant.BOOTSTRAP_SERVERS);
        props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "group-2298");
        props.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, OffsetResetStrategy.EARLIEST.toString());

//        props.setProperty("max.poll.records", "10000");
//        props.setProperty("max.poll.interval.ms", "1");
//        max.poll.records：100 (默认值 500)
//        max.poll.interval.ms：600000 (默认值 300000，也就是5分钟)


        //props.setProperty(ConsumerConfig.ISOLATION_LEVEL_CONFIG, IsolationLevel.READ_COMMITTED.toString().toLowerCase(Locale.ROOT));
        // Integer.MAX_VALUE
        //指定"auto.offset.reset"参数的值为earliest；

        // ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION

        org.apache.kafka.clients.consumer.KafkaConsumer<String, Object> consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("abc"));


        try {
            while (true) {
                // consumer.consume();
                ConsumerRecords<String, Object> records = consumer.poll(Duration.ofMillis(500));
                for (ConsumerRecord<String, Object> record : records) {
                    // Process the record
                    System.out.println("Received message: " + record.value());

                    // Manually commit the offset
//                    TopicPartition topicPartition = new TopicPartition(record.topic(), record.partition());
//                    consumer.commitSync(Collections.singletonMap(topicPartition, new OffsetAndMetadata(record.offset() + 1)));
                }
            }
        } finally {
            consumer.close();
        }


    }
}
