import java.io.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

public class CharMap extends Mapper<LongWritable, Text, Text, IntWritable>
{
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String line = value.toString();
		char[] carr = line.toCharArray();
		for(char ch : carr) {
			context.write(new Text(String.valueOf(ch)), new IntWritable(1));
		} 
	}
}