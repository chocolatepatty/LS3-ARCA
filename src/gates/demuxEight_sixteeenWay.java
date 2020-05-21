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
public class demuxEight_sixteeenWay extends Gate {
	static final long serialVersionUID = 4521959944440523564L;

	int inpNo = 16;
	int outNo = 128+16;
	
	public demuxEight_sixteeenWay() {
		super("basic");
		label = "demuxEight_sixteenWay";
		labelOffsetX = 100;
		labelOffsetY = 60;
		type = "demuxEight_sixteenWay";
		
		width = 280;
		height = 1450;
		createInputs(inpNo);
		createOutputs(outNo);
		
		int offset = 40;

		getPin(0).label = "in4_1";
		getPin(1).label = "in4_0";
		getPin(2).label = "in3_1";
		getPin(3).label = "in3_0";
		getPin(4).label = "in2_1";
		getPin(5).label = "in2_0";
		getPin(6).label = "in1_1";
		getPin(7).label = "in1_0";
		
		getPin(8).label = "RAXXX";
		getPin(9).label = "RBXXX";
		getPin(10).label = "RXAXX";
		getPin(11).label = "RXBXX";
		getPin(12).label = "RXXAX";
		getPin(13).label = "RXXBX";
		getPin(14).label = "RXXXA";
		getPin(15).label = "RXXXB";
		
		String l = "";
		for (int i = inpNo; i < inpNo + 128; i++) {
			l = "";
			
			if ((i-inpNo) / 64 == 0) l = l + "A";
			else l = l + "B";
			
			if (((i-inpNo) / 32) % 2 == 0) l = l + "A";
			else l = l + "B";
			

			if (((i-inpNo) / 16) % 2 == 0) l = l + "A";
			else l = l + "B";
			
			if (((i-inpNo) / 8) % 2 == 0) l = l + "A";
			else l = l + "B";
			
			switch ((i-inpNo) % 8) {
				case 0: l = l + "4_1";
					break;
				case 1: l = l + "4_0"; 
					break;
				case 2: l = l + "3_1"; 
					break;
				case 3: l = l + "3_0"; 
					break;
				case 4: l = l + "2_1"; 
					break;
				case 5: l = l + "2_0"; 
					break;
				case 6: l = l + "1_1"; 
					break;
				case 7: l = l + "1_0"; 
					break;
			}
			
			getPin(i).label = l;
		}
		
		getPin(inpNo + 128 + 0).label = "RAAAA";
		getPin(inpNo + 128 + 1).label = "RAAAB";
		getPin(inpNo + 128 + 2).label = "RAABA";
		getPin(inpNo + 128 + 3).label = "RAABB";
		getPin(inpNo + 128 + 4).label = "RABAA";
		getPin(inpNo + 128 + 5).label = "RABAB";
		getPin(inpNo + 128 + 6).label = "RABBA";
		getPin(inpNo + 128 + 7).label = "RABBB";
		getPin(inpNo + 128 + 8).label = "RBAAA";
		getPin(inpNo + 128 + 9).label = "RBAAB";
		getPin(inpNo + 128 + 10).label = "RBABA";
		getPin(inpNo + 128 + 11).label = "RBABB";
		getPin(inpNo + 128 + 12).label = "RBBAA";
		getPin(inpNo + 128 + 13).label = "RBBAB";
		getPin(inpNo + 128 + 14).label = "RBBBA";
		getPin(inpNo + 128 + 15).label = "RBBBB";
		
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
		
		if ((getPin(8).getLevel() || getPin(9).getLevel()) && (getPin(10).getLevel() || getPin(11).getLevel()) && (getPin(12).getLevel() || getPin(13).getLevel()) && (getPin(14).getLevel() || getPin(15).getLevel())) {

			getPin(inpNo + 8*(8*(getPin(9).getLevel()? 1 : 0) + 4*(getPin(11).getLevel()? 1 : 0) + 2*(getPin(13).getLevel()? 1 : 0) + (getPin(15).getLevel()? 1:0))).changedLevel(new LSLevelEvent(this, getPin(0).getLevel()));
			getPin(inpNo + 8*(8*(getPin(9).getLevel()? 1 : 0) + 4*(getPin(11).getLevel()? 1 : 0) + 2*(getPin(13).getLevel()? 1 : 0) + (getPin(15).getLevel()? 1:0)) + 1).changedLevel(new LSLevelEvent(this, getPin(1).getLevel()));
			getPin(inpNo + 8*(8*(getPin(9).getLevel()? 1 : 0) + 4*(getPin(11).getLevel()? 1 : 0) + 2*(getPin(13).getLevel()? 1 : 0) + (getPin(15).getLevel()? 1:0)) + 2).changedLevel(new LSLevelEvent(this, getPin(2).getLevel()));
			getPin(inpNo + 8*(8*(getPin(9).getLevel()? 1 : 0) + 4*(getPin(11).getLevel()? 1 : 0) + 2*(getPin(13).getLevel()? 1 : 0) + (getPin(15).getLevel()? 1:0)) + 3).changedLevel(new LSLevelEvent(this, getPin(3).getLevel()));
			getPin(inpNo + 8*(8*(getPin(9).getLevel()? 1 : 0) + 4*(getPin(11).getLevel()? 1 : 0) + 2*(getPin(13).getLevel()? 1 : 0) + (getPin(15).getLevel()? 1:0)) + 4).changedLevel(new LSLevelEvent(this, getPin(4).getLevel()));
			getPin(inpNo + 8*(8*(getPin(9).getLevel()? 1 : 0) + 4*(getPin(11).getLevel()? 1 : 0) + 2*(getPin(13).getLevel()? 1 : 0) + (getPin(15).getLevel()? 1:0)) + 5).changedLevel(new LSLevelEvent(this, getPin(5).getLevel()));
			getPin(inpNo + 8*(8*(getPin(9).getLevel()? 1 : 0) + 4*(getPin(11).getLevel()? 1 : 0) + 2*(getPin(13).getLevel()? 1 : 0) + (getPin(15).getLevel()? 1:0)) + 6).changedLevel(new LSLevelEvent(this, getPin(6).getLevel()));
			getPin(inpNo + 8*(8*(getPin(9).getLevel()? 1 : 0) + 4*(getPin(11).getLevel()? 1 : 0) + 2*(getPin(13).getLevel()? 1 : 0) + (getPin(15).getLevel()? 1:0)) + 7).changedLevel(new LSLevelEvent(this, getPin(7).getLevel()));
			getPin(inpNo + 128 + (8*(getPin(9).getLevel()? 1 : 0) + 4*(getPin(11).getLevel()? 1 : 0) + 2*(getPin(13).getLevel()? 1 : 0) + (getPin(15).getLevel()? 1:0))).changedLevel(new LSLevelEvent(this, true));
		
			for (int i = inpNo; i < inpNo + 128; i++) {
				if ((((i-inpNo) % 2 == 1)) && (((i-inpNo) < 8*(8*(getPin(9).getLevel()? 1 : 0) + 4*(getPin(11).getLevel()? 1 : 0) + 2*(getPin(13).getLevel()? 1 : 0) + (getPin(15).getLevel()? 1:0)) || ((i-inpNo) >= 8*(8*(getPin(9).getLevel()? 1 : 0) + 4*(getPin(11).getLevel()? 1 : 0) + 2*(getPin(13).getLevel()? 1 : 0) + (getPin(15).getLevel()? 1:0)) + 8)))) {
					getPin(i).changedLevel(new LSLevelEvent(this, true));
				}
			}
			
		
		}
		
		if (!(getPin(8).getLevel() || getPin(9).getLevel()) && !(getPin(10).getLevel() || getPin(11).getLevel()) && !(getPin(12).getLevel() || getPin(13).getLevel()) && !(getPin(14).getLevel() || getPin(15).getLevel())) {

			for (int i = inpNo; i < inpNo + outNo; i++) {
				getPin(i).changedLevel(new LSLevelEvent(this, false));
			}
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
		I18N.addGate(I18N.ALL, type, I18N.TITLE, "demuxEight_sixteenWay");
		I18N.addGate(I18N.ALL, type, I18N.DESCRIPTION, "4-bit 16-way demultiplexer");
	}
}