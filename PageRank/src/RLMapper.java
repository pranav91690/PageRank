import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class RLMapper extends
		Mapper<LongWritable, Text, Text, Text> {

	@Override
	public void map(LongWritable key, Text value1, Context context)

	throws IOException, InterruptedException {

		String xmlString = value1.toString();
		SAXBuilder builder = new SAXBuilder();
		Reader in = new StringReader(xmlString);
		try {
			Set<String> outlinkSet = new HashSet<String>();
			Document doc = builder.build(in);
			Element root = doc.getRootElement();

			String title = root.getChild("title").getTextTrim()
					.replace(" ", "_");
			String text = root.getChild("revision").getChild("text")
					.getTextTrim();

			String pattern = "\\[\\[(.*?)\\]\\]";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(text);

			while (m.find()) {
				outlinkSet.add(m.group(1).split("\\|")[0].replace(" ", "_"));
			}

			context.write(new Text(title), new Text("==="));

			for (String outlink : outlinkSet) {
				context.write(new Text(outlink), new Text(title));
			}

		} catch (JDOMException ex) {
			Logger.getLogger(RLMapper.class.getName()).log(
					Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(RLMapper.class.getName()).log(
					Level.SEVERE, null, ex);
		}

	}
}
