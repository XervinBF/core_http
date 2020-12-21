package org.xbf.addons.core_http;

public enum KeyLevel {

	TOP(0),
	CONFIG(1),
	STATISTICS(2),
	USER(3),
	NONE(1000);
	
	public int level;
	
	KeyLevel(int level) {
		this.level = level;
	}

	public static KeyLevel of(int l) {
		switch (l) {
			case 0: return TOP;
			case 1: return CONFIG;
			case 2: return STATISTICS;
			case 3: return USER;
			default: return NONE;
		}
	}
	
}
