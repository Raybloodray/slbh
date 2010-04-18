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

package slbh.gui.dialogs;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import slbh.scene.Scene;

@SuppressWarnings("serial")
public class Properties extends JFrame implements ActionListener{ 
	private JTextField deltaXY = new JTextField();
	private JTextField deltaZ = new JTextField();
	private JTextField repeat = new JTextField();
	private Scene myScene;
	private JButton bOk = new JButton("OK");
	private JButton bCancel = new JButton(slbh.lang.Config.getMyLanguage().buttonCancel());
	
	
	public Properties(Scene myScene) {
		this.myScene = myScene;
		
		// Set the window
		setTitle("Properties");
		setSize(500, 200);
		setLayout(new FlowLayout());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		// Set the components
		deltaXY.setText(String.valueOf(myScene.getDeltaXY()));
		deltaZ.setText(String.valueOf(myScene.getDeltaZ()));
		repeat.setText(String.valueOf(myScene.getRepeat()));
		
		// Add the components
		add(new JLabel(slbh.lang.Config.getMyLanguage().propertiesDeltaXY()));
		add(deltaXY);
		add(new JLabel(slbh.lang.Config.getMyLanguage().propertiesDeltaZ()));
		add(deltaZ);
		add(new JLabel(slbh.lang.Config.getMyLanguage().propertiesRepeat()));
		add(repeat);
		add(bOk);
		add(bCancel);
		
		// Set the listener
		bOk.setActionCommand("ok");
		bCancel.setActionCommand("cancel");
		bOk.addActionListener(this);
		bCancel.addActionListener(this);
	}

	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().compareTo("ok") == 0) {
			myScene.changeDelta(Double.valueOf(deltaXY.getText()).doubleValue(), Double.valueOf(deltaZ.getText()).doubleValue());
			myScene.setRepeat(Integer.valueOf(repeat.getText()));
		}
		
		dispose();
	}
}
