package com.dyrnq.stream.hadoop;

import com.dyrnq.stream.Constant;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URI;

/**
 * HDFS拷贝文件
 */
public class HdfsLocalCopy {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI(Constant.DEFAULT_FS),conf, Constant.USER);

        String srcLocalPath = "/tmp/file";
        String srcLocalPath2 = "/tmp/file2";
        String dstPath = "/path/to/destination_file2";

        OutputStream outputStream= new FileOutputStream(srcLocalPath,true);
        IOUtils.write("i am local file",outputStream,"UTF-8");

        fs.copyFromLocalFile(false, new Path(srcLocalPath), new Path(dstPath));
        fs.copyToLocalFile(false,new Path(dstPath),new Path(srcLocalPath2));
        System.out.println(IOUtils.toString(new FileInputStream(srcLocalPath2),"UTF-8"));
        fs.close();
    }
}
