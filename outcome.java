
public class outcome{
	String agentname;
	String name;
	int value;
	/**
	 * @param name
	 * @param value
	 */
	public outcome(String _agentname,String name, int value) {
		super();
		agentname=_agentname;
		this.name = name;
		this.value = value;
	}
	@Override
	public String toString() {
		return "outcome [agentname="+agentname+", name=" + name + ", value=" + value + "]";
	}




}
