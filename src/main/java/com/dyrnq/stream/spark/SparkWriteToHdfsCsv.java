package com.dyrnq.stream.spark;


import com.dyrnq.stream.Constant;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SparkWriteToHdfsCsv {

    public static void main(String[] args) throws IOException,Exception {
        // 创建SparkSession
        SparkSession spark = SparkSession.builder()
                .appName("Write to HDFS CSV")
                .master("local[*]")
                .getOrCreate();

        // 创建测试数据集
        List<List<String>> data = new ArrayList<>();
        data.add(Arrays.asList("ORD123456", "张三", "北京市海淀区中关村", "iPhone 12 Pro Max", "2台", "9999元/台", "19998元"));
        data.add(Arrays.asList("ORD123456", "张三", "北京市海淀区中关村", "iPhone 12 Pro Max", "2台", "9999元/台", "19998元"));
        // 将测试数据转换为DataFrame
        List<String> columns = Arrays.asList("订单号", "客户姓名", "客户地址", "商品名称", "商品数量", "商品单价", "订单总金额");
        List<Row> rows = new ArrayList<>();
        for (List<String> rowData : data) {
            Row row = RowFactory.create(rowData.toArray());
            rows.add(row);
        }
        Dataset<Row> df = spark.createDataFrame(rows, createSchema(columns));

        // 写入CSV文件
        String outputPath = "/tmp/test.csv";
        FileUtils.forceDelete(new File(outputPath));
        df.coalesce(1)
                .write()
                .option("header", true)
                .option("delimiter", ",")
                .csv(outputPath);

        // 关闭SparkSession
        spark.close();

        spark.sparkContext().hadoopConfiguration().set("fs.defaultFS", Constant.DEFAULT_FS);
        //spark.sparkContext().hadoopConfiguration().set("user.name", Constant.USER);
        System.setProperty("user.name", "hdfs");

        // 将数据从本地文件系统移动到HDFS
        FileSystem fs = FileSystem.get(spark.sparkContext().hadoopConfiguration());

//        Configuration conf = new Configuration();
//        FileSystem fs = FileSystem.get(new URI(Constant.DEFAULT_FS),conf, Constant.USER);

        Path srcPath = new Path("file://" + outputPath);
        Path dstPath = new Path(Constant.DEFAULT_FS+"/path/to/");
        fs.copyFromLocalFile(srcPath, dstPath);
    }

    private static StructType createSchema(List<String> columns) {
        List<StructField> fields = new ArrayList<>();
        for (String column : columns) {
            StructField field = DataTypes.createStructField(column, DataTypes.StringType, true);
            fields.add(field);
        }
        return DataTypes.createStructType(fields);
    }


}
