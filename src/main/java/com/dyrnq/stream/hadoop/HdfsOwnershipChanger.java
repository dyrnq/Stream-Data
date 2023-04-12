package com.dyrnq.stream.hadoop;

import com.dyrnq.stream.Constant;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;

import java.net.URI;

/**
 * 更改HDFS的用户
 */
public class HdfsOwnershipChanger {
    public static void main(String[] args) throws Exception {
        String hdfsPath = "/path/to/test.csv";
        String newOwner = "hduser";

        //UserGroupInformation ugi = UserGroupInformation.createRemoteUser(newOwner);

        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI(Constant.DEFAULT_FS),conf, Constant.USER);
        Path path = new Path(hdfsPath);

        if (fs.exists(path)) {
            fs.setOwner(path, newOwner, null);
            System.out.println("Ownership of HDFS directory " + hdfsPath + " changed to " + newOwner);
        } else {
            System.out.println("HDFS directory " + hdfsPath + " does not exist");
        }
    }
}
