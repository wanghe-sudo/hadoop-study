package org.study.hadoop.top;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Mapper<LongWritable, Text,TKey,Integer>
 * 前两个是输入的类型，使用框架的内置
 * 后两个是自定义输出的类型，由自己定义
 * <p>
 * <p>
 * 【重要】：重写map方法
 */
public class TMapper extends Mapper<LongWritable, Text, TKey, IntWritable> {
    // map方法会被调用多次，将变量定义在外面，可以减少new对象的消耗，【这里不用担心同一引用的问题！因为框架每次都会序列化这组key和value！】
    // map输出的key和value都会被序列化，到buffer缓冲区中，默认大小是100M，这是一个【环形缓冲区】，很有意思的设计！
    TKey mkey = new TKey();
    IntWritable mvalue = new IntWritable();

    @Override
    protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, TKey, IntWritable>.Context context) throws IOException, InterruptedException {
//        super.map(key, value, context);

        String[] strs = StringUtils.split(value.toString(), ',');
        LocalDateTime localDateTime = formatFromStr(strs[0]);
        mkey.setYear(localDateTime.getYear());
        mkey.setMonth(localDateTime.getMonthValue());
        mkey.setDay(localDateTime.getDayOfMonth());
        mkey.setTemperature(Integer.parseInt(strs[2]));
        // 可以不要value！
        mvalue.set(Integer.parseInt(strs[2]));
        // 写入上下文
        context.write(mkey, mvalue);
    }

    private static LocalDateTime formatFromStr(String dateTime) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateTime, pattern);
    }

}
