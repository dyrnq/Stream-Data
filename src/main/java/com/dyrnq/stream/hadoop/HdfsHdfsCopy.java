package com.dyrnq.stream.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;

import java.net.URI;

/**
 * HDFS拷贝文件
 */
public class HdfsHdfsCopy {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI(Constant.DEFAULT_FS),conf, Constant.USER);

        String srcPath = "/path/to/file";
        String dstPath = "/path/to/destination_file";

        Path hdfsSrcPath = new Path(srcPath);
        Path hdfsDstPath = new Path(dstPath);
        FileUtil.copy(fs, hdfsSrcPath, fs, hdfsDstPath, false, conf);
        fs.close();
    }
}
