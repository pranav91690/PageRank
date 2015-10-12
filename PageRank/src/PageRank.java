import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


public class PageRank extends Configured implements Tool {

	public static void main(String[] args) throws Exception {
		
		int status = ToolRunner.run(new Configuration(), new PageRank(), args);
		
		System.exit(status);
	}

	public int run(String args[]) throws Exception {
			
			String inputxml = args[0];
			String rootpath = "s3://" + args[1];
			// Assuming we have a results folder inside the bucket
			String iterpath = "s3://" + rootpath + "/results/iter";

			MapReduceJob(inputxml, rootpath + "/results/out1", RLMapper.class, RLReducer.class, 1);
			
			MapReduceJob(rootpath + "/results/out1", rootpath + "/results/out2", LMapper.class, LReducer.class, 0);
			
			MapReduceJob(rootpath + "/results/out2", rootpath + "/results/out3", NCMapper.class, NCReducer.class, 0);
			
			long N = getNodeCount(rootpath + "/results/out3");
			
			MapReduceJob(rootpath + "/results/out2", iterpath + "0", AppendRankMapper.class, AppendRankReducer.class, N);

			
			for (int iter = 1; iter < 9; iter++) {
				
				MapReduceJob(iterpath + (iter - 1), iterpath + iter, AlMapper.class, AlReducer.class, N);
				
			}
			
			MapReduceJob(iterpath + 8, rootpath + "results/out5", SortMapper.class, SortReducer.class, N);
			
			return 0;

	}
	
	public static long getNodeCount(String fpath) throws Exception{
		Scanner sc = new Scanner(new File(fpath + "/part-r-00000"));
		try{
			String str = sc.nextLine();
			return Long.parseLong(str.trim());
		}
		finally{
			sc.close();
		}
	}

	public static boolean MapReduceJob(String inputPath, String outputPath, Class mapperClass, Class reducerClass, long N) throws IOException,
			ClassNotFoundException, InterruptedException {
		
		Configuration conf = new Configuration();
		conf.set("nodecount", ""+N);
		
		if (N == 1) {
			conf.set("xmlinput.start", "<page>");
			conf.set("xmlinput.end", "</page>");
			conf.set(
					"io.serializations",
					"org.apache.hadoop.io.serializer.JavaSerialization,org.apache.hadoop.io.serializer.WritableSerialization");
		}

		Job job = Job.getInstance(conf);
		job.setJarByClass(PageRank.class);
		
		if (N == 5){
			job.setSortComparatorClass(SortComparator.class);
			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(Text.class);
		}
		
		job.setMapperClass(mapperClass);
		job.setReducerClass(reducerClass);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		FileInputFormat.addInputPath(job, new Path(inputPath));
		job.setInputFormatClass(TextInputFormat.class);

		FileOutputFormat.setOutputPath(job, new Path(outputPath));
		job.setOutputFormatClass(TextOutputFormat.class);
		
		if (N == 1){
			job.setInputFormatClass(XmlInputFormat.class);
		}

		return job.waitForCompletion(true);

	}

}
