package com.example.fragementstest;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
 
 
public class MainActivity extends Activity {
	Button button2;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
    }
      
    public void selectFrag(View view) {
         Fragment fr;
          
         if(view == findViewById(R.id.button2)) {
             fr = new FragmentTwo();
          
         }else {
             fr = new FragmentOne();
         }
          
         FragmentManager fm = getFragmentManager();
         FragmentTransaction fragmentTransaction = fm.beginTransaction();
         fragmentTransaction.replace(R.id.fragment_place, fr);
         fragmentTransaction.commit();
          
    }
    
}