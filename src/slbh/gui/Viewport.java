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

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;
import javax.swing.JComponent;

@SuppressWarnings("serial")
public class Viewport extends JComponent implements MouseListener, MouseMotionListener {
	private String objectSelected = "";
	private Vector<Vector<Object>> floors;
	private Vector<Object> objects;
	private int startPosition[];
	private int mouseStart[];
	private int mouseEnd[];
	private boolean mousePressed;
	private int zoom;
	private double deltaXY;
	private double deltaZ;
	private int currentFloor;
	private int repeat;

	public Viewport() {
		init();
		
        addMouseListener(this);
        addMouseMotionListener(this);
	}
	
	public void addNonDuplicateObject(Object object) {
    	int size = objects.size();
        for (int i=0; i<size; i++) {
        	Object current = objects.get(i);
        	
        	if ( (current.type.compareTo(object.type) == 0) && (current.position[0] == object.position[0]) && (current.position[1] == object.position[1])) {
        		return;
        	}
        }
    	
    	// If all good
    	objects.add(object);
    }

	public void addNonDuplicateObjects(Vector<Object> _objects) {
    	boolean good;
    	
    	int listSize = _objects.size();
    	for (int j=0; j<listSize; j++) {
    		// Get the next
    		Object object = _objects.get(j);

    		// Search if good
    		good = true;
    		int size = objects.size();
    		for (int i=0; i<size; i++) {
    			Object current = objects.get(i);

    			if ( (current.type.compareTo(object.type) == 0) && (current.position[0] == object.position[0]) && (current.position[1] == object.position[1])) {
    				good = false;
    				break;
    			}
    		}

    		// If all good
    		if (good) objects.add(object);
    	}
    }

	public void changeDelta(double deltaXY, double deltaZ) {
		this.deltaXY = deltaXY;
		this.deltaZ = deltaZ;
	}
	
    public int changeFloor(int number) {
		// Change
		objects = floors.get(number);
		currentFloor = number;
		
		// Repaint
		repaint();
		
		return number;
	}
    
    public void changeObject(String object) {
    	objectSelected = object;
    }
    
    public int createFloor(int number, boolean after) {
		// Create new
		objects = new Vector<Object>();
		currentFloor = number;
		if (after) {
			number++;
			floors.insertElementAt(objects, number);
		} else {
			// Before
			startPosition[2] ++;
			floors.insertElementAt(objects, number);
		}
		
		// Repaint
		repaint();
		
		return number;
	}
    
    public Vector<Object> createListObjects(boolean force) {
    	Vector<Object> result = new Vector<Object>();
    	
    	if (force || mousePressed) {
    		// Wall
    		if (objectSelected.compareTo("wall") == 0){
    			// In X or Y
    			if ( Math.abs((mouseEnd[0]-mouseStart[0])) < Math.abs((mouseEnd[1]-mouseStart[1]))) {
    				// X fixed
    				if (mouseStart[1]<mouseEnd[1]) {
	    				for (double y=mouseStart[1]; y<mouseEnd[1]; y++) {
	    					result.add(new Object(objectSelected, mouseStart[0]+.5, y));
	    				}
    				} else {
	    				for (double y=mouseEnd[1]; y<mouseStart[1]; y++) {
	    					result.add(new Object(objectSelected, mouseStart[0]+.5, y));
	    				}
    				}

    			} else {
    				if (mouseStart[0]<mouseEnd[0]) {
    					// Y fixed
    					for (double x=mouseStart[0]; x<mouseEnd[0]; x++) {
    						result.add(new Object(objectSelected, x, mouseStart[1]+.5));
    					}
    				} else {
    					for (double x=mouseEnd[0]; x<mouseStart[0]; x++) {
    						result.add(new Object(objectSelected, x, mouseStart[1]+.5));
    					}
    				}
    			}
    		} else {
    			// Other
    			int SX = mouseStart[0] < mouseEnd[0] ? mouseStart[0] : mouseEnd[0];
    			int EX = mouseStart[0] < mouseEnd[0] ? mouseEnd[0] : mouseStart[0];
    			int SY = mouseStart[1] < mouseEnd[1] ? mouseStart[1] : mouseEnd[1];
    			int EY = mouseStart[1] < mouseEnd[1] ? mouseEnd[1] : mouseStart[1];
    			
    			for (int x=SX; x<=EX; x++) {
    				for (int y=SY; y<=EY; y++) {
    					result.add(new Object(objectSelected, x, y));
    				}
    			}
    		}
    	}
    	
    	return result;
    }
    
