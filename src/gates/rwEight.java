package gates;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

import javax.swing.JOptionPane;

import logicsim.Gate;
import logicsim.I18N;
import logicsim.LSLevelEvent;
import logicsim.LSProperties;
import logicsim.Pin;

/**
 * Acknowledge-Request Buffer for asynchronous pipelines
 * 
 * @author Andreas Tetzl
 * @author Peter Gabriel
 * @author Matthew Lister April 2020
 */
public class rwEight extends Gate {
	static final long serialVersionUID = 4521959944440523564L;

	int inpNo = 12;
	int outNo = 11;
	
	public rwEight() {
		super("basic");
		label = "rwEight";
		labelOffsetX = 100;
		labelOffsetY = 60;
		type = "rwEight";
		
		width = 280;
		height = 160;
		createInputs(inpNo);
		createOutputs(outNo + 8);
		
		int offset = 40;

		getPin(0).label = "MreadRack";
		getPin(1).label = "MreadLreq";
		getPin(2).label = "Minit";
		getPin(3).label = "MwriteLreq";
		getPin(4).label = "MwriteLdata4_1";
		getPin(5).label = "MwriteLdata4_0";
		getPin(6).label = "MwriteLdata3_1";
		getPin(7).label = "MwriteLdata3_0";
		getPin(8).label = "MwriteLdata2_1";
		getPin(9).label = "MwriteLdata2_0";
		getPin(10).label = "MwriteLdata1_1";
		getPin(11).label = "MwriteLdata1_0";
		
		getPin(inpNo).label = "MRdata4_1";
		getPin(inpNo + 1).label = "MRdata4_0";
		getPin(inpNo + 2).label = "MRdata3_1";
		getPin(inpNo + 3).label = "MRdata3_0";
		getPin(inpNo + 4).label = "MRdata2_1";
		getPin(inpNo + 5).label = "MRdata2_0";
		getPin(inpNo + 6).label = "MRdata1_1";
		getPin(inpNo + 7).label = "MRdata1_0";
		getPin(inpNo + 8).label = "MreadLack";
		getPin(inpNo + 9).label = "MreadRreq";
		getPin(inpNo + 10).label = "MwriteLack";
		
		getPin(inpNo + outNo).label = "_4_1";
		getPin(inpNo + outNo + 1).label = "_4_0";
		getPin(inpNo + outNo + 2).label = "_3_1";
		getPin(inpNo + outNo + 3).label = "_3_0";
		getPin(inpNo + outNo + 4).label = "_2_1";
		getPin(inpNo + outNo + 5).label = "_2_0";
		getPin(inpNo + outNo + 6).label = "_1_1";
		getPin(inpNo + outNo + 7).label = "_1_0";
		getPin(inpNo + outNo).moveTo(getX() + 190, getY() + 10);
		getPin(inpNo + outNo + 1).moveTo(getX() + 190, getY() + 20);
		getPin(inpNo + outNo + 2).moveTo(getX() + 190, getY() + 30);
		getPin(inpNo + outNo + 3).moveTo(getX() + 190, getY() + 40);
		getPin(inpNo + outNo + 4).moveTo(getX() + 190, getY() + 50);
		getPin(inpNo + outNo + 5).moveTo(getX() + 190, getY() + 60);
		getPin(inpNo + outNo + 6).moveTo(getX() + 190, getY() + 70);
		getPin(inpNo + outNo + 7).moveTo(getX() + 190, getY() + 80);
		
		reset();
		loadProperties();
	}
	
	/*static final String TEXT = "text";

	static final String TEXT_DEFAULT = "<Label>";
	
	String text;

	
	@Override
	protected void loadProperties() {
		text = getPropertyWithDefault(TEXT, TEXT_DEFAULT);
	}
	
	@Override
	public boolean hasPropertiesUI() {
		return true;
	}
	
	@Override
	public boolean showPropertiesUI(Component frame) {
		String h = (String) JOptionPane.showInputDialog(frame, I18N.getString(type, "ui.text"),
				I18N.getString(type, "ui.title"), JOptionPane.QUESTION_MESSAGE, null, null, text);
		if (h != null && h.length() > 0) {
			text = h;
			setProperty(TEXT, text);
		}
		return true;
	}*/

