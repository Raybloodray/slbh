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

package slbh.conf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

public class Configuration {
	private Vector<Object> objects = new Vector<Object>();
	private String objectsConf = "";
	
	public Configuration() {
		Init();
	}
	
	public void AddObjectsConf(String text) {
		objectsConf += "// " + text + "\n";
	}
	
	public Configuration(InputStream _input) {
		Init();
		BufferedReader myReader = new BufferedReader(new InputStreamReader(_input));
		
		// Get the next line
		String nextLine;
		try {
			while ( (nextLine = myReader.readLine()) != null) {
				// Parse it
				String[] command = nextLine.split(" ");
				
				// Check the command
				// OBJECT
				if (command[0].compareToIgnoreCase("object") == 0) {
					double[] position = new double[3];
					String[] tmp = command[2].split(",");
					for(int i=0; i<3; i++) position[i] = Double.valueOf(tmp[i]).doubleValue();
					double[] rotation = new double[3];
					tmp = command[3].split(",");
					for(int i=0; i<3; i++) rotation[i] = Double.valueOf(tmp[i]).doubleValue();
					
					AddObject(command[1], position, rotation);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void Init() {
		
	}
	
	public void AddObject(String name, double position[], double rotation[]) {
		int pos[] = new int[3];
		for (int i=0; i<3; i++) pos[i] = (int) Math.round(position[i]*100);
		objects.add(new Object(name, pos, rotation));
	}
	
	public String ToSLScript(int high, int repeat) {
		String result = objectsConf;
		result += "integer live = 60;\n\n";
		result += "integer i;\n";
		result += "integer j;\n";
		result += "vector posinit;\n";
		result += "default {\n";
		result += "\tstate_entry() {posinit = llGetPos();}\n";
		result += "\ttouch (integer param) {\n";
		result += "\t\tllSetAlpha(0, ALL_SIDES);\n";
		
		if (repeat > 1) {
			result += "\n\t\tinteger r;\n";
			result += "\t\tfor(r=0; r<" + repeat + "; r++) {\n\n";
		}
		
		result += CodeCreator.Entry(objects, high);
		
		if (repeat > 1) {
			result += "\n\t\t}\n\n";
		}
		
		result += "\t\tllDie();\n";
		result += "\t}\n";
		result += "}";
		
		return result;
	}
}
