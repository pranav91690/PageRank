import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class AlReducer extends Reducer<Text, Text, Text, Text>{

	@Override
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		
		long N = Long.parseLong(context.getConfiguration().get("nodecount"));
		double d = 0.85;
		double rank = (1-d)/N;
		double remaining = 0;
		
		StringBuffer sb = new StringBuffer();
		String prefix = "";
		for (Text t: values){
			try{
				remaining += Double.parseDouble(t.toString());
			}catch(Exception e){
				sb.append(prefix + t.toString());
				prefix = "\t";
			}
		}
		
		rank += d*remaining;
		
		context.write(key, new Text(rank + "\t" + sb.toString()));
	}
}
