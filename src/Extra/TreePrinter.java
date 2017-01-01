package Extra;

import java.awt.BorderLayout;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import tools.TNode;

/**
 * JTree basic tutorial and example
 * @author wwww.codejava.net
 */

public class TreePrinter extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTree tree;
	private JLabel selectedLabel;

	public TreePrinter(TNode root)
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

		ImageIcon imageIcon = new ImageIcon(TreePrinter.class.getResource("leaf.jpg"));
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


}
