import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class LReducer extends Reducer<Text, Text, Text, Text> {

	@Override
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

		StringBuffer sb = new StringBuffer();
		for (Text v : values) {
			sb.append(" " + v.toString());
		}

		context.write(key, new Text(sb.toString()));
	}

}
