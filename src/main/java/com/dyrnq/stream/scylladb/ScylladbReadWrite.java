package com.dyrnq.stream.scylladb;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;

import java.net.InetSocketAddress;

// <https://github.com/yannmango/scylladb-example/blob/master/src/main/java/com/yanan/demo/App.java>
public class ScylladbReadWrite {

    static String dc = "datacenter1";
    static CqlSession session = CqlSession.builder()
            .addContactPoint(InetSocketAddress.createUnresolved("172.18.0.6", 9042))
            .addContactPoint(InetSocketAddress.createUnresolved("172.18.0.7", 9042))
            .addContactPoint(InetSocketAddress.createUnresolved("172.18.0.8", 9042))
            .withLocalDatacenter(dc)
            .withKeyspace("catalog")
            .build();


    static PreparedStatement insert = session.prepare("INSERT INTO mutant_data (first_name,last_name,address,picture_location) VALUES (?,?,?,?)");

    static PreparedStatement delete = session.prepare("DELETE FROM mutant_data WHERE first_name = ? and last_name = ?");

    public static void main(String[] args) {
        selectQuery();
        insertQuery();
        updateQuery();
        deleteQuery();
        prepareInsertQuery("Alex", "Jones", "56789 Hickory St", "http://www.facebook.com/ajones");
        prepareDeleteQuery("Alex", "Jones");
        testCreateKeySpace();
        testCreateTable();
        session.close();
    }

    public static void selectQuery() {
        System.out.print("\n\nDisplaying Results:");
        ResultSet results = session.execute("SELECT * FROM catalog.mutant_data");
        for (Row row : results) {
            String first_name = row.getString("first_name");
            String last_name = row.getString("last_name");
            String address = row.getString("address");
            System.out.print("\n" + first_name + " " + last_name + " " + address);
        }
    }

    public static void insertQuery() {
        System.out.print("\n\nInserting Mike Tyson......");
        session.execute(
                "INSERT INTO mutant_data (first_name,last_name,address,picture_location) VALUES ('Mike','Tyson','1515 Main St', 'http://www.facebook.com/mtyson')");
        selectQuery();
    }

    public static void updateQuery() {
        System.out.print("\n\nUpdateing Mike Tyson......");
        session.execute(
                "UPDATE mutant_data set address = '8200 Warden Street' WHERE last_name = 'Tyson' and first_name = 'Mike'");
        selectQuery();
    }

    public static void deleteQuery() {
        System.out.print("\n\nDeleting Mike Tyson......");
        session.execute("DELETE FROM mutant_data WHERE last_name = 'Tyson' and first_name = 'Mike'");
        selectQuery();
    }

    /*
     * add two columns to the catalog.mutant_data table: b and m. Column b is the
     * blob column where the binary file is stored. column m is used to record the
     * file’s name.
     *
     */
//    public static void createSchema(Session session) {
//        try {
//            session.execute("ALTER table catalog.mutant_data ADD b blob");
//            session.execute("ALTER table catalog.mutant_data ADD m map<text, blob>");
//        } catch (Exception schema) {
//        }
//
//    }

    public static void prepareInsertQuery(String first_name, String last_name, String address,
                                          String picture_location) {
        System.out.print("\n\nInserting with preparedStatement " + first_name + "......");
        session.execute(insert.bind(first_name, last_name, address, picture_location));
        selectQuery();
    }

    public static void prepareDeleteQuery(String first_name, String last_name) {
        System.out.print("\n\nDeleting with preparedStatement" + first_name + "......");
        session.execute(delete.bind(first_name, last_name));
        selectQuery();
    }

    private static void createKeyspace(String keyspaceName, String replicationStrategy, int replicationFactor) {

        String query = "CREATE KEYSPACE IF NOT EXISTS " + keyspaceName +
                " WITH replication = {" + "'class':'" + replicationStrategy +
                "','replication_factor':" + replicationFactor + "};";
        session.execute(query);

    }

    public static void testCreateKeySpace() {
        String keyspaceName = "simplex";
        System.out.print("\n\nCreating new keyspace " + keyspaceName + "......");

        createKeyspace(keyspaceName, "SimpleStrategy", 1);
        ResultSet results = session.execute("SELECT * FROM system_schema.keyspaces;");
//        List<String> matchedKeyspaces = result.all().stream()
//                .filter(r -> r.getString(0).equals(keyspaceName.toLowerCase())).map(r -> r.getString(0))
//                .collect(Collectors.toList());
        System.out.print("\n\nDisplaying Results:");
        for (Row row : results) {
            String name = row.getString(0);
            System.out.print("\n" + "keyspace with name " + name + " exists");
        }
    }

    private static void createTable(String TABLE_NAME, String KEYSPACE_NAME) {
        session.execute("USE " + KEYSPACE_NAME);

        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                "id uuid PRIMARY KEY, " + "title text," + "album text," + "artist text," +
                "tags set<text>," + "data blob);";
        session.execute(query);
    }

    public static void testCreateTable() {
        String KEYSPACE_NAME = "simplex";
        String TABLE_NAME = "songs";
        session.execute("USE " + KEYSPACE_NAME);
        System.out.print("\n\nCreating table " + KEYSPACE_NAME + "." + TABLE_NAME + "......");
        createTable(TABLE_NAME, KEYSPACE_NAME);
        ResultSet result = session.execute("SELECT * FROM system_schema.tables");

//        List<String> columnNames = result.getColumnDefinitions().asList().stream().map(cl -> cl.getName())
//                .collect(Collectors.toList());
//
//        String listString = columnNames.stream().map(Object::toString)
//                .collect(Collectors.joining(", "));

//        System.out.println("Table "+TABLE_NAME + "contains "+ listString);
        for (Row row : result) {
            String name = row.getString("keyspace_name");
            String table = row.getString("table_name");
            System.out.print("\n" + "table with name " + table + " exists in the keyspace " + name);
        }
    }
}


//        SELECT * FROM system_schema.keyspaces;
//
//        keyspace_name                 | durable_writes | replication
//-------------------------------+----------------+---------------------------------------------------------------------------------------
//        system_auth |           True |   {'class': 'org.apache.cassandra.locator.SimpleStrategy', 'replication_factor': '1'}
//        system_schema |           True |                               {'class': 'org.apache.cassandra.locator.LocalStrategy'}
//        system_distributed |           True |   {'class': 'org.apache.cassandra.locator.SimpleStrategy', 'replication_factor': '3'}
//        system |           True |                               {'class': 'org.apache.cassandra.locator.LocalStrategy'}
//        tracking |           True | {'class': 'org.apache.cassandra.locator.NetworkTopologyStrategy', 'datacenter1': '3'}
//        catalog |           True | {'class': 'org.apache.cassandra.locator.NetworkTopologyStrategy', 'datacenter1': '3'}
//        system_traces |           True |   {'class': 'org.apache.cassandra.locator.SimpleStrategy', 'replication_factor': '2'}
//        simplex |           True |   {'class': 'org.apache.cassandra.locator.SimpleStrategy', 'replication_factor': '1'}
//        system_distributed_everywhere |           True |                          {'class': 'org.apache.cassandra.locator.EverywhereStrategy'}
//
