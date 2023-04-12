package com.dyrnq.stream.hadoop;

import com.dyrnq.stream.Constant;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.net.URI;

/**
 * HDFS循环目录获取文件名
 */
public class HdfsListFiles {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI(Constant.DEFAULT_FS),conf, Constant.USER);

        String dirPath = "/path/to";
        Path hdfsDirPath = new Path(dirPath);

        if (fs.isDirectory(hdfsDirPath)) {
            for (FileStatus fileStatus : fs.listStatus(hdfsDirPath)) {
                if (fileStatus.isFile()) {
                    System.out.println(fileStatus.getPath().getName());
                }
            }
        } else {
            System.out.println(dirPath + " is not a directory.");
        }

        fs.close();
    }
}
