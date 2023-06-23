package org.study.hadoop.top;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * 分区器.
 */
public class TPartitioner extends Partitioner<TKey, IntWritable> {

    @Override
    public int getPartition(TKey tKey, IntWritable intWritable, int numPartitions) {
        // 分区器不能太复杂，每一个map都会调用分区器，因此如果太复杂，会导致性能变差
        // 分区器只需要获取相同的key为一组，就达到了目的！

        // 最简单的分区器【这里需要注意，这里可能会造成数据倾斜，早年的温度数据肯定少，近几年随着科技的进步，数据量多，就会有倾斜，其他业务也需要考虑具体的问题】
        return tKey.getYear() % numPartitions;
    }


}
