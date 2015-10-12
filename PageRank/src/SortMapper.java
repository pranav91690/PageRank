
import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class SortMapper extends Mapper<LongWritable, Text, Text, Text>{

	@Override
	public void map(LongWritable key, Text value, Context context)	throws IOException, InterruptedException {
		String[] outs = value.toString().split("\t");

		context.write(new Text(outs[1]), new Text(outs[0]));
		
	}
}
