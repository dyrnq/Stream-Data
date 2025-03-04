package com.dyrnq.stream.kafka;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import com.github.f4b6a3.tsid.TsidCreator;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Date;
import java.util.Properties;

public class KafkaOrderProducer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstant.BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put("sequence.number", true);
        props.put("sequence.number.start", 1000);
        props.put("sequence.number.increment", 1);

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        while (true) {
            long number = TsidCreator.getTsid().toLong();
            int customer_id = RandomUtil.randomInt(0,Integer.MAX_VALUE);
            String order_date = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS");

            String status = "pending";
            if (customer_id % 3 == 0) {
                status = "paid";
            }

            String order = "{\"id\": " + number + ", \"customer_id\": " + customer_id + ", \"order_date\": \"" + order_date + "\", \"status\": \"" + status + "\", \"total_amount\": 100.0}";
            ProducerRecord<String, String> record = new ProducerRecord<>("orders", order);

            producer.send(record);
            ThreadUtil.sleep(1000L);
            //producer.close();
        }


    }
}
