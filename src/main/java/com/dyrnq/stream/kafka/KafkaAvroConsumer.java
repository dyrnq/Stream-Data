package com.dyrnq.stream.kafka;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Properties;

/**
 * https://codingharbour.com/apache-kafka/guide-to-apache-avro-and-kafka/
 * https://zhmin.github.io/posts/kafka-schema-registry/
 */
public class KafkaAvroConsumer {
    public static void main(String[] args) {

        String registryHost = KafkaConstant.SCHEMA_REGISTRY_SERVERS;
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", KafkaConstant.BOOTSTRAP_SERVERS);
        props.setProperty("group.id", "group-21");
        props.setProperty("enable.auto.commit", "true");
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty("key.deserializer", StringDeserializer.class.getName());
        props.setProperty("value.deserializer", KafkaAvroDeserializer.class.getName());
        props.setProperty("auto.offset.reset", "earliest");
        props.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, registryHost);
        props.put(KafkaAvroDeserializerConfig.AUTO_REGISTER_SCHEMAS, false);
        //props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);  //ensures records are properly converted

        // Integer.MAX_VALUE
        //指定"auto.offset.reset"参数的值为earliest；

        // ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION

        org.apache.kafka.clients.consumer.KafkaConsumer<String, Object> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("schema-tutorial"));
        while (true) {
            ConsumerRecords<String, Object> records = consumer.poll(100);
            // 打印消息
            for (ConsumerRecord<String, Object> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
                for (Header header : record.headers()) {
                    System.out.println("headers -->" + header.key() + ":" + new String(header.value()));
                }
            }
        }
    }
}
