package com.dyrnq.stream.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * HDFS检测路径是文件还是目录
 */
import java.net.URI;

public class HdfsCheck {


    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI(Constant.DEFAULT_FS),conf, Constant.USER);

        String path = "/path/to/csv";
        Path hdfsPath = new Path(path);

        if (fs.isFile(hdfsPath)) {
            System.out.println(path + " is a file.");
        } else if (fs.isDirectory(hdfsPath)) {
            System.out.println(path + " is a directory.");
        } else {
            System.out.println(path + " does not exist.");
        }

        fs.close();
    }
}