import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class SortComparator extends WritableComparator{
	
	protected SortComparator(){
		super(Text.class, true);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int compare(WritableComparable w1, WritableComparable w2){
		Double d1 = Double.parseDouble(((Text) w1).toString());
		Double d2 = Double.parseDouble(((Text) w2).toString());
		return -1*d1.compareTo(d2);
	}
}
