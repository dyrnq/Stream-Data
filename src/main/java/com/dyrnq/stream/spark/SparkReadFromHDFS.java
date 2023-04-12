package com.dyrnq.stream.spark;

import com.dyrnq.stream.Constant;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class SparkReadFromHDFS {
    public static void main(String[] args) {

//        SparkConf conf = new SparkConf();
//        conf.setMaster("local");
//        conf.setAppName("testJob");

        SparkSession spark = SparkSession.builder()
                .master("local[*]")
                .appName("Read from HDFS")
                .getOrCreate();


        String hdfsPath = Constant.DEFAULT_FS+"/path/to/file";
        Dataset<Row> df = spark.read().csv(hdfsPath);

        df.show();
        spark.stop();
    }
}
