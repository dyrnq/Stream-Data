package com.dyrnq.stream.hadoop;

import com.dyrnq.stream.Constant;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.URI;
/**
 * HDFS 追加文字到文本中
 */
public class HdfsAppendText {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI(Constant.DEFAULT_FS),conf, Constant.USER);

        String filePath = "/path/to/file";
        Path hdfsPath = new Path(filePath);

        if (!fs.exists(hdfsPath)) {
            System.out.println(filePath + " does not exist.");
            return;
        }

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fs.append(hdfsPath)));

        String text = "This is some appended text.";
        bw.write(text);
        bw.newLine();

        bw.close();
        fs.close();
    }
}
