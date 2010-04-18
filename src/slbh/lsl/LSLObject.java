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

public class LSLObject {
	private String name;
	private int position[] = new int[3];
	private double rotation[] = new double[3];
	
	public LSLObject(String name, int[] position, double[] rotation) {
		this.name = name;
		this.position = position;
		this.rotation = rotation;
	}
	
	public void Print() {
		System.out.print("Object " + name + " ");
		for (int i=0; i<3; i++) {
			System.out.print(position[i] + ",");
		}
		for (int i=0; i<3; i++) {
			System.out.print(rotation[i] + ",");
		}
		
		System.out.println();
	}
	
	public int DistanceSquare(int[] other) {
		return (int) ( Math.pow((position[0]-other[0]),2) + Math.pow((position[1]-other[1]),2) + Math.pow((position[2]-other[2]),2) );
	}
	
	public int[] Direction(int[] other, int mult) {
		int result[] = new int[3];
		
		// Get the delta
		for (int i=0; i<3; i++) result[i] = position[i]-other[i];
		
		// Normalize
		double size = 0;
		for (int i=0; i<3; i++) size += Math.pow(result[i], 2);
		
		// Multiply
		if (mult != 0) size = mult/Math.sqrt(size);
		else size = 1;
		
		// Divide
		for (int i=0; i<3; i++) result[i] *= size;
		
		return result;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int[] getPosition() {
		return position;
	}

	public void setPosition(int[] position) {
		this.position = position;
	}

	public double[] getRotation() {
		return rotation;
	}

	public void setRotation(double[] rotation) {
		this.rotation = rotation;
	}
	
}
