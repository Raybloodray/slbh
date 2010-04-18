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
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class ViewLSL extends JFrame implements ActionListener {
	private JTextArea code;
	private JButton bOk = new JButton("OK");
	private JButton bClip = new JButton(slbh.lang.Config.getMyLanguage().buttonToClipboard());
	
	public ViewLSL(String text) {
		// Set the window
		setTitle("LSL Code");
		setSize(800, 600);
		setLayout(new FlowLayout());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		// Set the components
		code = new JTextArea(text, 30, 70);
		bOk.setActionCommand("ok");
		bOk.addActionListener(this);
		add(new JScrollPane(code));
		bClip.setActionCommand("clip");
		bClip.addActionListener(this);
		add(bClip);
		add(bOk);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().compareTo("clip") == 0) {
			code.selectAll();
			String selection = code.getSelectedText();
			StringSelection data = new StringSelection(selection);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(data, data);
		} else {
			dispose();
		}
	}
}
