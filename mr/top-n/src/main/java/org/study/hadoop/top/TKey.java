package org.study.hadoop.top;

import lombok.Data;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

@Data
public class TKey implements WritableComparable<TKey> {
    private int year;
    private int month;
    private int day;
    /**
     * 温度
     */
    private int temperature;

    /**
     * 码值
     */
    private String location;

    /**
     * 比较方法
     *
     * @param that the object to be compared.
     * @return
     */
    @Override
    public int compareTo(TKey that) {
        //为了这这个案例体现api开发，因此，按照时间正序排序【这也是通用的逻辑】
        // 但是我们最终的需求是，按温度倒序，因此，后面我们还需要实现一个SortComparator
        int c1 = Integer.compare(this.year, that.getYear());
        if (c1 == 0) {
            int c2 = Integer.compare(this.month, that.getMonth());
            if (c2 == 0) {
                return Integer.compare(this.day, that.getDay());
            }
            return c2;
        }
        return c1;
    }

    /**
     * 序列化写
     *
     * @param out <code>DataOuput</code> to serialize this object into.
     * @throws IOException
     */
    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(year);
        out.writeInt(month);
        out.writeInt(day);
        out.writeInt(temperature);
        out.writeUTF(location);

    }

    /**
     * 读取序列化为对象
     *
     * @param in <code>DataInput</code> to deseriablize this object from.
     * @throws IOException
     */
    @Override
    public void readFields(DataInput in) throws IOException {
        this.year = in.readInt();
        this.month = in.readInt();
        this.day = in.readInt();
        this.temperature = in.readInt();
        this.location = in.readUTF();
    }
}
