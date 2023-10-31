package com.dyrnq.stream.kafka;

import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import org.apache.avro.Schema;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * https://codingharbour.com/apache-kafka/guide-to-apache-avro-and-kafka/
 * https://zhmin.github.io/posts/kafka-schema-registry/
 */
public class KafkaAvroSchemaRegistry {
    public static void main(String[] args) throws IOException, RestClientException {
// schema registry url.
        String url = KafkaConstant.SCHEMA_REGISTRY_SERVERS;
// associated topic name.
        String topic = "schema-tutorial";
// avro schema avsc file path.
        String schemaPath = "src/avro/user.avsc";
// subject convention is "<topic-name>-value"
        String subject = topic + "-value";
// avsc json string.
        String schema = null;

        FileInputStream inputStream = new FileInputStream(schemaPath);
        try {
            schema = IOUtils.toString(inputStream);
        } finally {
            inputStream.close();
        }

        Schema avroSchema = new Schema.Parser().parse(schema);

        CachedSchemaRegistryClient client = new CachedSchemaRegistryClient(url, 20);

//        client.deleteSubject(topic);
//        client.setMode("import");
        client.register(subject, avroSchema);
    }
}
