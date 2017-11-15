package com.angle.hshb.sqliteopenhelperdemo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.angle.hshb.sqliteopenhelperdemo.database.MyDataBaseHelper;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private MyDataBaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new MyDataBaseHelper(this,"BookStore.db",null,3);
    }

    /**
     * 创建数据库
     * @param view
     */
    public void createDataBase(View view) {
        dbHelper.getWritableDatabase();
    }

    /**
     * 添加数据
     * @param view
     */
    public void addData(View view) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        //第一条数据
//        values.put("name","The Da Vinci Code");
//        values.put("author","Dan Brown");
//        values.put("pages",454);
//        values.put("price",16.96);
//        db.insert("Book",null,values);
//        values.clear();
//        //第二条数据
//        values.put("name","The Lost Symbol");
//        values.put("author","Dan Brown");
//        values.put("pages",510);
//        values.put("price",19.95);
//        db.insert("Book",null,values);
        db.execSQL("insert into Book(name,author,pages,price) values(?,?,?,?)",
                new String[]{"The Da Vinci Code","Dan Brown","454","16.96"});
        db.execSQL("insert into Book(name,author,pages,price) values(?,?,?,?)",
                new String[]{"The Lost Symbol","Dan Brown","540","19.95"});
    }

    /**
     * 修改数据
     * @param view
     */
    public void updateData(View view) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("price",10.99);
//        db.update("Book",values,"name = ?",new String[]{"The Da Vinci Code"});
        db.execSQL("update Book set price = ? where name = ?",
                new String[]{"10.99","The Da Vinci Code"});
    }

    /**
     * 删除数据
     * @param view
     */
    public void deleteData(View view) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
//     db.delete("Book","pages > ?",new String[]{"500"});
        db.execSQL("delete from Book where pages > ?",
                new String[]{"500"});
    }

    /**
     * 查询数据
     * @param view
     */
    public void queryData(View view) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        Cursor cursor = db.query("Book",null,null,null,null,null,null);
        Cursor cursor = db.rawQuery("select * from Book",null);
        if (cursor.moveToFirst()){
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String author = cursor.getString(cursor.getColumnIndex("author"));
                int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                double price = cursor.getDouble(cursor.getColumnIndex("price"));
                Log.i(TAG,"book name is "+name);
                Log.i(TAG,"book author is "+author);
                Log.i(TAG,"book pages is "+pages);
                Log.i(TAG,"book price is "+price);
            }while (cursor.moveToNext());
        }
        cursor.close();

    }
}
