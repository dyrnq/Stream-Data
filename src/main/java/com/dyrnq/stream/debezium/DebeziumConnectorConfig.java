package com.dyrnq.stream.debezium;
import io.debezium.config.Configuration;
import io.debezium.connector.cassandra.CassandraConnectorConfig;
import io.debezium.connector.jdbc.JdbcSinkConnectorConfig;
import io.debezium.connector.mysql.MySqlConnectorConfig;
import io.debezium.data.Json;
import io.debezium.embedded.Connect;
import org.apache.kafka.connect.connector.Connector;

import java.io.IOException;
import java.util.HashMap;

public class DebeziumConnectorConfig {

    public static void main(String[] args) throws IOException,Exception {

        // 创建一个MySQL连接器配置对象
//        Configuration config = Configuration.create()
//                .with("name", "my-sql-connector")
//                .with("connector.class", "io.debezium.connector.mysql.MySqlConnector")
//                .with(MySqlConnectorConfig.HOSTNAME, "localhost")
//                .with(MySqlConnectorConfig.PORT, "3306")
//                .with(MySqlConnectorConfig.USER, "user")
//                .with(MySqlConnectorConfig.PASSWORD, "password")
//                .build();


        System.out.println(MySqlConnectorConfig.ALL_FIELDS.allFieldNames());

        System.out.println(JdbcSinkConnectorConfig.ALL_FIELDS.allFieldNames());


    }
}
