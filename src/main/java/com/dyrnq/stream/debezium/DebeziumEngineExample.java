package com.dyrnq.stream.debezium;
import io.debezium.config.Configuration;
//import io.debezium.connector.cassandra.CassandraConnectorConfig;
import io.debezium.connector.jdbc.JdbcSinkConnectorConfig;
import io.debezium.connector.mysql.MySqlConnectorConfig;
//import io.debezium.data.Json;
import io.debezium.embedded.Connect;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import org.apache.kafka.connect.connector.Connector;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.kafka.connect.storage.FileOffsetBackingStore;
import io.debezium.storage.redis.offset.RedisOffsetBackingStore;

import io.debezium.storage.rocketmq.history.RocketMqSchemaHistory;

// https://debezium.io/documentation/reference/stable/development/engine.html
public class DebeziumEngineExample {
    private static DebeziumEngine<ChangeEvent<String, String>> engine;
    public static void main(String[] args) throws IOException,Exception {

        // 创建一个MySQL连接器配置对象
        Configuration config = Configuration.create()
                .with("name", "my-sql-connector")
                .with("connector.class", "io.debezium.connector.mysql.MySqlConnector")
                .with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore")
                .with("offset.storage.file.filename", "/tmp/offsets.dat")
                .with("offset.flush.interval.ms", "60000")
                .with(MySqlConnectorConfig.HOSTNAME, "localhost")
                .with(MySqlConnectorConfig.PORT, "3306")
                .with(MySqlConnectorConfig.USER, "user")
                .with(MySqlConnectorConfig.PASSWORD, "password")
                .with(MySqlConnectorConfig.SERVER_ID,85744)
                .with("topic.prefix", "my-app-connector")
                .with("schema.history.internal","io.debezium.storage.file.history.FileSchemaHistory")
                .with("schema.history.internal.file.filename", "/tmp/schemahistory.dat")
                .build();
        // Create the engine with this configuration ...
        try (DebeziumEngine<ChangeEvent<String, String>> engine = DebeziumEngine.create(Json.class)
                .using(config.asProperties())
                .notifying(record -> {
                    System.out.println(record);
                }).build()
        ) {
            // Run the engine asynchronously ...
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(engine);

            // Do something else or wait for a signal or an event
        }
        // Engine is stopped when the main code is finished


    }
}
