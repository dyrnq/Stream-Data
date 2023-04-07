package com.dyrnq.stream.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.net.URI;

/**
 * HDFS判断文件是否存在
 */
public class HdfsFileExists {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI(Constant.DEFAULT_FS),conf, Constant.USER);

        String filePath = "/path/to/file";
        Path hdfsPath = new Path(filePath);

        boolean exists = fs.exists(hdfsPath);
        if (exists) {
            System.out.println(filePath + " exists.");
        } else {
            System.out.println(filePath + " does not exist.");
        }

        fs.close();
    }
}
