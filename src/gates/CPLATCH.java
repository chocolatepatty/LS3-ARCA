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
public class CPLATCH extends Gate {
	static final long serialVersionUID = 4521959944440523564L;

	public CPLATCH() {
		super("basic");
		label = "CPLATCH";
		labelOffsetX = 0;
		labelOffsetY = -30;
		type = "cplatch";
		width = 120;
		height = 240;
		
		int inpNo = 3;
		createInputs(inpNo);
		int outNo = 3;
		createOutputs(outNo);
		
		int offset = 40;
		
		getPin(0).moveTo(getX(), getY() + getHeight()/2);
		getPin(0).label = "Ldata";
		
		getPin(1).moveTo(getX() + getWidth()/2, getY());
		getPin(1).setDirection(Pin.DOWN);
		getPin(1).label = "Lreq";
		
		getPin(2).moveTo(getX() + getWidth() / 2 + offset, getY() + getHeight());
		getPin(2).setDirection(Pin.UP);
		getPin(2).label = "Rack";
		
		getPin(inpNo + 0).moveTo(getX() + getWidth(), getY() + getHeight() / 2);
		getPin(inpNo + 0).label = "Rdata";
		
		getPin(inpNo + 1).moveTo(getX() + getWidth() / 2 - offset, getY() + getHeight());
		getPin(inpNo + 1).setDirection(Pin.UP);
		getPin(inpNo + 1).label = "Lack";
		
		getPin(inpNo + 2).moveTo(getX() + getWidth() / 2, getY() + getHeight());
		getPin(inpNo + 2).setDirection(Pin.UP);
		getPin(inpNo + 2).label = "Rreq";
		
		reset();
		loadProperties();
	}
	
	static final String TEXT = "text";

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
	}

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
		g2.drawString(text, getX() + 20, getY() + height / 2 + 60);
	}

	
	@Override
	public void simulate() {
		super.simulate();
		boolean oldLevel = getPin(0).getInternalLevel();
		boolean newLevel = true;
		for (Pin c : getInputs()) {
			newLevel = newLevel && c.getLevel();
			if (!newLevel)
				break;
		}
		// call pin directly
		if (newLevel != oldLevel)
			getPin(0).changedLevel(new LSLevelEvent(this, newLevel));
	}
	
	@Override
	protected void drawLabel(Graphics2D g2, String lbl, Font font) {
		super.drawLabelWithOffset(g2, label, bigFont, labelOffsetX, labelOffsetY);
	}
	
	@Override
	public void changedLevel(LSLevelEvent e) {
		super.changedLevel(e);
		if (busted)
			return;
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
		I18N.addGate(I18N.ALL, type, I18N.TITLE, "CPLATCH");
		I18N.addGate(I18N.ALL, type, I18N.DESCRIPTION, "Capture-Pass Latch");
		I18N.addGate("de", type, I18N.DESCRIPTION, "CPLatch");
	}
}