import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class AppendRankMapper extends Mapper<LongWritable, Text, Text, Text>{

	@Override
	public void map(LongWritable key, Text value, Context context)	throws IOException, InterruptedException {
		long N = Integer.parseInt(context.getConfiguration().get("nodecount"));
		double initrank = (double) 1/N;
		String[] s = value.toString().split("[ ]+");
		String out = "";
		for (int i=1; i<s.length; i++){
			String str = s[i].trim();
			if (str.length() > 0)
				out += str + "\t";
		}
		context.write(new Text(s[0]), new Text(out + "\t" + initrank));
	}
}
