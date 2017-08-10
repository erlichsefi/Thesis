package DrawTree;

import java.awt.BorderLayout;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.JFrame;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;
import org.abego.treelayout.util.DefaultTreeForTreeLayout;

import Negotiation.Fullinfo;
import tools.Agent;
import tools.options;


public class DecisionTreePrinter 
{
	private static final String FRAME_HEADLINE="DecisionTree";

	private static final int ROOT_WIDTH=40;
	private static final int ROOT_HIGHT=20;
	private static final double GAP_BETWEEN_LEVLES = 40;
	private static final double GAP_BETWEEN_NODES = 64;
	private static final int NUM_OF_BUFFERS_STRATGTY=2;
	DrawBoxTreePane panel;
	
	public  DecisionTreePrinter(DecisionNode root1) {

		DrawBox root = new DrawBox(true,root1.getPlayerId(), ROOT_WIDTH, ROOT_HIGHT);
		root.SetPref(root1.getPrefrenceInNode());
		root.text=root1.getResult();
		
		DefaultTreeForTreeLayout<DrawBox> tree= new DefaultTreeForTreeLayout<DrawBox>(
				root);


		Queue<DecisionNode> nodes = new LinkedList<DecisionNode>();
		Queue<DrawBox> prents = new LinkedList<DrawBox>();
		prents.add(root);
		nodes.add(root1);

		while (!nodes.isEmpty()){

			DecisionNode TempNode=nodes.poll();
			DrawBox TempRoot=prents.poll();

			for (DecisionNode child:TempNode.getOptions()) {
				if (child!=null){
					//create the node 
					DrawBox myNode=new DrawBox(child.IsSelected(),child.getReason(),"R="+child.getResult(),child.getOfferGotten(),child.getPlayerId(),10,10);
					//adding it to the tree
					tree.addChild(TempRoot, myNode);
					//
					prents.add(myNode);
					nodes.add(child);
				}
			}

		}


		// setup the tree layout configuration
		DefaultConfiguration<DrawBox> configuration = new DefaultConfiguration<DrawBox>(
				GAP_BETWEEN_LEVLES, GAP_BETWEEN_NODES);

		// create the NodeExtentProvider for TextInBox nodes
		DrawBoxNodeExtentProvider nodeExtentProvider = new DrawBoxNodeExtentProvider();

		// create the layout
		TreeLayout<DrawBox> treeLayout = new TreeLayout<DrawBox>(tree,
				nodeExtentProvider, configuration);

		// Create a panel that draws the nodes and edges and show the panel
		panel = new DrawBoxTreePane(treeLayout);
	}

	public  void show() {
		JFrame frame = new JFrame(FRAME_HEADLINE);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.add(panel, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		panel.createBufferStrategy(NUM_OF_BUFFERS_STRATGTY);
	}

	
	public static void main(String[] args) {
		String[] out =new String[]{"o1","o2","o3","o4","o5","o6"};
		Agent p1=new Agent("P1","o1<o2<o3<o4<o6<o5");
		Agent p2=new Agent("P2","o3<o2<o6<o5<o4<o1");
		options o1=Fullinfo.FindBestByPrefernce(out,p2,p1);
		new DecisionTreePrinter(o1.getTree()).show();
	}
}
