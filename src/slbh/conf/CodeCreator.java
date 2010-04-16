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

import java.util.Vector;

public class CodeCreator {
	static private int maxRange = 750;
	static private int error = -1;
	
	static public String Entry(Vector<Object> objects, int high) {
		String result = "";
		
		// The starting point
		int[] currentPos = new int[3];
		for(int i=0; i<3; i++) currentPos[i] = 0;
		
		// Make a new list and the current position
		Vector<Object> rest = objects;
		
		// Check for the biggest lines
		result += BiggestLines(currentPos, rest);
		
		// Ask for the normal procedure
		result += Normal(currentPos, rest);
		
		// Up the last floor
		result += GoSomewhere(currentPos, new int[] {0, 0, high});
		
		return result;
	}
	
	// All the objects are created one at a time
	static private String Normal(int currentPos[], Vector<Object> objects) {
		String result = "";
		
		// Find the nearest object
		while (!objects.isEmpty()) {
			double min = objects.get(0).DistanceSquare(currentPos);
			int index = 0; 
			for (int i=1; i<objects.size(); i++) {
				double next = objects.get(i).DistanceSquare(currentPos);
				if (next < min) {
					min = next;
					index = i;
				}
			}
			
			// Take it
			Object myObject = objects.remove(index);
			
			// Go to it if needed
			result += GoSomewhere(currentPos, myObject);
			
			// Create it
			int position[] = myObject.Direction(currentPos, 0);
			double rotation[] = myObject.getRotation();
			if ((rotation[0] == 0) && (rotation[1] == 0) && (rotation[2] == 0))
				result += "\t\tllRezObject(\"" + myObject.getName() + "\", llGetPos() + <" + posToString(position[0]) + "," + posToString(position[1]) + "," + posToString(position[2]) + ">, <0,0,0>, <0,0,0,0>, live);\n";
			else
				result += "\t\tllRezObject(\"" + myObject.getName() + "\", llGetPos() + <" + posToString(position[0]) + "," + posToString(position[1]) + "," + posToString(position[2]) + ">, <0,0,0>, llEuler2Rot(<" + rotation[0] + "," + rotation[1] + "," + rotation[2] + ">* DEG_TO_RAD), live);\n";

		}
		
		return result;
	}
	
	static private String posToString(int pos) {
		String result = "";
		
		if (pos < 0) {
			result += "-";
			pos *= -1;
		}
		
		result += pos/100 + "." + pos%100;		
		return result;
	}

