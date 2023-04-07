package com.dyrnq.stream.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.net.URI;

/**
 * HDFS文件重命名
 */
public class HdfsRename {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI(Constant.DEFAULT_FS),conf, Constant.USER);

        String oldFilePath = "/path/to/file";
        String newFilePath = "/path/to/new_file";

        Path hdfsOldPath = new Path(oldFilePath);
        Path hdfsNewPath = new Path(newFilePath);

        boolean success = fs.rename(hdfsOldPath, hdfsNewPath);
        if (success) {
            System.out.println(oldFilePath + " is renamed to " + newFilePath);
        } else {
            System.out.println("Failed to rename " + oldFilePath);
        }

        fs.close();
    }
}