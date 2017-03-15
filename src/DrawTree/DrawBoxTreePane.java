/*
 * [The "BSD license"]
 * Copyright (c) 2011, abego Software GmbH, Germany (http://www.abego.org)
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of the abego Software GmbH nor the names of its 
 *    contributors may be used to endorse or promote products derived from this 
 *    software without specific prior written permission.
 *    
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package DrawTree;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;


import org.abego.treelayout.TreeForTreeLayout;
import org.abego.treelayout.TreeLayout;

/**
 * A JComponent displaying a tree of TextInBoxes, given by a {@link TreeLayout}.
 * 
 * @author Udo Borkowski (ub@abego.org)
 */
public class DrawBoxTreePane extends Canvas {

	private static final long serialVersionUID = 1L;
	private ZoomAndPanListener zoomAndPanListener;
	private final TreeLayout<DrawBox> treeLayout;

	private boolean init = true;
	
	private final static int ARC_SIZE = 30;
	private final static Color BOX_COLOR = Color.orange;
	private final static Color BORDER_COLOR = Color.darkGray;
	private final static Color TEXT_COLOR = Color.black;
	private final static Color CHOSEEN_COLOR = Color.GREEN;
	private final static Color NON_CHOSEEN_COLOR = Color.RED;

	private final static int Dimension_WIDTH=600;
	private final static int Dimension_HIGTH=500;

	public DrawBoxTreePane(TreeLayout<DrawBox> treeLayout) {
		this.treeLayout = treeLayout;
		this.zoomAndPanListener = new ZoomAndPanListener(this);
		this.addMouseListener(zoomAndPanListener);
		this.addMouseMotionListener(zoomAndPanListener);
		this.addMouseWheelListener(zoomAndPanListener);
		Dimension size = treeLayout.getBounds().getBounds().getSize();
		setPreferredSize(size);
	}
	
	private TreeForTreeLayout<DrawBox> getTree() {
		return treeLayout.getTree();
	}

	private Iterable<DrawBox> getChildren(DrawBox parent) {
		return getTree().getChildren(parent);
	}

	private Rectangle2D.Double getBoundsOfNode(DrawBox node) {
		return treeLayout.getNodeBounds().get(node);
	}


	public Dimension getPreferredSize() {
		return new Dimension(Dimension_WIDTH, Dimension_HIGTH);
	}


	private void paintEdges(Graphics g, DrawBox parent) {
		if (!getTree().isLeaf(parent)) {
			
			Rectangle2D.Double b1 = getBoundsOfNode(parent);
			double x1 = b1.getCenterX();
			double y1 = b1.getCenterY();
			for (DrawBox child : getChildren(parent)) {
			
				Rectangle2D.Double b2 = getBoundsOfNode(child);
				
				paintTextOverEdge(g,b1,b2,child);
				
				if (child.choosen){
					g.setColor(CHOSEEN_COLOR);
				}
				else{
					g.setColor(NON_CHOSEEN_COLOR);

				}
				g.drawLine((int) x1, (int) y1, (int) b2.getCenterX(),
						(int) b2.getCenterY());

				paintEdges(g, child);
			}
		}
	}

	private void paintBox(Graphics g, DrawBox textInBox) {
		// draw the box in the background
		g.setColor(BOX_COLOR);
		Rectangle2D.Double box = getBoundsOfNode(textInBox);
		g.fillRoundRect((int) box.x, (int) box.y, (int) box.width - 1,
				(int) box.height - 1, ARC_SIZE, ARC_SIZE);
		g.setColor(BORDER_COLOR);
		g.drawRoundRect((int) box.x, (int) box.y, (int) box.width - 1,
				(int) box.height - 1, ARC_SIZE, ARC_SIZE);

		//draw the text to show inside the box
		paintTextInBoxComment(g,box,textInBox);

		//draw the result from the branch
		paintTextNearBoxComment(g,box,textInBox);
	}
	
	
	@Override
	public void paint(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		if (init) {
			// Initialize the viewport by moving the origin to the center of the window,
			// and inverting the y-axis to point upwards.
			init = false;
			Dimension d = getSize();
			int xc = d.width / 2;
			int yc = d.height / 2;
			g.translate(xc, yc);
			// Save the viewport to be updated by the ZoomAndPanListener
			zoomAndPanListener.setCoordTransform(g.getTransform());
		} else {
			// Restore the viewport after it was updated by the ZoomAndPanListener
			g.setTransform(zoomAndPanListener.getCoordTransform());
		}


		super.paint(g);
		DrawBox parent=getTree().getRoot();
		paintEdges(g, parent);
		paintTextComment(g,parent);
		
		// paint the boxes
		for (DrawBox textInBox : treeLayout.getNodeBounds().keySet()) {
			paintBox(g, textInBox);
		}


	}
	private void paintTextOverEdge(Graphics g,Rectangle2D.Double b1, Rectangle2D.Double b2,DrawBox child) {
		String OverLineText=child.getOverlineText();
		if (OverLineText!=null){
			g.setColor(TEXT_COLOR);
			g.drawString(OverLineText,(int)((b1.getCenterX()+ b2.getCenterX())/2),(int)(b1.getCenterY()+ b2.getCenterY())/2);
		}
	}
	private void paintTextComment(Graphics g, DrawBox parent) {
		if (parent.order!=null){
			g.setColor(TEXT_COLOR);
			String[] lines = parent.order.split("\n");
			FontMetrics m = getFontMetrics(getFont());
			int y=10;
			for (int i = 0; i < lines.length; i++) {
				g.drawString(lines[i], 1, y);
				y += m.getHeight();
			}
		}
	}
	
	private void paintTextInBoxComment(Graphics g,Rectangle2D.Double box, DrawBox textInBox) {
		String OverLineText=textInBox.getInBoxText();
		if (OverLineText!=null){
			g.setColor(TEXT_COLOR);
			g.setFont(new Font("TimesRoman", Font.PLAIN,(int) (Math.min(box.width,box.height)-4)));
			int x=((int) box.x+1);
			int y=((int) box.y)+((int) box.height-3);
			g.drawString(OverLineText,  x,y);
		}	
	}
	private void paintTextNearBoxComment(Graphics g,Rectangle2D.Double box, DrawBox textInBox) {
		g.setColor(TEXT_COLOR);
		FontMetrics m = getFontMetrics(getFont());
		int x = (int) box.x + ARC_SIZE / 2;
		int y = (int) box.y + m.getAscent()/2 + m.getLeading() + 1;
		g.drawString(textInBox.text, x, y);

	}
}