    public slbh.conf.Configuration createScene() {
    	slbh.conf.Configuration myConf = new slbh.conf.Configuration();

		// Say the type of objects
		myConf.AddObjectsConf("floor - " + deltaXY + "x" + deltaXY + "x0.1");
		myConf.AddObjectsConf("wall - 0.1x" + deltaXY + "x" + deltaZ);
		myConf.AddObjectsConf("step - " + (deltaXY/10) + "x" + deltaXY + "x" + (deltaZ/10));


		// All floors
		for (int f=0; f<floors.size(); f++) {
			// All objects
			Vector<Object> nextObj = floors.get(f);
			for (int i=0; i<nextObj.size(); i++) {
				double position[] = new double[3];
				double rotation[] = new double[3];
				Object current = nextObj.get(i);

				// Common
				position[0] = (startPosition[1] - current.position[1]) * deltaXY;
				position[1] = (startPosition[0] - current.position[0]) * deltaXY;
				position[2] = (f - startPosition[2]) * deltaZ;

				rotation[0] = 0;
				rotation[1] = 0;
				rotation[2] = 0;

				if (current.type.compareTo("wall") == 0) {
					// WALL
					int pos = (int)current.position[0];
					position[2] += deltaZ/2;
					if (current.position[0] == pos) {
						// Placed like -
						position[0] += deltaXY;
					} else {
						// Placed like |
						position[1] += deltaXY;
						rotation[2] = 90;
					}
					
					myConf.AddObject(current.type, position, rotation);
				} else if (current.type.compareTo("floor") == 0) {
					// FLOOR
					myConf.AddObject(current.type, position, rotation);
				} else if (current.type.compareTo("stairsN") == 0) {
					// STAIRS NORTH
					position[0] -= deltaXY/2 - deltaXY/20;
					position[2] += + deltaZ/20;
					for (int j=0;j < 10; j++) {
						myConf.AddObject("step", position, rotation);
						position = new double[] {Math.floor(position[0]*10 + deltaXY)/10, position[1], position[2] + deltaZ/10};

					}
				} else if (current.type.compareTo("stairsS") == 0) {
					// STAIRS SOUTH
					position[0] += deltaXY/2 - deltaXY/20;
					position[2] += + deltaZ/20;
					for (int j=0;j < 10; j++) {
						myConf.AddObject("step", position, rotation);
						position = new double[] {Math.floor(position[0]*10 - deltaXY)/10, position[1], position[2] + deltaZ/10};

					}
				} else if (current.type.compareTo("stairsE") == 0) {
					// STAIRS EAST
					rotation[2] = 90;
					position[1] += deltaXY/2 - deltaXY/20;
					position[2] += + deltaZ/20;
					for (int j=0;j < 10; j++) {
						myConf.AddObject("step", position, rotation);
						position = new double[] {position[0], Math.floor(position[1]*10 - deltaXY)/10, position[2] + deltaZ/10};

					}
				} else if (current.type.compareTo("stairsW") == 0) {
					// STAIRS WEST
					rotation[2] = 90;
					position[1] -= deltaXY/2 - deltaXY/20;
					position[2] += + deltaZ/20;
					for (int j=0;j < 10; j++) {
						myConf.AddObject("step", position, rotation);
						position = new double[] {position[0], Math.floor(position[1]*10 + deltaXY)/10, position[2] + deltaZ/10};

					}
				}
			}
		}
		
		return myConf;
	}
    
    public void deleteNearObject(int _x, int _y) {
    	double x, y;
		
    	if ((objectSelected.compareTo("floor") == 0) || ( (objectSelected.length() > 6) && (objectSelected.substring(0, 6).compareTo("stairs") == 0)) ) {
    		// FLOOR or STAIRS
    		x = _x / zoom;
    		y = _y / zoom;
    	} else if (objectSelected.compareTo("wall") == 0) {
    		// WALL
    		double posX = (_x+zoom/2) % zoom;
    		double posY = (_y+zoom/2) % zoom;
    		if ( ((posX < zoom*.3) || (posX > zoom*.7)) && ((posY < zoom*.7) && (posY > zoom*.3)) ) {
    			// -
    			x = _x / zoom;
        		y = _y / zoom + .5;
    		} else if ( ((posY < zoom*.3) || (posY > zoom*.7)) && ((posX < zoom*.7) && (posX > zoom*.3)) ) {	
    			// |
    			x = _x / zoom + .5;
        		y = _y / zoom;
    		} else {
    			x = -1; y = -1;
    		}
    	} else {
    		// None
    		x = -1; y = -1;
    	}

		// Search for the object at that position
		for (int i=0; i<objects.size(); i++) {
			Object current = objects.get(i);
			
			if ( (current.type.compareTo(objectSelected) == 0) && (current.position[0] == x)  && (current.position[1] == y)) {
				objects.remove(i);
				break;
			}
		}
    }

