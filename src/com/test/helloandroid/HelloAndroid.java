package com.test.helloandroid;

import android.app.Activity;
import android.os.Bundle;

public class HelloAndroid extends Activity{
    /** Called when the activity is first created. */
	CustomDrawableView bob;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bob = new CustomDrawableView(this);
        setContentView(bob);
    }
}