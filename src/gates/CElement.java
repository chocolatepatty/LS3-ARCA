package gates;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

import javax.swing.JOptionPane;

import logicsim.Gate;
import logicsim.I18N;
import logicsim.LSLevelEvent;
import logicsim.LSProperties;
import logicsim.Pin;

/**
 * C-element for LogicSim
 * 
 * @author Andreas Tetzl
 * @author Peter Gabriel
 * @author Matthew Lister April 2020
 */
public class CElement extends Gate {
	static final long serialVersionUID = 4521959944440523564L;

	public CElement() {
		super("basic");
		label = "C";
		type = "celement";
		width = 100;
		height = 100;
		int inpNo = 2;
		int outNo = 1;
		createInputs(inpNo);
		createOutputs(outNo);
		//variableInputCountSupported = true;
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
	public void simulate() {
		super.simulate();
		boolean oldLevel = getPin(2).getInternalLevel();
		boolean newLevel = oldLevel;
		if (getPin(0).getLevel() == getPin(1).getLevel())
			newLevel = getPin(0).getLevel();
		if (newLevel != oldLevel)
			getPin(2).changedLevel(new LSLevelEvent(this, newLevel));
	}

	@Override
	public void changedLevel(LSLevelEvent e) {
		super.changedLevel(e);
		if (busted)
			return;
		simulate();
	}

	@Override
	protected void drawFrame(Graphics2D g2) {
		String gateType = LSProperties.getInstance().getProperty(LSProperties.GATEDESIGN, LSProperties.GATEDESIGN_IEC);
		if (gateType.equals(LSProperties.GATEDESIGN_IEC))
			super.drawFrame(g2);
		else
			drawANSI(g2);
	}

	private void drawANSI(Graphics2D g2) {
		Ellipse2D p = new Ellipse2D.Double();
		p.setFrame(getX() + CONN_SIZE - 18, getY() + CONN_SIZE - 10, 105, 105);
		g2.setPaint(Color.WHITE);
		g2.fill(p);
		g2.setPaint(Color.black);
		g2.draw(p);
		super.drawLabelWithOffset(g2, label, bigFont, -8, -20);
		g2.drawString(text, getX() + 3, getY() + height / 2 + 20);
	}
	
	@Override
	protected void drawLabel(Graphics2D g2, String lbl, Font font) {
		super.drawLabelWithOffset(g2, label, bigFont, 0, -13);
		g2.drawString(text, getX() + 8, getY() + 3 * height / 4);
	}

	@Override
	public void loadLanguage() {
		I18N.addGate(I18N.ALL, type, I18N.TITLE, "CELEMENT");
		I18N.addGate(I18N.ALL, type, I18N.DESCRIPTION, "Muller C-gate");
	}

}