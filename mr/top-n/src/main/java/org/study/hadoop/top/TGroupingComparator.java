package org.study.hadoop.top;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * 分组比较器
 */
public class TGroupingComparator extends WritableComparator {
    public TGroupingComparator() {
        super(TKey.class, true);
    }

    /**
     * 按照年月分组
     *
     * @param a the first object to be compared.
     * @param b the second object to be compared.
     * @return
     */
    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        TKey k1 = (TKey) a;
        TKey k2 = (TKey) b;
        // 比较年
        int c1 = Integer.compare(k1.getYear(), k2.getYear());
        if (c1 == 0) {
            // 比较月
            return Integer.compare(k1.getMonth(), k2.getMonth());
        }
        return c1;
    }
}
