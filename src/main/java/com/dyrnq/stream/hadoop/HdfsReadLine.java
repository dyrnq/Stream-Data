package com.dyrnq.stream.hadoop;

import com.dyrnq.stream.Constant;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * HDFS读取文件
 */
public class HdfsReadLine {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI(Constant.DEFAULT_FS),conf, Constant.USER);

        String filePath = "/path/to/file";
        Path hdfsPath = new Path(filePath);

        BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(hdfsPath)));

        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }

        IOUtils.closeStream(br);
        fs.close();
    }
}
