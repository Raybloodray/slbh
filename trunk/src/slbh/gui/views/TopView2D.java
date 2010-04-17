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

package slbh.gui.views;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;

import slbh.scene.Scene;
import slbh.scene.SceneObject;

public class TopView2D {
	private Scene myScene;
	private int currentFloor;
	
	public TopView2D(Scene scene) {
		myScene = scene;
	}
	
	public int getCurrentFloor() {
		return currentFloor;
	}

	public Scene getMyScene() {
		return myScene;
	}

	public void paintObjects(Graphics g, Vector<SceneObject> toDraw, int zoom) {
        for (SceneObject current : toDraw) {        	
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
        if (currentFloor == myScene.startPosition[2]){
        	int x = myScene.startPosition[0]*zoom;
        	int y = myScene.startPosition[1]*zoom;
        	g.setColor(Color.red);
        	g.drawLine(x, y, x+zoom, y+zoom);
        	g.drawLine(x, y+zoom, x+zoom, y);
        }
	}

	public void paintScene(Graphics g, int zoom) {
		paintObjects(g, myScene.getFloorObjects(currentFloor), zoom);
	}

	public void setCurrentFloor(int currentFloor) {
		this.currentFloor = currentFloor;
	}
	
	public void setMyScene(Scene myScene) {
		this.myScene = myScene;
	}
}
