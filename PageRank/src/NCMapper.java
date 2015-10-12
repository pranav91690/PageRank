
import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class NCMapper extends Mapper<LongWritable, Text, Text, Text> {
	
	@Override
	public void map(LongWritable key, Text value, Context context)	throws IOException, InterruptedException {
		
		String[] titles = value.toString().split(" ");
		String val = titles[0];
		
		context.write(new Text("count"), new Text(val));
	}
}