	// Create the biggest lines
	static private String BiggestLines(int currentPos[], Vector<Object> objects) {
		String result = "";
		Vector<Line> lines = new Vector<Line>();
		
		do {
			// Remove all items
			lines.clear();
			
			// Find all the possible lines of at least 3 objects
			// For all objects
			Object first, second;
			double x,y,z;
			int dx,dy,dz;
			for (int i=0; i<objects.size(); i++) {
				first = objects.get(i);
				// Take another object
				for (int j=0; j<objects.size(); j++) {
					second = objects.get(j);
					// Check for the same name and rotation
					if ( (i == j) || 
							(first.getName().compareTo(second.getName()) != 0) || 
							(first.getRotation()[0] != second.getRotation()[0]) ||
							(first.getRotation()[1] != second.getRotation()[1]) ||
							(first.getRotation()[2] != second.getRotation()[2])) 
						continue;

					// Get the delta
					dx = second.getPosition()[0] - first.getPosition()[0];
					dy = second.getPosition()[1] - first.getPosition()[1];
					dz = second.getPosition()[2] - first.getPosition()[2];
					Line newLine = new Line();
					newLine.startingObject = first;
					newLine.numberOfObjects = 2;
					newLine.delta[0] = dx;
					newLine.delta[1] = dy;
					newLine.delta[2] = dz;

					// If all deltas are 0, don't bother
					if ( (dx == 0) && (dy == 0) && (dz == 0)) continue;

					// Look if there is another object farther
					int last;
					do {
						last = newLine.numberOfObjects;
						x = second.getPosition()[0] + dx;
						y = second.getPosition()[1] + dy;
						z = second.getPosition()[2] + dz;
						for (int k=0; k<objects.size(); k++) {
							second = objects.get(k);
							if ( (second.getPosition()[0] == x) &&
									(second.getPosition()[1] == y) &&
									(second.getPosition()[2] == z) &&
									(first.getName().compareTo(second.getName()) == 0) &&
									(first.getRotation()[0] == second.getRotation()[0]) &&
									(first.getRotation()[1] == second.getRotation()[1]) &&
									(first.getRotation()[2] == second.getRotation()[2])) {
								newLine.numberOfObjects++;
								break;
							}
						} 
					} while(newLine.numberOfObjects != last);
					if (newLine.numberOfObjects >= 3) lines.add(newLine);
				}
			}
			
			// Check if empty
			if (lines.isEmpty()) break;

			// Take the biggest and nearest line
			int index = 0;
			int max = lines.get(0).numberOfObjects;
			int distance = lines.get(0).startingObject.DistanceSquare(currentPos);
			for (int i=1; i<lines.size(); i++) {
				if (lines.get(i).numberOfObjects >= max) {
					max = lines.get(i).numberOfObjects;
					// The nearest of the two
					if (lines.get(i).numberOfObjects == max) {
						if (lines.get(i).startingObject.DistanceSquare(currentPos) < distance) {
							index = i;
							distance = lines.get(i).startingObject.DistanceSquare(currentPos);
						}
					} else {
						// The biggest
						index = i;
						distance = lines.get(i).startingObject.DistanceSquare(currentPos);
					}
				}
			}
			
			// Draw it
			// Get the first one
			first = lines.get(index).startingObject;
			int numberOfObjects = lines.get(index).numberOfObjects;
			int delta[] = new int[3];
			for (int i=0; i<3; i++) delta[i] = lines.get(index).delta[i];
			
			// Go to the first one
			result += GoSomewhere(currentPos, first);
			
			// Keep all the lines in range
			for (int i=0; i<lines.size(); i++) {
				// When to delete
				if ( (lines.get(i).numberOfObjects != numberOfObjects) ||
						(lines.get(i).delta[0] != delta[0]) ||
						(lines.get(i).delta[1] != delta[1]) ||
						(lines.get(i).delta[2] != delta[2]) ||
						(lines.get(i).startingObject.DistanceSquare(currentPos) >= maxRange*maxRange) ) {
					lines.remove(i);
					i--;
				}
			}
			
			// The header
			result += "\t\tfor(i=0; i<" + numberOfObjects + "; i++) {\n";
			// Get the maximum i
			int imax = (int) ((maxRange/2)/(Math.sqrt((Math.pow(delta[0],2) + Math.pow(delta[1],2) + Math.pow(delta[2],2)))) + 1);
			// All the lines
			for (int i=0; i<lines.size(); i++) {
				first = lines.get(i).startingObject;
				int position[] = first.Direction(currentPos, 0);
				double rotation[] = first.getRotation();
				
				// The base
				result += "\t\t\tllRezObject(\"" + first.getName() + "\", llGetPos() + <" + posToString(position[0]) + "," + posToString(position[1]) + "," + posToString(position[2]) + "> ";
				if (imax != 1) {
					// The delta multiplier
					if (imax < numberOfObjects) result += "+ (i%" + imax + ")"; else result += "+ i";
					// The delta
					result += "*<" + posToString(delta[0]) + "," + posToString(delta[1]) + "," + posToString(delta[2]) + ">";
				}
				// The speed
				result += ", <0,0,0>, ";
				// The rotation
				if ((rotation[0] == 0) && (rotation[1] == 0) && (rotation[2] == 0))
					result += "<0,0,0,0>, live);\n";
				else
					result += "llEuler2Rot(<" + rotation[0] + "," + rotation[1] + "," + rotation[2] + ">* DEG_TO_RAD), live);\n";
			}

			// The footer
			if (imax == 1) {
				result += GoSomewhere(currentPos, new int[] {currentPos[0]+delta[0], currentPos[1]+delta[1], currentPos[2]+delta[2]});
				for (int advance = 1; advance<numberOfObjects; advance++) {
					GoSomewhere(currentPos, new int[] {currentPos[0]+delta[0], currentPos[1]+delta[1], currentPos[2]+delta[2]});
				}
			} else if (imax < numberOfObjects) {
				result += "\t\t\tif(i%" + imax + "==" + (imax-1) + ") llSetPos(llGetPos() + <" + posToString(delta[0]*imax) + "," + posToString(delta[1]*imax) + "," + posToString(delta[2]*imax) + ">);\n";
				// Advance
				int numberOfJumps = (numberOfObjects)/imax;
				for (int i=0; i<3; i++) currentPos[i] += numberOfJumps*delta[i]*imax;
			}
			result += "\t\t}\n";
			if (error != -1) result += "if (llGetPos() - posinit - <" + posToString(currentPos[0]) + "," + posToString(currentPos[1]) + "," + posToString(currentPos[2]) + "> != <0,0,0>) llOwnerSay( \"" + (error++) + " -> " + currentPos[0] + " " + currentPos[1] + " " + currentPos[2] + "\" + (string) (llGetPos() - posinit));\n";
			
			// Erase them all
			for (int i=0; i<lines.size(); i++)
				lines.get(i).DeleteLine(objects);
		} while (!lines.isEmpty());
		
		return result;
	}
	
