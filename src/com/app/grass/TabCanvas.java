package com.app.grass;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class TabCanvas extends SherlockFragmentActivity implements ActionBar.TabListener {
    private TextView mSelected;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    //    setTheme(SampleList.THEME); //Used for theme switching in samples
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
 
        actionBar.setDisplayShowTitleEnabled(true);

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
 
        ActionBar.Tab tab = getSupportActionBar().newTab();
        tab.setText("tab1");
        tab.setTabListener(new MainCanvas());
        actionBar.addTab(tab);

        tab = getSupportActionBar().newTab();
        tab.setText("tab2");
        tab.setTabListener(new SMSActivity());
        actionBar.addTab(tab);
        
        tab = getSupportActionBar().newTab();
        tab.setText("tab3");
        tab.setTabListener(new BlackList());
        actionBar.addTab(tab);
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction transaction) {
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction transaction) {
    	
        mSelected.setText("Selected: " + tab.getText());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction transaction) {
    }
}
