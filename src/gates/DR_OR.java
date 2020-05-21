package gates;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import logicsim.Gate;
import logicsim.I18N;
import logicsim.LSLevelEvent;
import logicsim.LSProperties;
import logicsim.Pin;

/**
 * Dual Rail AND Gate for LogicSim
 * 
 * @author Andreas Tetzl
 * @author Peter Gabriel
 * @author Matthew Lister April 2020
 */
public class DR_OR extends Gate {
	static final long serialVersionUID = 4521959944440523564L;
	
	private int inpNo = 4;
	private int outNo = 2;

	public DR_OR() {
		super("basic");
		label = "drOR";
		type = "dr_or";
		createInputs(inpNo);
		createOutputs(outNo);		
		width = 60;
		height = 70;
		
		getPin(0).moveTo(getX(), getY() + 10);
		getPin(1).moveTo(getX(), getY() + 20);
		getPin(2).moveTo(getX(), getY() + 50);
		getPin(3).moveTo(getX(), getY() + 60);
		
		getPin(4).moveTo(getX() + getWidth(), getY() + 30);
		getPin(5).moveTo(getX() + getWidth(), getY() + 40);

		getPin(0).label = "B1";
		getPin(1).label = "B0";
		getPin(2).label = "A1";
		getPin(3).label = "A0";
		getPin(4).label = "S1";
		getPin(5).label = "S0";
		
		reset();
	}

	@Override
	public void simulate() {
		super.simulate();
		boolean oldLevel1 = getPin(inpNo + 1).getInternalLevel();
		boolean oldLevel2 = getPin(inpNo).getInternalLevel();
		
		boolean inp1_0 = getPin(3).getLevel();
		boolean inp1_1 = getPin(2).getLevel();
		boolean inp2_0 = getPin(1).getLevel();
		boolean inp2_1 = getPin(0).getLevel();
		
		boolean newLevel1 = (inp1_0 && inp2_0);
		boolean newLevel2 = (inp1_1 && inp2_1) || (inp1_0 && inp2_1) || (inp1_1 && inp2_0);
		
		// call pin directly
		if (newLevel1 != oldLevel1)
			getPin(inpNo + 1).changedLevel(new LSLevelEvent(this, newLevel1));
		if (newLevel2 != oldLevel2)
			getPin(inpNo).changedLevel(new LSLevelEvent(this, newLevel2));
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

	protected void drawANSI(Graphics2D g2) {
		Path2D p = new Path2D.Double();
		double xl = getX() + CONN_SIZE;
		double yu = getY() + CONN_SIZE;
		double xr = getX() + width - CONN_SIZE + 1;
		double yb = getY() + height - CONN_SIZE;
		double xb1 = getX() + 5 * CONN_SIZE;
		double cY = getY() + height / 2;

		p.moveTo(xl, yu);
		p.lineTo(xl + CONN_SIZE, yu);
		p.curveTo(xb1, yu, xr, cY, xr, cY);
		p.curveTo(xr, cY, xb1, yb, xl + CONN_SIZE, yb);
		p.lineTo(xl, yb);
		p.curveTo(xl + 10, yb - 10, xl + 10, yu + 10, xl, yu);
		p.closePath();
		g2.setPaint(Color.gray);
		g2.fill(p);
		g2.setPaint(Color.black);
		g2.draw(p);
	}

	@Override
	public void loadLanguage() {
		I18N.addGate(I18N.ALL, type, I18N.TITLE, "DR_OR");
		I18N.addGate(I18N.ALL, type, I18N.DESCRIPTION, "Dual rail OR Gate");
	}

}