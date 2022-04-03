package com.mycompany.myapp3;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

	public final static String DATABASE_NAME="Books.db";
	public final static String TABLE_NAME="Book";
	public final static String COL1="ID";
	public final static String COL2="title";
	public final static String COL3="author";
	public final static String COL4="year";
	
	private SQLiteDatabase db;
	
	private ContentValues cv;
	public Database(Context context){
		super(context, DATABASE_NAME,null,1);
		db=this.getWritableDatabase();
		cv = new ContentValues();
	}
	
	@Override 
    public void onCreate(SQLiteDatabase db) { 
		db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"("+COL1+" INTEGER PRIMARY KEY AUTOINCREMENT,"+COL2+" TEXT COLLATE NOCASE,"+COL3+" TEXT COLLATE NOCASE,"+COL4+" TEXT)"); 
    } 

    @Override 
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { 
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME); 
		onCreate(db); 
    }
	
	public void insertData(String title, String author, String year) { 
		cv.put(COL2, title); 
		cv.put(COL3, author); 
		cv.put(COL4, year);
		db.insert(TABLE_NAME, null, cv);
    } 

	public Cursor getData(String title){ 
		SQLiteDatabase db = this.getWritableDatabase(); 
		String query="SELECT * FROM "+TABLE_NAME+" WHERE "+COL2+"='"+title+"'"; 
		Cursor  cursor = db.rawQuery(query,null); 
		return cursor; 
    } 
	
	public Cursor searchData(String search){ 
		SQLiteDatabase db = this.getWritableDatabase(); 
		String query="SELECT * FROM "+TABLE_NAME+" WHERE "+COL2+"='"+search+"' OR "+COL3+"='"+search+"' COLLATE NOCASE"; 
		Cursor  cursor = db.rawQuery(query,null); 
		return cursor; 
    } 
	
	public Cursor getAllData() { 
		Cursor res = db.rawQuery("SELECT * FROM "+TABLE_NAME, null); 
		return res; 
    } 
    
	public void updateData(String id,String title, String author, String year) { 
		cv.put(COL1, id); 
		cv.put(COL2, title); 
		cv.put(COL3, author); 
		cv.put(COL4, year); 
		db.update(TABLE_NAME, cv, "ID=?", new String[]{id}); 
    } 
	
	public void deleteAll(){
		db.execSQL("DELETE FROM "+TABLE_NAME);
	}
	
	public void deleteOne(String id){
		db.execSQL("DELETE FROM "+TABLE_NAME+" WHERE "+COL1+"='"+id+"'");
	}
}
