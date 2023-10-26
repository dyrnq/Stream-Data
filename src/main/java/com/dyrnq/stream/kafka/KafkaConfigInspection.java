package com.dyrnq.stream.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.io.IOException;
import java.util.Map;

/**
 * 对比ProducerConfig和ConsumerConfig的参数
 */
public class KafkaConfigInspection {

    public static void main(String[] args) throws IOException {

        ConfigDef producerConfigDef = ProducerConfig.configDef();

        ConfigDef consumerConfigDef = ConsumerConfig.configDef();

        ConfigDef adminClientConfigDef = AdminClientConfig.configDef();
        Map<String, Object> producerDefault = producerConfigDef.defaultValues();
        Map<String, Object> consumerDefault = consumerConfigDef.defaultValues();
        Map<String, Object> adminClientDefault = adminClientConfigDef.defaultValues();

        System.out.println("===========================producerDefault===========================");
        for (Map.Entry<String, Object> entry : producerDefault.entrySet()) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }

        System.out.println("===========================consumerDefault===========================");
        for (Map.Entry<String, Object> entry : consumerDefault.entrySet()) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }

        System.out.println("===========================adminClientDefault===========================");
        for (Map.Entry<String, Object> entry : adminClientDefault.entrySet()) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }
        System.out.println("===========================producerDefault HAVE AND consumerDefault not HAVE===========================");

        for (Map.Entry<String, Object> entry : producerDefault.entrySet()) {
            if (consumerDefault.containsKey(entry.getKey())) {

            } else {
                System.out.println(entry.getKey());
            }

        }
        System.out.println("===========================consumerDefault HAVE AND producerDefault not HAVE===========================");

        for (Map.Entry<String, Object> entry : consumerDefault.entrySet()) {
            if (producerDefault.containsKey(entry.getKey())) {

            } else {
                System.out.println(entry.getKey());
            }

        }


    }
}
