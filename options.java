
public class options{
	String prefrence;
	String startingOfer;
	/**
	 * @param prefrence
	 * @param startingOfer
	 */
	public options(String prefrence, String startingOfer) {
		super();
		this.prefrence = prefrence;
		this.startingOfer = startingOfer;
	}
	@Override
	public String toString() {
		return "options [prefrence=" + prefrence + ", startingOfer=" + startingOfer + "]";
	}
	
}