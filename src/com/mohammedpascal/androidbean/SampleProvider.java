package com.mohammedpascal.androidbean;


/**
 * Content provider for feedback item
 * 
 * @author Mohammed Mustafa
 * 
 */
public class SampleProvider extends BeanProvider {
	
	@Override
	Class<?> getEntity() {
		return User.class;
	}	
}