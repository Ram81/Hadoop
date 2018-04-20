import java.io.*;
import java.util.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

public class CharReduce extends Reducer<Text, IntWritable, Text, IntWritable>
{
	public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
		int cnt = 0;
		IntWritable res = new IntWritable();
		for(IntWritable itr : values) {
			cnt += itr.get();
		}
		res.set(cnt);
		context.write(key, res);
	}
}