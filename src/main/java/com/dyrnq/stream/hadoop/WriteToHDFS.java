package com.dyrnq.stream.hadoop;

import com.dyrnq.stream.Constant;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * HDFS写入文件
 */
public class WriteToHDFS {
    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {

        // 创建一个配置对象
        Configuration conf = new Configuration();
        conf.set("dfs.replication","2");

        // 获取与Hadoop文件系统的连接
        FileSystem fs = FileSystem.get(new URI(Constant.DEFAULT_FS),conf, Constant.USER);

        // 创建一个Path对象，指定要写入的文件路径
        Path filePath = new Path("/path/to/file");

        // 创建输出流
        OutputStream outputStream = fs.create(filePath);

        // 写入数据
        String data = "Hello world!";
        outputStream.write(data.getBytes());

        // 关闭输出流
        outputStream.close();

        // 关闭文件系统连接
        fs.close();
    }
}
