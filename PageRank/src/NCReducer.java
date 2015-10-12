
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class NCReducer extends Reducer<Text, Text, Text, Text> {

	@SuppressWarnings("unused")
	@Override
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

		int count = 0;
		for (Text t : values) {
			count++;
		}

		context.write(new Text(""), new Text(count + ""));
	}

}
