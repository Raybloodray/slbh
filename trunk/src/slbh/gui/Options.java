/*
	This file is part of SLBH.
	
	SLBH is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.
	
	SLBH is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with SLBH.  If not, see <http://www.gnu.org/licenses/>.
	
	Copyright 2010 Simon Levesque
*/

package slbh.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.*;

import slbh.lang.Config;

@SuppressWarnings("serial")
public class Options extends JPanel implements ActionListener, ItemListener {
	private JComboBox floors;
	private ButtonGroup group;
	private JButton floorCreateB;
	private JButton floorCreateA;
	private JButton floorRemove;
	private JRadioButton start;
	private JRadioButton floor;
	private JRadioButton wall;
	private JRadioButton stairsN;
	private JRadioButton stairsS;
	private JRadioButton stairsE;
	private JRadioButton stairsW;
	private JButton properties;
	private JButton sl;
	private Viewport myViewport;

	public Options(Viewport myViewport) {
		this.myViewport = myViewport;
		reloadLang();
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().compareTo("floorCreateB") == 0) {
			int current = Integer.valueOf((String) floors.getSelectedItem()).intValue();
			makeCombo(myViewport.createFloor(current, false));
			return;
		}
		
		if (e.getActionCommand().compareTo("floorCreateA") == 0) {
			int current = Integer.valueOf((String) floors.getSelectedItem()).intValue();
			makeCombo(myViewport.createFloor(current, true));
			return;
		}
		
		if (e.getActionCommand().compareTo("floorRemove") == 0) {
			int current = Integer.valueOf((String) floors.getSelectedItem()).intValue();
			makeCombo(myViewport.removeFloor(current));
			return;
		}
		
		if (e.getActionCommand().compareTo("sl") == 0) {
			// Create the scene
			slbh.conf.Configuration myConf = myViewport.createScene();

			// Show
			ViewLSL v = new ViewLSL(myConf.ToSLScript((int) myViewport.getDeltaZ()*100*myViewport.numberOfFloors(), myViewport.getRepeat()));
			v.setVisible(true);
			return;
		}
		
		if (e.getActionCommand().compareTo("properties") == 0) {
			// Open the properties window
			Properties p = new Properties(myViewport);
			p.setVisible(true);
			return;
		}
		
		myViewport.changeObject(e.getActionCommand());

	}

	public void itemStateChanged(ItemEvent arg0) {
		if (arg0.getStateChange() == ItemEvent.SELECTED)
			myViewport.changeFloor(Integer.valueOf((String) floors.getSelectedItem()).intValue());
	}

	public void makeCombo(int selected) {
		floors.removeAllItems();
		int size = myViewport.numberOfFloors();
		for (int i=0; i<size; i++) floors.addItem(String.valueOf(i));
		floors.setSelectedIndex(selected);
	}

	public void reloadLang() {
		// Clear all
		if (getComponentCount() != 0) removeAll();
		
		// Create
		floors = new JComboBox();
		group = new ButtonGroup();
		floorCreateB = new JButton(Config.getMyLanguage().buttonAddFloorBefore());
		floorCreateA = new JButton(Config.getMyLanguage().buttonAddFloorAfter());
		floorRemove = new JButton(Config.getMyLanguage().buttonRemoveFloor());
		start = new JRadioButton(Config.getMyLanguage().objectStart());
		floor = new JRadioButton(Config.getMyLanguage().objectFloor());
		wall = new JRadioButton(Config.getMyLanguage().objectWall());
		stairsN = new JRadioButton(Config.getMyLanguage().objectStairsN());
		stairsS = new JRadioButton(Config.getMyLanguage().objectStairsS());
		stairsE = new JRadioButton(Config.getMyLanguage().objectStairsE());
		stairsW = new JRadioButton(Config.getMyLanguage().objectStairsW());
		properties = new JButton(Config.getMyLanguage().buttonProperties());
		sl = new JButton("SL Script");
		
		// Create the objects
		group.add(start);
		group.add(floor);
		group.add(wall);
		group.add(stairsN);
		group.add(stairsS);
		group.add(stairsE);
		group.add(stairsW);
		
		// Set the components
		JLabel l;
		int x = 10;
		
		l = new JLabel(Config.getMyLanguage().labelCurrentFloor());
		l.setBounds(0, x, 100, 20);
		add(l);
		
		floors.addItem("0");
		floors.setSelectedIndex(0);
		floors.setBounds(100, x, 100, 20);
		x += 20;
		floorCreateB.setBounds(0, x, 200, 20);
		x += 20;
		floorCreateA.setBounds(0, x, 200, 20);
		x += 20;
		floorRemove.setBounds(0, x, 200, 20);
		
		x += 50;
		l = new JLabel(Config.getMyLanguage().labelObjects());
		l.setBounds(0, x, 100, 20);
		add(l);
		x += 20;
		start.setBounds(0, x, 200, 20);
		x += 20;
		floor.setBounds(0, x, 200, 20);
		x += 20;
		wall.setBounds(0, x, 200, 20);
		x += 20;
		stairsN.setBounds(0, x, 200, 20);
		x += 20;
		stairsS.setBounds(0, x, 200, 20);
		x += 20;
		stairsE.setBounds(0, x, 200, 20);
		x += 20;
		stairsW.setBounds(0, x, 200, 20);
		
		x += 50;
		properties.setBounds(0, x, 200, 20);
		x += 20;
		sl.setBounds(0, x, 200, 20);
		
		// Set the panel
		setLayout(null);
		
		add(floors);
		add(floorCreateB);
		add(floorCreateA);
		add(floorRemove);
		add(start);
		add(floor);
		add(wall);
		add(stairsN);
		add(stairsS);
		add(stairsW);
		add(stairsE);
		add(properties);
		add(sl);
		
		
		// Place the listeners
		floorCreateB.setActionCommand("floorCreateB");
		floorCreateA.setActionCommand("floorCreateA");
		floorRemove.setActionCommand("floorRemove");
		floor.setActionCommand("floor");
		wall.setActionCommand("wall");
		stairsN.setActionCommand("stairsN");
		stairsS.setActionCommand("stairsS");
		stairsE.setActionCommand("stairsE");
		stairsW.setActionCommand("stairsW");
		properties.setActionCommand("properties");
		sl.setActionCommand("sl");
		start.setActionCommand("start");
		
		myViewport.changeObject("floor");
		floor.setSelected(true);
		
		floors.addItemListener(this);
		floorCreateB.addActionListener(this);
		floorCreateA.addActionListener(this);
		floorRemove.addActionListener(this);
		floor.addActionListener(this);
		wall.addActionListener(this);
		stairsN.addActionListener(this);
		stairsS.addActionListener(this);
		stairsE.addActionListener(this);
		stairsW.addActionListener(this);
		properties.addActionListener(this);
		sl.addActionListener(this);
		start.addActionListener(this);
		
		repaint();
	}
}
