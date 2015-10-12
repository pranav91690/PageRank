import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class AlMapper extends Mapper<LongWritable, Text, Text, Text>{

	@Override
	public void map(LongWritable key, Text value, Context context)	throws IOException, InterruptedException {
		
		String line = value.toString();
		String[] outs = line.split("[\t]");
		String node = outs[0];
		
		double rank = Double.parseDouble(outs[1]);
		long adjLength = outs.length - 2;
		
		StringBuffer sb = new StringBuffer();
		String prefix = "";
		
		
		for (int i=2; i<outs.length; i++){
			double outrank = rank/adjLength;
			context.write(new Text(outs[i]), new Text(outrank+""));
			sb.append(prefix + outs[i]);
			prefix = "\t";
		}
		
		context.write(new Text(node), new Text(sb.toString()));
	}
}
