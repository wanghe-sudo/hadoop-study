package org.study.hadoop.wc;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

public class MyMapper extends Mapper<Object, Text, Text, IntWritable> {
    // hadoop有自己的数据类型，可以序列化，这个很重要
    // 排序和比较是必须有的：字典序，数值序，因此需要实现比较器接口，自己实现比较逻辑
    private final static IntWritable one = new IntWritable(1);
    private final Text word = new Text();

    /**
     *
     * @param key 偏移量
     * @param value
     * @param context
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    protected void map(Object key, Text value, Mapper<Object, Text, Text, IntWritable>.Context context) throws IOException, InterruptedException {
        StringTokenizer itr = new StringTokenizer(value.toString());
        while (itr.hasMoreTokens()) {
            word.set(itr.nextToken());
            // 这里一定存在序列化
            context.write(word, one);
        }
    }
}