	public double getDeltaXY() {
		return deltaXY;
	}
	
	public double getDeltaZ() {
		return deltaZ;
	}
	public void init() {
		objects = new Vector<Object>();
		startPosition = new int[3];
		mouseStart = new int[2];
		mouseEnd = new int[2];
		mousePressed = false;
		zoom = 20;
		deltaXY = 2;
		deltaZ = 3;
		repeat = 1;
		currentFloor = 0;
		startPosition[0] = 0;
		startPosition[1] = 0;
		startPosition[2] = 0;
		floors = new Vector<Vector<Object>>();
		floors.add(objects);
	}
	public void mouseClicked(MouseEvent arg0) {}
	
	public void mouseDragged(MouseEvent arg0) {
		if (mousePressed) {
			if (objectSelected.compareTo("wall")==0) {
				mouseEnd[0] = (arg0.getX()+5)/zoom;
				mouseEnd[1] = (arg0.getY()+5)/zoom;
			} else {
				mouseEnd[0] = arg0.getX()/zoom;
				mouseEnd[1] = arg0.getY()/zoom;
			}
		} else {
			if ( (arg0.getModifiers() & MouseEvent.BUTTON3_MASK) != 0) {
				// RIGHT BUTTON
				deleteNearObject(arg0.getX(), arg0.getY());
			}
		}
		
		repaint();
	}
	
	public void mouseEntered(MouseEvent arg0) {}

	public void mouseExited(MouseEvent arg0) {}

	public void mouseMoved(MouseEvent arg0) {}
	
