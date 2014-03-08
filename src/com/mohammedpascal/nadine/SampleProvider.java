package com.mohammedpascal.nadine;


/**
 * Content provider for feedback item
 * 
 * @author Mohammed Mustafa
 * 
 */
public class SampleProvider extends BeanProvider {
	
	@Override
	public Class<?> getEntity() {
		return User.class;
	}	
}