	@Override
	public void draw(Graphics2D g2) {
		super.draw(g2);
		AffineTransform old = null;
		xc = getX() + width / 2;
		yc = getY() + height / 2;
		if (rotate90 != 0) {
			old = g2.getTransform();
			g2.rotate(Math.toRadians(rotate90 * 90), xc, yc);
		}
		g2.setStroke(new BasicStroke(1));
		//drawBounds(g2);
		g2.setPaint(Color.black);
		drawFrame(g2);
		if (rotate90 != 0) {
			g2.setTransform(old);
		}
		drawIO(g2);
		g2.drawString(text, getX() + 100, getY() + 100);
	}

	
	@Override
	public void simulate() {
		super.simulate();

		/*boolean in4_1 = getPin(inpNo + outNo + 0).getInternalLevel();
		boolean in4_0 = getPin(inpNo + outNo + 1).getInternalLevel();
		boolean in3_1 = getPin(inpNo + outNo + 2).getInternalLevel();
		boolean in3_0 = getPin(inpNo + outNo + 3).getInternalLevel();
		boolean in2_1 = getPin(inpNo + outNo + 4).getInternalLevel();
		boolean in2_0 = getPin(inpNo + outNo + 5).getInternalLevel();
		boolean in1_1 = getPin(inpNo + outNo + 6).getInternalLevel();
		boolean in1_0 = getPin(inpNo + outNo + 7).getInternalLevel();
		
		boolean oldin4_1 = getPin(inpNo + outNo + 0).getInternalLevel();
		boolean oldin4_0 = getPin(inpNo + outNo + 1).getInternalLevel();
		boolean oldin3_1 = getPin(inpNo + outNo + 2).getInternalLevel();
		boolean oldin3_0 = getPin(inpNo + outNo + 3).getInternalLevel();
		boolean oldin2_1 = getPin(inpNo + outNo + 4).getInternalLevel();
		boolean oldin2_0 = getPin(inpNo + outNo + 5).getInternalLevel();
		boolean oldin1_1 = getPin(inpNo + outNo + 6).getInternalLevel();
		boolean oldin1_0 = getPin(inpNo + outNo + 7).getInternalLevel();*/
		
		if (getPin(2).getLevel()) {
			getPin(inpNo + outNo + 0).changedLevel(new LSLevelEvent(this, false));
			getPin(inpNo + outNo + 1).changedLevel(new LSLevelEvent(this, true));
			getPin(inpNo + outNo + 2).changedLevel(new LSLevelEvent(this, false));
			getPin(inpNo + outNo + 3).changedLevel(new LSLevelEvent(this, true));
			getPin(inpNo + outNo + 4).changedLevel(new LSLevelEvent(this, false));
			getPin(inpNo + outNo + 5).changedLevel(new LSLevelEvent(this, true));
			getPin(inpNo + outNo + 6).changedLevel(new LSLevelEvent(this, false));
			getPin(inpNo + outNo + 7).changedLevel(new LSLevelEvent(this, true));
		} 
		
		if (getPin(1).getLevel()) {
			getPin(inpNo + 0).changedLevel(new LSLevelEvent(this, getPin(inpNo + outNo + 0).getInternalLevel()));
			getPin(inpNo + 1).changedLevel(new LSLevelEvent(this, getPin(inpNo + outNo + 1).getInternalLevel()));
			getPin(inpNo + 2).changedLevel(new LSLevelEvent(this, getPin(inpNo + outNo + 2).getInternalLevel()));
			getPin(inpNo + 3).changedLevel(new LSLevelEvent(this, getPin(inpNo + outNo + 3).getInternalLevel()));
			getPin(inpNo + 4).changedLevel(new LSLevelEvent(this, getPin(inpNo + outNo + 4).getInternalLevel()));
			getPin(inpNo + 5).changedLevel(new LSLevelEvent(this, getPin(inpNo + outNo + 5).getInternalLevel()));
			getPin(inpNo + 6).changedLevel(new LSLevelEvent(this, getPin(inpNo + outNo + 6).getInternalLevel()));
			getPin(inpNo + 7).changedLevel(new LSLevelEvent(this, getPin(inpNo + outNo + 7).getInternalLevel()));
			getPin(inpNo + 8).changedLevel(new LSLevelEvent(this, true));
			getPin(inpNo + 9).changedLevel(new LSLevelEvent(this, true));
		}
		
		if (getPin(0).getLevel()) {
			getPin(inpNo + 0).changedLevel(new LSLevelEvent(this, false));
			getPin(inpNo + 1).changedLevel(new LSLevelEvent(this, false));
			getPin(inpNo + 2).changedLevel(new LSLevelEvent(this, false));
			getPin(inpNo + 3).changedLevel(new LSLevelEvent(this, false));
			getPin(inpNo + 4).changedLevel(new LSLevelEvent(this, false));
			getPin(inpNo + 5).changedLevel(new LSLevelEvent(this, false));
			getPin(inpNo + 6).changedLevel(new LSLevelEvent(this, false));
			getPin(inpNo + 7).changedLevel(new LSLevelEvent(this, false));
			getPin(inpNo + 8).changedLevel(new LSLevelEvent(this, false));
			getPin(inpNo + 9).changedLevel(new LSLevelEvent(this, false));
		}
		
		if (getPin(3).getLevel() && (getPin(4).getLevel() || getPin(5).getLevel()) && (getPin(6).getLevel() || getPin(7).getLevel()) && (getPin(8).getLevel() || getPin(9).getLevel()) && (getPin(10).getLevel() || getPin(11).getLevel())) {
			getPin(inpNo + outNo + 0).changedLevel(new LSLevelEvent(this, getPin(4).getLevel()));
			getPin(inpNo + outNo + 1).changedLevel(new LSLevelEvent(this, getPin(5).getLevel()));
			getPin(inpNo + outNo + 2).changedLevel(new LSLevelEvent(this, getPin(6).getLevel()));
			getPin(inpNo + outNo + 3).changedLevel(new LSLevelEvent(this, getPin(7).getLevel()));
			getPin(inpNo + outNo + 4).changedLevel(new LSLevelEvent(this, getPin(8).getLevel()));
			getPin(inpNo + outNo + 5).changedLevel(new LSLevelEvent(this, getPin(9).getLevel()));
			getPin(inpNo + outNo + 6).changedLevel(new LSLevelEvent(this, getPin(10).getLevel()));
			getPin(inpNo + outNo + 7).changedLevel(new LSLevelEvent(this, getPin(11).getLevel()));
			getPin(inpNo + 10).changedLevel(new LSLevelEvent(this, true));
		}
		
		if (!getPin(3).getLevel() && !(getPin(4).getLevel() || getPin(5).getLevel()) && !(getPin(6).getLevel() || getPin(7).getLevel()) && !(getPin(8).getLevel() || getPin(9).getLevel()) && !(getPin(10).getLevel() || getPin(11).getLevel())) {
			getPin(inpNo + 10).changedLevel(new LSLevelEvent(this, false));
		}
		
		
		
		/*boolean capture = getPin(inpNo + throughput + 2).getInternalLevel();
		boolean oldCapture = capture;
		if (getPin(throughput).getLevel() != getPin(throughput + 1).getLevel()) {
			capture = getPin(throughput).getLevel();
		}
		if (capture != oldCapture) {
			getPin(inpNo + throughput + 2).changedLevel(new LSLevelEvent(this, capture));
		}
		
		boolean[] data = new boolean [throughput];
		boolean[] oldData = new boolean [throughput];
		for (int i = 0; i < throughput; i++) {
			data[i] = getPin(inpNo + i).getInternalLevel();
			oldData[i] = data[i];
		}
		if (!capture) {
			for (int i = 0; i < throughput; i++) {
				data[i] = data[i] && getPin(i).getLevel();
			}
		}
		else {
			for (int i = 0; i < throughput; i++) {
				data[i] = data[i] || getPin(i).getLevel();
			}
		}
		
		boolean oldLack = getPin(inpNo + throughput).getInternalLevel();
		boolean oldRreq = getPin(inpNo + throughput + 1).getInternalLevel();


		for (int i = 0; i < throughput; i++) {
			if (oldData[i] != data[i]) {
				getPin(inpNo + i).changedLevel(new LSLevelEvent(this, data[i]));
			}
		}
		
		boolean ZA = true;
		boolean ZO = false;
		for (int i = 0; i < throughput; i = i + 2) {
			ZA = ZA && (getPin(inpNo + i).getLevel() || getPin(inpNo + i+1).getLevel());
			ZO = ZO || (getPin(inpNo + i).getLevel() || getPin(inpNo + i+1).getLevel());
		}
		boolean signalRecieved = getPin(inpNo + throughput).getInternalLevel();
		boolean oldSignalRecieved = signalRecieved;
		if (ZA == ZO)
			signalRecieved = ZA;
		if (oldLack != signalRecieved) {
			getPin(inpNo + throughput).changedLevel(new LSLevelEvent(this, capture));
		}
		if (oldRreq != signalRecieved) {
			getPin(inpNo + throughput + 1).changedLevel(new LSLevelEvent(this, capture));
		}*/
	}
	