	public void mousePressed(MouseEvent arg0) {
		// LEFT BUTTON
		if ( (arg0.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
			if (objectSelected.compareTo("wall")==0) {
				mouseStart[0] = mouseEnd[0] = (arg0.getX()+5)/zoom;
				mouseStart[1] = mouseEnd[1] = (arg0.getY()+5)/zoom;
			} else {
				mouseStart[0] = mouseEnd[0] = arg0.getX()/zoom;
				mouseStart[1] = mouseEnd[1] = arg0.getY()/zoom;
			}

			mousePressed = true;
		} else if ( (arg0.getModifiers() & MouseEvent.BUTTON3_MASK) != 0) {
			// RIGHT BUTTON
			deleteNearObject(arg0.getX(), arg0.getY());
		}
		repaint();
	}
	
	public void mouseReleased(MouseEvent arg0) {
		mousePressed = false;
		if (arg0.getButton() == MouseEvent.BUTTON1) {
			if (objectSelected.compareTo("start") == 0) {
				startPosition[0] = arg0.getX()/zoom;
				startPosition[1] = arg0.getY()/zoom;
				startPosition[2] = currentFloor;
			} else {
				addNonDuplicateObjects(createListObjects(true));
			}
			
			repaint();
		}
	}
	
	public int numberOfFloors() {
		return floors.size();
	}
	
	public void open(InputStream in) throws NumberFormatException, IOException {
		BufferedReader myReader = new BufferedReader(new InputStreamReader(in));
		String nextLine;

		// Get a fresh document
		init();
		floors.clear();
		
		// Start position
		nextLine = myReader.readLine();
		String[] parts = nextLine.split(" ");
		for (int i=0; i<3; i++) startPosition[i] = Integer.valueOf(parts[i]).intValue();

		// Deltas
		nextLine = myReader.readLine();
		parts = nextLine.split(" ");
		deltaXY = Double.valueOf(parts[0]).doubleValue();
		deltaZ = Double.valueOf(parts[1]).doubleValue();
		
		// Repeat
		nextLine = myReader.readLine();
		repeat = Integer.valueOf(nextLine);
		
		// Get the objects
		while ( (nextLine = myReader.readLine()) != null) {
			// Check if new floor
			if (nextLine.charAt(0) == '-') {
				objects = new Vector<Object>();
				floors.add(objects);
				continue;
			}
			
			// Parse it
			String[] obj = nextLine.split(" ");
			
			// Objects
			objects.add(new Object(obj[0], Double.valueOf(obj[1]).doubleValue(), Double.valueOf(obj[2]).doubleValue()));
		}
	}
	
	public void paintComponent(Graphics g) {
        super.paintComponent(g); 
        
        // Set the constants
        int width = getWidth();
        int height = getHeight();
        
        // Clear the component
        g.setColor(Color.black);
        g.fillRect(0, 0, width, height);
        
        // Show the grid
        g.setColor(Color.white);
        for (int x=0; x<width; x += zoom) g.drawLine(x, 0, x, height);
        for (int y=0; y<height; y += zoom) g.drawLine(0, y, width, y);
        
        // Get the objects to draw
        Vector<Object> toDraw = new Vector<Object>();
        toDraw.addAll(objects);
        toDraw.addAll(createListObjects(false));
        
        
        // Draw scene
        int size = toDraw.size();
        for (int i=0; i<size; i++) {
        	Object current = toDraw.get(i);
        	
        	// FLOOR
        	if (current.type.compareTo("floor") == 0) {
        		g.setColor(Color.white);
        		int x = (int) current.position[0]*zoom;
        		int y = (int) current.position[1]*zoom;
        		g.fillRect(x+1, y+1, zoom-1, zoom-1);
        	}
        	
        	// WALL
        	if (current.type.compareTo("wall") == 0) {
        		g.setColor(Color.blue);
        		int x = (int) current.position[0]*zoom;
        		int y = (int) current.position[1]*zoom;
        		if (current.position[0]*zoom != x ) {
        			g.fillRect(x-1, y-1, 2, zoom+1);
        		} else {
        			g.fillRect(x-1, y-1, zoom+1, 2);
        		}
        	}

        	// STAIRS
        	if (current.type.compareTo("stairsN") == 0) {
        		g.setColor(Color.green);
        		g.fillRect( (int) (current.position[0]*zoom + .2*zoom), (int) current.position[1]*zoom, (int) (zoom*.6), (int) (.5*zoom));
        	}
        	if (current.type.compareTo("stairsS") == 0) {
        		g.setColor(Color.green);
        		g.fillRect( (int) (current.position[0]*zoom + .2*zoom), (int) (current.position[1]*zoom + .5*zoom), (int) (zoom*.6), (int) (.5*zoom));
        	}
        	if (current.type.compareTo("stairsW") == 0) {
        		g.setColor(Color.green);
        		g.fillRect((int) current.position[0]*zoom , (int) (current.position[1]*zoom + .2*zoom), (int) (zoom*.5), (int) (.6*zoom));
        	}
        	if (current.type.compareTo("stairsE") == 0) {
        		g.setColor(Color.green);
        		g.fillRect((int) (current.position[0]*zoom + .5*zoom) , (int) (current.position[1]*zoom + .2*zoom), (int) (zoom*.5), (int) (.6*zoom));
        	}
        }


        // Draw the start position
        if (currentFloor == startPosition[2]){
        	int x = startPosition[0]*zoom;
        	int y = startPosition[1]*zoom;
        	g.setColor(Color.red);
        	g.drawLine(x, y, x+zoom, y+zoom);
        	g.drawLine(x, y+zoom, x+zoom, y);
        }
    }
	
	public int removeFloor(int number) {
		// Remove
		floors.remove(number);
		
		// Check if too big
		if (number >= floors.size()) number = floors.size()-1;
		
		// Check if no more floors
		if (floors.size() == 0) {
			floors.add(new Vector<Object>());
			number = 0;
		}
		
		// Change
		objects = floors.get(number);
		currentFloor = number;
		
		// Repaint
		repaint();
		
		return number;
	}	
	
	public String save() {
		String result = "";
		
		// Start position
		for (int i=0; i<3; i++) result += startPosition[i] + " ";
		result += "\n";
		
		// Deltas
		result += deltaXY + " " + deltaZ + "\n";
		
		// Repeat
		result += repeat + "\n";
		
		// Objects
		for (int i=0; i<floors.size(); i++) {
			result += "-\n";
			Vector<Object> current = floors.get(i);
			for (int j=0; j<current.size(); j++) {
				Object object = current.get(j);
				result += object.type + " " + object.position[0] + " " + object.position[1] + "\n";
			}
		}
		
		return result;
	}

	public int getRepeat() {
		return repeat;
	}
	
	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}
}
