package com.mycompany.myapp3;
 
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends Activity { 
    private Database db;
	private Cursor cur;
	private ArrayList<Book> items;
	private ListView lv;
	private Dialog addDialog, editDialog;
	private ImageView addTrigger;
	private EditText addTitle, addAuthor, addYear, editTitle, editAuthor, editYear, searchBar;
	private Button addBtn, saveBtn, delBtn;
	private Book currentBook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
	
		db = new Database(this);
		items = new ArrayList<Book>();
		lv = findViewById(R.id.listView);
		
		searchBar = findViewById(R.id.searchBar);
		//Init add item dialog
		addDialog = new Dialog(MainActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
		addDialog.setTitle("Add Book");
		addDialog.setContentView(R.layout.add_item);
	    addDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		addDialog.setCancelable(true);
		
		
		//Add Input views
		addTitle = addDialog.findViewById(R.id.addTitle);
		addAuthor = addDialog.findViewById(R.id.addAuthor);
		addYear = addDialog.findViewById(R.id.addYear);
		
		//Set popup modal trigger for add item
		addTrigger = findViewById(R.id.addTrigger);
		addTrigger.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1) {
					addDialog.show();
				}
			});
		
		//Edit dialog
		editDialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
		editDialog.setTitle("Edit Book Record");
		editDialog.setContentView(R.layout.edit_item);
		editDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		editDialog.setCancelable(true);
		
		editTitle = editDialog.findViewById(R.id.editTitle);
		editAuthor = editDialog.findViewById(R.id.editAuthor);
		editYear = editDialog.findViewById(R.id.editYear);
		
		saveBtn = editDialog.findViewById(R.id.saveBtn);
		addBtn = addDialog.findViewById(R.id.addBtn);
		delBtn = editDialog.findViewById(R.id.delBtn);
		try{
			//Initialize app, get all data in db
			getAllData();
			
			
			//Handle Add Item Query
			addBtn.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View p1) {
						String title = addTitle.getText().toString();
						String author = addAuthor.getText().toString();
						String year = addYear.getText().toString();
						if(title.length()==0 || author.length()==0 || year.length()==0){
							Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_LONG).show();
						}else{
							db.insertData(title, author, year);
							Toast.makeText(getApplicationContext(), "Book added to database", Toast.LENGTH_LONG).show();
							addDialog.hide();
							getAllData();
							addTitle.setText("");
							addAuthor.setText("");
							addYear.setText("");
						}
					}
				});
			
				//Handle searchbar
			searchBar.addTextChangedListener(new TextWatcher(){
					@Override
					public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
					}

					@Override
					public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
					}

					@Override
					public void afterTextChanged(Editable p1) {
						String search = searchBar.getText().toString().trim().toUpperCase();
						if(search.equals("")){
							getAllData();
						}else{
							searchData(search);
						}
					}
				});
				
				
				//Handle update book
			saveBtn.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View p1) {
						String id=currentBook.id;
						String title=editTitle.getText().toString();
						String author = editAuthor.getText().toString();
						String year = editYear.getText().toString();
						
						if(title.length()==0||author.length()==0||year.length()==0){
							Toast.makeText(getApplicationContext(), "All fields must be filled", Toast.LENGTH_LONG).show();
						}else{
							db.updateData(id,title,author,year);
							getAllData();
							editDialog.hide();
							Toast.makeText(getApplicationContext(), "Book was updated successfully", Toast.LENGTH_LONG).show();
						}
					}
				});
				
				//Handle delete specific book
			delBtn.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View p1) {
						AlertDialog.Builder d = new AlertDialog.Builder(MainActivity.this);
						d.setTitle("Confirm Delete Record");
						d.setMessage("Are you sure you want to delete "+currentBook.title+"?");
						d.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener(){
								public void onClick(DialogInterface dialog, int which){
									db.deleteOne(currentBook.id);
									editDialog.hide();
									getAllData();
									Toast.makeText(getApplicationContext(), currentBook.title+" was deleted successfully", Toast.LENGTH_LONG).show();
									dialog.dismiss();
								}
							});
						d.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener(){
								@Override
								public void onClick(DialogInterface p1, int p2) {
									p1.dismiss();
								}
							});
						d.create().show();
					}
				});
		}catch(Exception e){
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		}
    }
	
	//Handle back press
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
			case android.R.id.home:
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	};
	
	//Fetch all data from db and display on list
	void getAllData(){
		items.clear();
		cur = db.getAllData();
		while(cur.moveToNext()){
			items.add(new Book(cur.getString(0), cur.getString(1), cur.getString(2), cur.getString(3)));
		}
		BookAdapter itemsAdapter = new BookAdapter(this, items);
		lv.setAdapter(itemsAdapter);
		
		// Show edit dialog when list item clicked
		lv.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
					Book book = items.get(p3);
					currentBook = book;
					editTitle.setText(book.title);
					editAuthor.setText(book.author);
					editYear.setText(book.year);
					Toast.makeText(getApplicationContext(), book.title, Toast.LENGTH_LONG).show();
					editDialog.show();
				}
			});
	}
	
	
	//Search data from db and display on list
	void searchData(String search){
		getAllData();
		
		for(Iterator<Book> it=items.iterator(); it.hasNext();){
			if(!it.next().title.toUpperCase().contains(search)){
				it.remove();
			}
		}
		
		BookAdapter itemsAdapter = new BookAdapter(this, items);
		lv.setAdapter(itemsAdapter);

		// Show edit dialog when list item clicked
		lv.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
					Book book = items.get(p3);
					currentBook = book;
					editTitle.setText(book.title);
					editAuthor.setText(book.author);
					editYear.setText(book.year);
					Toast.makeText(getApplicationContext(), book.title, Toast.LENGTH_LONG).show();
					editDialog.show();
				}
			});
	}
	
}
