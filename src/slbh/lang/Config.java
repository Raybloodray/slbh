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

import java.util.List;
import java.util.Vector;
import java.util.prefs.Preferences;

public class Config {
	static private Language myLanguage;
	static private String lang;
	static private Preferences myPref;
	static private List<Language> allLangs;
	
	static {
		allLangs = new Vector<Language>();
		
		allLangs.add(new English());
		allLangs.add(new French());
	}
	
	static public void load() {
		// Load config
		myPref = Preferences.userRoot().node("/slbh");
		lang = myPref.get("lang", "English");
		change(lang);
	}
	
	static public void change(String _lang) {
		lang = _lang;
		
		for (Language l: allLangs) {
			if (l.lang().equals(lang)) {
				myLanguage = l;
				break;
			}
		}
		
		// Save config
		myPref.put("lang", lang);
	}
	
	static public Language getMyLanguage() {
		return myLanguage;
	}
	
	static public String[] getAllLanguages() {
		String[] result = new String[allLangs.size()];
		
		int pos = 0;
		for (Language l: allLangs) {
			result[pos++] = l.lang();
		}

		return result;
	}
}
