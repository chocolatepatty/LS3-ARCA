package gates;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import logicsim.Gate;
import logicsim.I18N;
import logicsim.Lang;

/**
 * Binary Display for LogicSim
 * 
 * @author Andreas Tetzl
 * @author Peter Gabriel
 * @version 2.0
 */
public class BinDisp extends Gate {
	static final long serialVersionUID = -6532037559895208921L;

	private static final String DISPLAY_TYPE = "displaytype";
	private static final String DISPLAY_TYPE_HEX = "hex";
	private static final String DISPLAY_TYPE_DEC = "dec";
	private static final String DISPLAY_TYPE_DEFAULT = "hex";

	String displayType;

	public BinDisp() {
		super("output");
		label = "HEX";
		type = "bindisp";
		height = 90;
		setNumInputs(8);
		setNumOutputs(0);
		loadProperties();
	}

	@Override
	protected void loadProperties() {
		displayType = getPropertyWithDefault(DISPLAY_TYPE, DISPLAY_TYPE_DEFAULT);
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);

		int value = 0;
		for (int i = 0; i < 8; i++) {
			if (getInputLevel(i))
				value += (1 << i);
		}

		String sval = "";
		if (DISPLAY_TYPE_DEC.equals(displayType)) {
			sval = Integer.toString(value);
		} else
			sval = Integer.toHexString(value);

		if (sval.length() == 0)
			sval = "00";
		if (sval.length() == 1)
			sval = "0" + sval;
		sval = sval.toUpperCase();
		g.setFont(bigFont);
		int sw = g.getFontMetrics().stringWidth(sval);
		g.drawString(sval, getX() + getWidth() / 2 - sw / 2, getY() + getHeight() - 20);
	}

	public boolean hasPropertiesUI() {
		return true;
	}

	public boolean showPropertiesUI(Component frame) {
		JRadioButton jRadioButton1 = new JRadioButton();
		JRadioButton jRadioButton2 = new JRadioButton();

		// Group the radio buttons.
		ButtonGroup group = new ButtonGroup();
		group.add(jRadioButton1);
		group.add(jRadioButton2);

		if (DISPLAY_TYPE_HEX.equals(displayType))
			jRadioButton1.setSelected(true);
		else
			jRadioButton2.setSelected(true);

		JPanel jPanel1 = new JPanel();
		TitledBorder titledBorder1;
		BorderLayout borderLayout1 = new BorderLayout();

		titledBorder1 = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED, Color.white, new Color(142, 142, 142)),
				I18N.getString(type, "ui.displaytype"));
		jRadioButton1.setText(I18N.getString(type, "ui.hex"));
		jRadioButton2.setText(I18N.getString(type, "ui.dec"));
		jPanel1.setBorder(titledBorder1);
		jPanel1.setBounds(new Rectangle(11, 11, 171, 150));
		jPanel1.setLayout(borderLayout1);
		jPanel1.add(jRadioButton1, BorderLayout.NORTH);
		jPanel1.add(jRadioButton2, BorderLayout.CENTER);

		JOptionPane pane = new JOptionPane(jPanel1);
		pane.setMessageType(JOptionPane.QUESTION_MESSAGE);
		pane.setOptions(new String[] { I18N.getString(Lang.BTN_OK), I18N.getString(Lang.BTN_CANCEL) });
		JDialog dlg = pane.createDialog(frame, I18N.getString(type, "ui.title"));
		dlg.setResizable(true);
		dlg.setSize(290, 180);
		dlg.setVisible(true);
		if (I18N.getString(Lang.BTN_OK).equals((String) pane.getValue())) {
			if (jRadioButton1.isSelected()) {
				displayType = DISPLAY_TYPE_HEX;
			} else if (jRadioButton2.isSelected()) {
				displayType = DISPLAY_TYPE_DEC;
			}
			label = displayType.toUpperCase();
			return true;
		}
		return false;
	}

}