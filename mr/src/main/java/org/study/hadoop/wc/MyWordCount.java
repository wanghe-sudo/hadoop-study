package org.study.hadoop.wc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;

public class MyWordCount {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        // 本地跑，这一步设置hdfs用户
        System.setProperty("HADOOP_USER_NAME", "root");
        Configuration configuration = new Configuration();
        // 本来准备看看这个属性，但是会打印null，因为conf只会在加载完后才会初始化参数
//        System.out.printf("mapreduce.framework.name=%s\n", configuration.get("mapreduce.framework.name"));
        // 本地方式，将需要将hadoop安装在本地，并且需要将hadoop.dll下载下来，放至c:\windows\system32下，
        // 设置环境变量 HADOOP_HOME
//        configuration.set("mapreduce.framework.name","local");
        // 【重点】：让框架知道是windows异构平台
        configuration.set("mapreduce.app-submission.cross-platform", "true");
        // Specify various job-specific parameters
        Job job = Job.getInstance(configuration);
        // 【重点】：设置jar包路径，当程序运行时，将会上传该jar包
        job.setJar("D:\\workspace\\codesource\\java\\hadoop-study\\mr\\target\\mr-1.0.jar");
        TextInputFormat.addInputPath(job, new Path("/data/wc/input"));
        // 【重点】：每次注意，需要换一个路径
        TextOutputFormat.setOutputPath(job, new Path("/data/wc/output5"));
        job.setJarByClass(MyWordCount.class);
        job.setJobName("MyWordCount");
        job.setMapperClass(MyMapper.class);
        // 定义key的class类型
        job.setOutputKeyClass(Text.class);
        // 定义value的class类型
        job.setOutputValueClass(IntWritable.class);
        job.setReducerClass(MyReducer.class);

        // Submit the job, then poll for progress until the job is complete
        job.waitForCompletion(true);
    }
}
