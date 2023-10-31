package com.dyrnq.stream.kafka;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.apache.kafka.common.serialization.StringSerializer;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Future;

/**
 * https://codingharbour.com/apache-kafka/guide-to-apache-avro-and-kafka/
 * https://zhmin.github.io/posts/kafka-schema-registry/
 */
public class KafkaAvroProducer {
    public static void main(String[] args) throws IOException {
        String kafkaHost = KafkaConstant.BOOTSTRAP_SERVERS;
        String topic = "schema-tutorial";
        String schameFilename = "src/avro/user.avsc";
        String registryHost = KafkaConstant.SCHEMA_REGISTRY_SERVERS;

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaHost);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // 指定Value的序列化类，KafkaAvroSerializer
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);

        // 指定 registry 服务的地址
        // 如果 Schema Registry 启动了高可用，那么这儿的配置值可以是多个服务地址，以逗号隔开
        props.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, registryHost);
        props.put(KafkaAvroSerializerConfig.AUTO_REGISTER_SCHEMAS, false);
        KafkaProducer<String, GenericRecord> producer = new KafkaProducer<>(props);
        String key = "Alyssa key";
        Schema schema = new Schema.Parser().parse(new File(schameFilename));

        for (int i = 0; i < 10; i++) {
            GenericRecord avroRecord = new GenericData.Record(schema);
            avroRecord.put("name", "Alyssa");
            avroRecord.put("favorite_number", (2000 + i));
            avroRecord.put("favorite_color", "RED");
            avroRecord.put("favorite_city", "北京");

            // 发送消息
            ProducerRecord<String, GenericRecord> record = new ProducerRecord<>(topic, key, avroRecord);

            // 设置header记录
            List<Header> headers = new ArrayList<>();
            record.headers().add(new RecordHeader("header1", "value1".getBytes()));
            record.headers().add(new RecordHeader("header2", "value2".getBytes()));

            Future<RecordMetadata> x = producer.send(record);
        }


        producer.flush();
        producer.close();
    }
}