	@Override
	protected void drawLabel(Graphics2D g2, String lbl, Font font) {
		super.drawLabelWithOffsetFromCorner(g2, label, bigFont, labelOffsetX, labelOffsetY);
	}
	
	@Override
	public void changedLevel(LSLevelEvent e) {
		super.changedLevel(e);
		if (busted) {
			System.err.println("busted");
			return;
		}
		simulate();
	}

	/*@Override
	protected void drawFrame(Graphics2D g2) {
		String gateType = LSProperties.getInstance().getProperty(LSProperties.GATEDESIGN, LSProperties.GATEDESIGN_IEC);
		if (gateType.equals(LSProperties.GATEDESIGN_IEC))
			super.drawFrame(g2);
		else
			drawANSI(g2);
	}

	private void drawANSI(Graphics2D g2) {
		Path2D p = new Path2D.Double();
		p.moveTo(getX() + CONN_SIZE, getY() + CONN_SIZE);
		p.lineTo(getX() + width - 4 * CONN_SIZE, getY() + CONN_SIZE);
		double x1 = getX() + width + 1.4f;
		p.curveTo(x1, getY() + CONN_SIZE, x1, getY() + height - CONN_SIZE, getX() + width - 4 * CONN_SIZE,
				getY() + height - CONN_SIZE);
		p.lineTo(getX() + CONN_SIZE, getY() + height - CONN_SIZE);
		p.closePath();
		g2.setPaint(Color.WHITE);
		g2.fill(p);
		g2.setPaint(Color.black);
		g2.draw(p);
	}*/

	@Override
	public void loadLanguage() {
		I18N.addGate(I18N.ALL, type, I18N.TITLE, "rwEight");
		I18N.addGate(I18N.ALL, type, I18N.DESCRIPTION, "8-bit rewritable memory");
	}
}