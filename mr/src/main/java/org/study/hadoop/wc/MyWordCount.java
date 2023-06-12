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
        Configuration configuration = new Configuration();

        // Specify various job-specific parameters
        Job job = Job.getInstance(configuration);
        TextInputFormat.addInputPath(job, new Path("/data/wc/input"));

        TextOutputFormat.setOutputPath(job, new Path("/data/wc/output"));

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