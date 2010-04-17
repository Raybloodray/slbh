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
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import slbh.lang.*;

@SuppressWarnings("serial")
public class Principal extends JFrame implements ComponentListener, ActionListener {
	private Viewport viewport = new Viewport();
	private Options options = new Options(viewport);
	private JMenuBar bar;
	
	// Program Entry
	public static void main(String[] args) {
		Config.load();
		
		// Load the window
		Principal myWindow = new Principal();
		myWindow.setVisible(true);
	}
	
	// The constructor
	public Principal() {
		// Set the window
		setTitle("Second Life Builder Helper");
		setSize(500, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		addComponentListener(this);
		
		// Lang
		reloadLang();		
	}
	
	public void reloadLang() {
		// Clear
		if (getComponentCount() != 1) removeAll();
		
		// Prepare the menu
		JMenu file = new JMenu(Config.getMyLanguage().menuFile());
		bar = new JMenuBar();
		JMenuItem item = new JMenuItem(Config.getMyLanguage().menuNew());
		item.setActionCommand("new");
		item.addActionListener(this);
		file.add(item);
		item = new JMenuItem(Config.getMyLanguage().menuOpen());
		item.setActionCommand("open");
		item.addActionListener(this);
		file.add(item);
		item = new JMenuItem(Config.getMyLanguage().menuSave());
		item.setActionCommand("save");
		item.addActionListener(this);
		file.add(item);
		
		bar.add(file);
		JMenu lang = new JMenu(Config.getMyLanguage().menuLanguage());
		
		for (String s : Config.getAllLanguages()) {
			JMenuItem one = new JMenuItem(s);
			one.setActionCommand("lang-" + s);
			one.addActionListener(this);
			lang.add(one);
		}

		bar.add(lang);
		
		setJMenuBar(bar);
		
		// Adjust the components
		Reposition();
		
		// Add the components
		add(options);
		add(viewport);
		
		// Traverse
		options.reloadLang();
		setSize(getWidth()+1, getHeight());
		setSize(getWidth()-1, getHeight());
		repaint();
	}
	
	public void Reposition() {
		int width = 200;
		options.setBounds(0,0,width,getHeight());
		viewport.setBounds(width,0,getWidth()-width,getHeight());
	}

	public void actionPerformed(ActionEvent arg0) {
		// NEW
		if (arg0.getActionCommand().equals("new")) {
			viewport.init();
			options.refreshFloorsList(0);
		}
		
		// OPEN
		if (arg0.getActionCommand().equals("open")) {
			// Create a file chooser
			JFileChooser fc = new JFileChooser();

			// Open it
			if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				try {
					viewport.open(new FileInputStream(fc.getSelectedFile()));
					
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Error reading file");
				}
			}
			
			options.refreshFloorsList(0);
		}
		
		// SAVE
		if (arg0.getActionCommand().equals("save")) {
			// Create a file chooser
			JFileChooser fc = new JFileChooser();

			// Save it
			if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				try {
					FileWriter f = new FileWriter(fc.getSelectedFile());
					f.write(viewport.save());
					f.close();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Error writing file");
				}
			}
		}
		
		// CHANGE LANGUAGE
		if (arg0.getActionCommand().startsWith("lang-")) {
			Config.change(arg0.getActionCommand().substring(5));
			reloadLang();
		}

		viewport.repaint();
	}

	public void componentHidden(ComponentEvent arg0) {}
	public void componentMoved(ComponentEvent arg0) {}
	public void componentResized(ComponentEvent arg0) {
		Reposition();
		repaint();
	}
	public void componentShown(ComponentEvent arg0) {}


}
