import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class CharCount {
	public static void main(String args[]) throws Exception {
		Configuration c = new Configuration();
		@SuppressWarnings("deprecation")
		Job job = new Job(c, "CharCount");
		job.setJarByClass(CharCount.class);
		job.setMapperClass(CharMap.class);
		job.setReducerClass(CharReduce.class);
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);	
		job.setMapOutputKeyClass(Text.class);	
		job.setMapOutputValueClass(IntWritable.class);	
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		System.exit(job.waitForCompletion(true) ? 1 : 0);
	}
}