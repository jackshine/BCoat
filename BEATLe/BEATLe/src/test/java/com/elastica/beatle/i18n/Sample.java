package com.elastica.beatle.i18n;

public class Sample {
	public static void main(String args[]) {
		System.out.println(I18N.getString("es_es"));
		System.out.println(I18N.getString("title","es_es"));
		System.out.println(I18N.getString("greeting","es_es"));
		System.out.println(I18N.getString("language","es_es"));
		
		System.out.println(I18N.getString("hi_in"));
		System.out.println(I18N.getString("title","hi_in"));
		System.out.println(I18N.getString("greeting","hi_in"));
		System.out.println(I18N.getString("language","hi_in"));
	}
}
