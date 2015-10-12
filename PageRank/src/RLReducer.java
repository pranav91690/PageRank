
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class RLReducer extends Reducer<Text, Text, Text, Text> {

	public void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {

		boolean found = false;
		StringBuffer sb = new StringBuffer();
		for (Text val : values) {
			if (val.toString().equals("===")) {
				found = true;
			} else {
				sb.append(" " + val.toString());
			}
		}
		boolean debug = false;
		if (found) {
			if (debug)
				System.out.println(key + " === " + sb.toString());
			context.write(key, new Text(sb.toString()));
		} else {
			context.write(new Text("==="), new Text(sb.toString()));
		}

	}

}
