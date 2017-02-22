package com.elekscamp.messenger_javafx_client;

import translation.Dictionary;

public class GlobalVariables {
	
	public enum Language {
		ENGLISH,
		UKRAINIAN
	}
	
	public static Language language = Language.UKRAINIAN;
	public static Dictionary languageDictionary;
}
