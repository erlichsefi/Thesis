package Extra;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Container;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.abego.treelayout.TreeForTreeLayout;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;
import org.abego.treelayout.util.DefaultTreeForTreeLayout;

import tools.TNode;

/**
 * JTree basic tutorial and example
 * @author wwww.codejava.net
 */

public class GuiTreePrinter extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTree tree;
	private JLabel selectedLabel;

	public GuiTreePrinter(TNode root)
	{
		DefaultMutableTreeNode RootDir = new DefaultMutableTreeNode(root.getPlayerId());

		Queue<TNode> nodes = new LinkedList<TNode>();
		Queue<DefaultMutableTreeNode> print = new LinkedList<DefaultMutableTreeNode>();

		print.add(RootDir);
		nodes.add(root);

		while (!nodes.isEmpty()){

			TNode tempNode=nodes.poll();
			DefaultMutableTreeNode TempRoot=print.poll();

			if (tempNode.getOPtions().length>0){
				for (TNode tq:tempNode.getOPtions()) {
					if (tq!=null){
						nodes.add(tq);
						DefaultMutableTreeNode s=new DefaultMutableTreeNode(tq.StepName());
						TempRoot.add(s);
						print.add(s);
					}
				}
			}


		}

		//create the tree by passing in the root node
		tree = new JTree(RootDir);
		tree.setName(root.getOrder());

		ImageIcon imageIcon = new ImageIcon(GuiTreePrinter.class.getResource("leaf.jpg"));
		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();        
		renderer.setLeafIcon(imageIcon);


		tree.setCellRenderer(renderer);
		tree.setShowsRootHandles(true);
		tree.setRootVisible(true);
		add(new JScrollPane(tree));

		selectedLabel = new JLabel();
		add(selectedLabel, BorderLayout.SOUTH);

		tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				System.out.println(e.getPath().toString());
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				selectedLabel.setText(selectedNode.getUserObject().toString());
			}
		});
		tree.expandRow(3);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("JTree Example");        
		this.setSize(200, 200);
		this.setVisible(true);

	}


	public static void createSampleTree(TNode root1) {

		TextInBox root = new TextInBox(true,"","",null,root1.getPlayerId(), 40, 20);
		root.SetPref(root1.getOrder());
		DefaultTreeForTreeLayout<TextInBox> tree= new DefaultTreeForTreeLayout<TextInBox>(
				root);
		Queue<TNode> nodes = new LinkedList<TNode>();
		Queue<TextInBox> prents = new LinkedList<TextInBox>();

		prents.add(root);
		nodes.add(root1);

		int MaxGap=0;
		while (!nodes.isEmpty()){

			TNode tempNode=nodes.poll();
			TextInBox TempRoot=prents.poll();

			if (tempNode.getOPtions().length>0){
				for (TNode tq:tempNode.getOPtions()) {
					if (tq!=null){
						nodes.add(tq);
						TextInBox myNode=null;
						myNode=new TextInBox(tq.IsSelected(),tq.getReason(),tq.StepName(),tq.getOfferOffered(),tq.getPlayerId(),10,10);


						tree.addChild(TempRoot, myNode);
						prents.add(myNode);
						MaxGap=Math.max(MaxGap, tempNode.StepName().length());
					}
					
				}
			}


		}


		// setup the tree layout configuration
		double gapBetweenLevels = 40;
		double gapBetweenNodes = MaxGap*8;
		DefaultConfiguration<TextInBox> configuration = new DefaultConfiguration<TextInBox>(
				gapBetweenLevels, gapBetweenNodes);

		// create the NodeExtentProvider for TextInBox nodes
		TextInBoxNodeExtentProvider nodeExtentProvider = new TextInBoxNodeExtentProvider();

		// create the layout
		TreeLayout<TextInBox> treeLayout = new TreeLayout<TextInBox>(tree,
				nodeExtentProvider, configuration);

		// Create a panel that draws the nodes and edges and show the panel
		TextInBoxTreePane panel = new TextInBoxTreePane(treeLayout);
		showInDialog1(panel);
	}

	private static void showInDialog1(Canvas chart) {
		JFrame frame = new JFrame("Zoom and Pan Canvas");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.add(chart, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		chart.createBufferStrategy(2);
	}

}
