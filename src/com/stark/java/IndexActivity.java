package com.mycompany.myapp3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class IndexActivity extends Activity {
    private Button gotoLib, aboutBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
		getActionBar().hide();
		
		gotoLib=findViewById(R.id.gotoLib);
		gotoLib.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1) {
					Intent i = new Intent(getApplicationContext(),MainActivity.class);
					startActivity(i);
				}
			});
		aboutBtn = findViewById(R.id.aboutBtn);
		aboutBtn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1) {
					AlertDialog.Builder b = new AlertDialog.Builder(IndexActivity.this);
						b.setTitle("About");
						b.setMessage("This is an application where the user can add, update and delete a book (Title, Author and Year). It will store all the add books information in the application's database where the user can also view and update and remove it anytime they want.");
						b.show();
				}
			});
    }
    
}
