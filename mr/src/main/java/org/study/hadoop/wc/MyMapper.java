package org.study.hadoop.wc;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MyMapper extends Mapper<Object, Text, Text, IntWritable> {


    public MyMapper() {
        super();
    }
}