	// Move near an object
	static private String GoSomewhere(int currentPos[], Object object) {
		String result = "";
		int counter = 0;
		
		// Get the direction 
		int ddelta[] = object.Direction(currentPos, maxRange);
		int delta[] = new int[3];
		for (int i=0; i<3; i++) delta[i] = (int) ddelta[i]/100;
		for (int i=0; i<3; i++) delta[i] = (int) delta[i]*100;
		// Advance as much as needed
		while (object.DistanceSquare(currentPos) > maxRange*maxRange) {
			counter ++;
			for (int i=0; i<3; i++) currentPos[i] += delta[i];
		}
		
		// Check how much time we need to advance
		if (counter > 0) {
			if (counter > 1) result += "\t\tfor(j=0; j<" + counter + "; j++) ";
			result += "\t\tllSetPos(llGetPos() + <" + posToString(delta[0]) + "," + posToString(delta[1]) + "," + posToString(delta[2]) + ">);\n";
			if (error != -1) result += "if (llGetPos() - posinit - <" + posToString(currentPos[0]) + "," + posToString(currentPos[1]) + "," + posToString(currentPos[2]) + "> != <0,0,0>) llOwnerSay( \"" + (error++) + " -> " + currentPos[0] + " " + currentPos[1] + " " + currentPos[2] + "\" + (string) (llGetPos() - posinit));\n";
		}
		
		return result;
	}
	
	static private String GoSomewhere(int currentPos[], int nextPos[]) {
		String result = "";
		int counter = 0;
		
		// Get the direction 
		Object object = new Object("temp", nextPos, null);
		int ddelta[] = object.Direction(currentPos, maxRange);
		int delta[] = new int[3];
		for (int i=0; i<3; i++) delta[i] = (int) ddelta[i]/100;
		for (int i=0; i<3; i++) delta[i] = (int) delta[i]*100;
		// Advance as much as needed
		while (object.DistanceSquare(currentPos) > maxRange*maxRange) {
			counter ++;
			for (int i=0; i<3; i++) currentPos[i] += delta[i];
		}
		
		// Check how much time we need to advance
		if (counter > 0) {
			if (counter > 1) result += "\t\tfor(j=0; j<" + counter + "; j++) ";
			result += "\t\tllSetPos(llGetPos() + <" + posToString(delta[0]) + "," + posToString(delta[1]) + "," + posToString(delta[2]) + ">);\n";
		}
		
		// The last part
		if (object.DistanceSquare(currentPos) != 0) {
			delta = object.Direction(currentPos, 0);
			result += "\t\tllSetPos(llGetPos() + <" + posToString(delta[0]) + "," + posToString(delta[1]) + "," + posToString(delta[2]) + ">);\n";
			for (int i=0; i<3; i++) currentPos[i] += delta[i];
		}
		
		return result;
	}
	
	static private class Line {
		public Object startingObject = null;
		public int delta[] = new int[3];
		public int numberOfObjects = 0;
		
		public void DeleteLine(Vector<Object> objects) {
			Object second, first;
			second = first = startingObject;
			for (int i=0; i<numberOfObjects; i++) {
				int x,y,z;
				x = second.getPosition()[0] + (i==0?0:delta[0]);
				y = second.getPosition()[1] + (i==0?0:delta[1]);
				z = second.getPosition()[2] + (i==0?0:delta[2]);
				for (int k=0; k<objects.size(); k++) {
					second = objects.get(k);
					if ( (second.getPosition()[0] == x) &&
							(second.getPosition()[1] == y) &&
							(second.getPosition()[2] == z) &&
							(first.getName().compareTo(second.getName()) == 0) &&
							(first.getRotation()[0] == second.getRotation()[0]) &&
							(first.getRotation()[1] == second.getRotation()[1]) &&
							(first.getRotation()[2] == second.getRotation()[2])) {
						objects.remove(k);
						break;
					}
				} 
			}
		}
	}

}
