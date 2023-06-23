package org.study.hadoop.top;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * 排序比较器
 * <p>
 * 首先，比较器会把序列化的数据反序列化成对象， 然后调compare方法
 * 因此我们会看到WritableComparator如如此调用的：
 * compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) -> compare(WritableComparable a, WritableComparable b)
 * 因此我们需要重写compare(WritableComparable a, WritableComparable b)
 * 但是需要注意，反序列化后，应该变成我么们期待的TKey类型，因此需要重写构造器，来设置这个点
 */
public class TSortComparator extends WritableComparator {
    public TSortComparator() {
        // 调父类的构造器，这时，才
        super(TKey.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        TKey k1 = (TKey) a;
        TKey k2 = (TKey) b;
        // 比较年
        int c1 = Integer.compare(k1.getYear(), k2.getYear());
        if (c1 == 0) {
            // 比较月
            int c2 = Integer.compare(k1.getMonth(), k2.getMonth());
            if (c2 == 0) {
                // 比较温度
                return -Integer.compare(k1.getTemperature(), k2.getTemperature());
            }
            return c2;
        }
        return c1;
    }
}