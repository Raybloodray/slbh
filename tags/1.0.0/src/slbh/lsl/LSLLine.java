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

package slbh.lsl;

import java.util.Vector;

public class LSLLine {
	public LSLObject startingObject = null;
	public int delta[] = new int[3];
	public int numberOfObjects = 0;
	
	public void DeleteLine(Vector<LSLObject> objects) {
		LSLObject second, first;
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
