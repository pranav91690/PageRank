import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class AppendRankReducer extends Reducer<Text, Text, Text, Text>{
	
	@Override
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		StringBuffer sb = new StringBuffer();
		boolean first = true;
		for (Text v : values) {
			//System.out.println(v);
			if (first) {
				sb.append(v.toString().replaceAll("[ \t]+", ";"));
				first = false;
			} else
				sb.append("\t" + v.toString().replaceAll("[ \t]+", ";"));
		}
		
		String k = key.toString().replaceAll("[ \t]+", "");
		String[] outs = sb.toString().split(";");
		List<String> outlinks = new ArrayList<String>();
		double rank = 0;
		
		for (String out: outs) {
			try {
				rank = Double.parseDouble(out);
			} catch(Exception e){
				outlinks.add(out);
			}
		}
		
		sb = new StringBuffer();
		sb.append(rank);
		for(String out: outlinks){
			if (out.length()>0)
				sb.append("\t" + out);
		}
		
		context.write(new Text(k), new Text(sb.toString()));
	}


}
