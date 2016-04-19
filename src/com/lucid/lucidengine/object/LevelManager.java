package com.lucid.lucidengine.object;

import java.util.ArrayList;

/**
 * @author Mark Diedericks
 *
 */

public class LevelManager {
	
	private ArrayList<Map> maps;
	public Map currentMap;
	
	public LevelManager() {
		maps = new ArrayList<Map>();
	}
	
	public void addMap(Map map) {
		maps.add(map);
	}
	
	public void loadMap(String name) {
		for(Map map : maps) {
			if(map.name.equals(name)) {
				this.currentMap = map;
				break;
			}
		}
	}
	
}
