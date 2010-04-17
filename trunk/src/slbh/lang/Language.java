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

package slbh.lang;

public interface Language {
	public String buttonAddFloorAfter();
	public String buttonAddFloorBefore();
	public String buttonCancel();
	public String buttonProperties();
	public String buttonRemoveFloor();
	public String buttonToClipboard();
	public String labelBackgroundFloor();
	public String labelCurrentFloor();
	public String labelObjects();
	public String lang();
	public String menuFile();
	public String menuLanguage();
	public String menuNew();
	public String menuOpen();
	public String menuSave();
	public String objectFloor();
	public String objectStairsE();
	public String objectStairsN();
	public String objectStairsS();
	public String objectStairsW();
	public String objectStart();
	public String objectWall();
	public String propertiesDeltaXY();
	public String propertiesDeltaZ();
	public String propertiesRepeat();
}
