package org.study.hadoop.top;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

public class TReducer extends Reducer<TKey, IntWritable, Text, IntWritable> {
    Text rkey = new Text();
    IntWritable rval = new IntWritable();


    @Override
    protected void reduce(TKey key, Iterable<IntWritable> values, Reducer<TKey, IntWritable, Text, IntWritable>.Context context) throws IOException, InterruptedException {
        Iterator<IntWritable> iterator = values.iterator();


        // 第一条标识
        int flag = 0;

        int day = 0;
        while (iterator.hasNext()) {
            // 这一步，key也会跟着变化
            IntWritable val = iterator.next();
            // 第一条数据
            if (flag == 0) {
                rkey.set(key.getYear() + "-" + key.getMonth() + "-" + key.getDay());
                rval.set(key.getTemperature());
                context.write(rkey, rval);
                flag++;
                day = key.getDay();
            }

            // 第二条数据
            if (flag != 0 && day != key.getDay()) {
                rkey.set(key.getYear() + "-" + key.getMonth() + "-" + key.getDay());
                rval.set(key.getTemperature());
                context.write(rkey, rval);
                break;
            }


        }

    }
}
