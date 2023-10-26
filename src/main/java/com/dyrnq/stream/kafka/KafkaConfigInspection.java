package com.dyrnq.stream.kafka;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.TopicConfig;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 对比ProducerConfig和ConsumerConfig的参数
 */
public class KafkaConfigInspection {

    public static void main(String[] args) throws IOException {

        ConfigDef producerConfigDef = ProducerConfig.configDef();

        ConfigDef consumerConfigDef = ConsumerConfig.configDef();

        ConfigDef adminClientConfigDef = AdminClientConfig.configDef();
        Map<String, Object> producerDefault = MapUtil.sort(producerConfigDef.defaultValues());
        Map<String, Object> consumerDefault = MapUtil.sort(consumerConfigDef.defaultValues());
        Map<String, Object> adminClientDefault = MapUtil.sort(adminClientConfigDef.defaultValues());


        System.out.println("===========================topicConfig===========================");

        Map<String, Object> topicConfig = reflectGetConfig(TopicConfig.class);

        for (Map.Entry<String, Object> entry : topicConfig.entrySet()) {
            System.out.println(entry.getKey());
        }


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
        System.out.println("===========================producer only===========================");

        checkHave(producerDefault, consumerDefault);

        System.out.println("===========================consumer only===========================");
        checkHave(consumerDefault, producerDefault);


    }

//    public static <K extends Comparable<? super K>, V> Map<K, V> sortByKey(Map<K, V> map, boolean isDesc) {
//        Map<K, V> result = Maps.newLinkedHashMap();
//        if (isDesc) {
//            map.entrySet().stream().sorted(Map.Entry.<K, V>comparingByKey().reversed())
//                    .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
//        } else {
//            map.entrySet().stream().sorted(Map.Entry.comparingByKey())
//                    .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
//        }
//        return result;
//    }


    public static void checkHave(Map<String, Object> one, Map<String, Object> two) {
        Map<String, Object> only_in_one = new HashMap<>();
        for (Map.Entry<String, Object> entry : one.entrySet()) {
            if (!two.containsKey(entry.getKey())) {
                only_in_one.put(entry.getKey(), entry.getValue());
            }

        }


        Map<String, Object> sortedByKey = MapUtil.sort(only_in_one);

        for (Map.Entry<String, Object> entry : sortedByKey.entrySet()) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }
    }


    public static Map<String, Object> reflectGetConfig(Class c) {
        Map<String, Object> topicConfig = new HashMap<>();


        java.util.Map<String, java.lang.reflect.Field> fields = ReflectUtil.getFieldMap(c);

        for (Map.Entry<String, Field> entry : fields.entrySet()) {
            String key = entry.getKey();
            Field field = entry.getValue();
            if (key.endsWith("_CONFIG")) {
                Field docField = null;
                try {
                    docField = fields.get(key.replace("_CONFIG", "_DOC"));
                } catch (Exception e) {

                }

                if (docField == null) {
                    docField = field;
                }
                topicConfig.put(ReflectUtil.getStaticFieldValue(field).toString(), ReflectUtil.getStaticFieldValue(docField).toString());
            }
        }
        return MapUtil.sort(topicConfig);
    }


}
