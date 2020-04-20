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
public class ARBUFFER8 extends Gate {
	static final long serialVersionUID = 4521959944440523564L;

	private int throughput = 8;
	private int inpNo = throughput + 2;
	private int outNo = 2 + throughput + 3;
	
	public ARBUFFER8() {
		super("basic");
		label = "ARBUFFER8";
		labelOffsetX = 0;
		labelOffsetY = -30;
		type = "arbuffer8";
		
		width = 120;
		height = Math.max(240, 20*throughput + 100);
		createInputs(inpNo);
		createOutputs(outNo);
		
		int offset = 40;
		
		for (int i = 0; i < throughput; i++) {
			getPin(i).moveTo(getX(), getY() + 90 + i*20 - 10*(i % 2));
		}
		
		getPin(throughput).moveTo(getX() + getWidth()/2, getY());
		getPin(throughput).setDirection(Pin.DOWN);
		getPin(throughput).label = "Lreq";
		
		getPin(throughput + 1).moveTo(getX() + getWidth() / 2 + offset, getY() + getHeight());
		getPin(throughput + 1).setDirection(Pin.UP);
		getPin(throughput + 1).label = "Rack";
		
		for (int i = 0; i < throughput; i++) {
			getPin(inpNo + i).moveTo(getX() + getWidth(), getY() + 90 + i*20 - 10*(i % 2));
		}
		
		getPin(inpNo + throughput).moveTo(getX() + getWidth() / 2 - offset, getY() + getHeight());
		getPin(inpNo + throughput).setDirection(Pin.UP);
		getPin(inpNo + throughput).label = "Lack";
		
		getPin(inpNo + throughput + 1).moveTo(getX() + getWidth() / 2, getY() + getHeight());
		getPin(inpNo + throughput + 1).setDirection(Pin.UP);
		getPin(inpNo + throughput + 1).label = "Rreq";
		
		getPin(inpNo + throughput + 2).moveTo(getX() + 30, getY() + 40);
		getPin(inpNo + throughput + 2).setDirection(Pin.UP);

		getPin(inpNo + throughput + 3).moveTo(getX() + 50, getY() + 40);
		getPin(inpNo + throughput + 3).setDirection(Pin.UP);
		
		getPin(inpNo + throughput + 4).moveTo(getX() + 40, getY() + 60);
		getPin(inpNo + throughput + 4).setDirection(Pin.UP);
		
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
		
		boolean ZA = true;
		boolean ZO = false;
		for (int i = 0; i < throughput; i = i + 2) {
			ZA = ZA && (getPin(i).getLevel() || getPin(i+1).getLevel());
			ZO = ZO || (getPin(i).getLevel() || getPin(i+1).getLevel());
		}
		
		boolean signalComplete = getPin(inpNo + throughput + 2).getInternalLevel();
		boolean oldSignalComplete = signalComplete;
		if (ZA == ZO)
			signalComplete = ZA;
		
		boolean captureRequest = getPin(inpNo + throughput + 3).getInternalLevel();
		boolean oldCaptureRequest = captureRequest;
		if (getPin(throughput).getLevel() != getPin(throughput + 1).getLevel()) {
			captureRequest = getPin(throughput).getLevel();
		}
		
		boolean capture = getPin(inpNo + throughput + 4).getInternalLevel();
		boolean oldCapture = capture;
		if (signalComplete == captureRequest) {
			capture = signalComplete;
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

		if (signalComplete != oldSignalComplete) {
			getPin(inpNo + throughput + 2).changedLevel(new LSLevelEvent(this, signalComplete));
		}
		if (captureRequest != oldCaptureRequest) {
			getPin(inpNo + throughput + 3).changedLevel(new LSLevelEvent(this, captureRequest));
		}
		if (capture != oldCapture) {
			getPin(inpNo + throughput + 4).changedLevel(new LSLevelEvent(this, capture));
		}
		for (int i = 0; i < throughput; i++) {
			if (oldData[i] != data[i]) {
				getPin(inpNo + i).changedLevel(new LSLevelEvent(this, data[i]));
			}
		}
		
		if (oldLack != capture) {
			getPin(inpNo + throughput).changedLevel(new LSLevelEvent(this, capture));
		}
		if (oldRreq != capture) {
			getPin(inpNo + throughput + 1).changedLevel(new LSLevelEvent(this, capture));
		}
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
		I18N.addGate(I18N.ALL, type, I18N.TITLE, "ARBUFFER8");
		I18N.addGate(I18N.ALL, type, I18N.DESCRIPTION, "8-bit Acknowledge Request Buffer");
		I18N.addGate("de", type, I18N.DESCRIPTION, "ARPuffer8");
	}
}