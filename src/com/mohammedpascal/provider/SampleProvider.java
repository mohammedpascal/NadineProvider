package com.mohammedpascal.provider;


/**
 * Content provider for feedback item
 * 
 * @author Mohammed Mustafa
 * 
 */
public class SampleProvider extends NadineProvider {
	
	public String[] getColumns(){
		return new String[]{ "username", "password" };
	}
	
}