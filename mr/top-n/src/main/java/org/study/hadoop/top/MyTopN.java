package org.study.hadoop.top;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;
import java.net.URI;

public class MyTopN {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

        // 本地跑，这一步设置hdfs用户
        System.setProperty("HADOOP_USER_NAME", "root");

        // 配置
        Configuration conf = new Configuration();
        // 【重点】：让框架知道是windows异构平台
        conf.set("mapreduce.app-submission.cross-platform", "true");
        // conf.set("mapreduce.framework.name", "local");

        // 将-D的参数剔除后，剩余的参数，-D的参数，已经在new configuration的时候注入进去了
        String[] others = new GenericOptionsParser(conf, args).getRemainingArgs();

        Job job = Job.getInstance(conf);

        // job.setJarByClass(MyTopN.class);
        // 需要集群模式
        job.setJar("D:\\workspace\\codesource\\java\\hadoop-study\\mr\\top-n\\target\\top-n-1.0.jar");
        job.setJobName("TopN");

        // 初学者，关注的是client端的代码梳理：因为把这块写明白了，其实你也就真的知道这个作业的开发原理：

        // map环节
        //input 输入 注意导入lib下的新包
        // 【假设】：输入的参数第一个就是文件路径
        TextInputFormat.addInputPath(job, new Path("/data/temperature/input")); // others[0]

        // 【假设】：输入参数的第二个是文件的写出路径
        Path outPath = new Path("/data/temperature/output/"); //  others[1]
        // 这里偷懒了，按道理讲，如果当前路径不为空，则需要直接抛出异常，而不是删除当前路径
        // 这里这样做，只是为了学习mapreduce流程
        if (outPath.getFileSystem(conf).exists(outPath)) {
            outPath.getFileSystem(conf).delete(outPath, true);
        }
        TextOutputFormat.setOutputPath(job, outPath);
        // 以上，是输入输出设置

        //key
        //map
        // 设置mapper类
        job.setMapperClass(TMapper.class);
        // 设置可以被序列化的key对象
        job.setMapOutputKeyClass(TKey.class);
        // map的value，为了简单起见，直接设置为温度 int类型包装类，可以被序列化
        job.setMapOutputValueClass(IntWritable.class);
        //partitioner
        job.setPartitionerClass(TPartitioner.class);
        //sortComparator
        job.setSortComparatorClass(TSortComparator.class);

        //combine 暂时先不处理Combiner
        // job.setCombinerClass();

        // 将hdfs上的文件缓存到datanode节点本地
        // 【因此】 要是再运行mr程序，则需提交到hdfs上，集群模式，on yarn
        URI dictFile = URI.create("/data/temperature/input/dict.txt");
        URI[] uris = {dictFile};

        job.setCacheFiles(uris);

        // reduce环节
        //groupingComparator
        job.setGroupingComparatorClass(TGroupingComparator.class);
        //reduce
        job.setReducerClass(TReducer.class);

        // job提交
        job.waitForCompletion(true);
    }
}
