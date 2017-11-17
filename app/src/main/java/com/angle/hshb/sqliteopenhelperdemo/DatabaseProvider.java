package com.angle.hshb.sqliteopenhelperdemo;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.angle.hshb.sqliteopenhelperdemo.database.MyDataBaseHelper;

public class DatabaseProvider extends ContentProvider {

    public static final int BOOK_DIR = 0;

    public static final int BOOK_ITEM = 1;

    public static final int CATEGORY_DIR = 2;

    public static final int CATEGORY_ITEM = 3;

    public static final String AUTHORITY = "com.angle.hshb.sqliteopenhelperdemo.provider";

    private static UriMatcher uriMatcher;

    private MyDataBaseHelper dbHelper;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY,"book",BOOK_DIR);
        uriMatcher.addURI(AUTHORITY,"book/#",BOOK_ITEM);
        uriMatcher.addURI(AUTHORITY,"category",CATEGORY_DIR);
        uriMatcher.addURI(AUTHORITY,"category/#",CATEGORY_ITEM);
    }

    /**
     * 初始化内容提供器的时候调用
     * 通常在这里完成对数据库的创建和升级操作
     * 返回True表示内容提供者初始化成功
     * False表示失败
     * 注：只有当存在ContentResolver尝试访问我们程序中的数据时，内容提供器才会被初始化
     * @return
     */
    @Override
    public boolean onCreate() {
        dbHelper = new MyDataBaseHelper(getContext(),"BookStore.db",null,3);
        return true;
    }

    /**
     * 从内容提供器中查询数据
     * @param uri  确定查询哪张表
     * @param projection  确定查询哪些列
     * @param selection   用于约束查询哪些行
     * @param selectionArgs  用于约束查询哪些行
     * @param sortOrder  对结果进行排序
     * @return  Cursor对象
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        //查询数据
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                //查询BOOK表中所有数据
                cursor = db.query("Book",projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case BOOK_ITEM:
                //查询BOOK表中单条数据
                String bookId = uri.getPathSegments().get(1);
                cursor = db.query("Book",projection,"id=?",new String[]{bookId},null,null,sortOrder);
                break;
            case CATEGORY_DIR:
                //查询CATEGORY表中所有数据
                cursor = db.query("Category",projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case CATEGORY_ITEM:
                //查询CATEGORY表中单条数据
                String categoryId = uri.getPathSegments().get(1);
                cursor = db.query("Category",projection,"id=?",new String[]{categoryId},null,null,sortOrder);
                break;
            default:
                break;
        }
        return cursor;
    }

    /**
     * 根据传入的URI来返回相应的MIME类型
     * @param uri
     * @return
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                return "vnd.android.cursor.dir/vnd."+AUTHORITY+".book";
            case BOOK_ITEM:
                return "vnd.android.cursor.item/vnd."+AUTHORITY+".book";
            case CATEGORY_DIR:
                return "vnd.android.cursor.dir/vnd."+AUTHORITY+".category";
            case CATEGORY_ITEM:
                return "vnd.android.cursor.item/vnd."+AUTHORITY+".category";
            default:
                break;
        }
        return null;
    }

    /**
     * 向内容提供器中添加数据
     * @param uri  确定添加到的表
     * @param values  待添加的数据保存在values中
     * @return  返回一个用于表示这条新纪录的URL
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        //添加数据
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri uriReturn = null;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
            case BOOK_ITEM:
                long newBookId = db.insert("Book",null,values);
                uriReturn = Uri.parse("content://"+AUTHORITY+"/book/"+newBookId);
                break;
            case CATEGORY_DIR:
            case CATEGORY_ITEM:
                long newCategoryId = db.insert("Book",null,values);
                uriReturn = Uri.parse("content://"+AUTHORITY+"/category/"+newCategoryId);
                break;
            default:
                break;
        }
        return uriReturn;
    }

    /**
     * 从内容提供器中删除数据
     * @param uri  确定删除哪一张表中的数据
     * @param selection   约束删除哪些行
     * @param selectionArgs   约束删除哪些行
     * @return  返回被删除的行数
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        //删除数据
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deleteRows =0;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                deleteRows = db.delete("Book",selection,selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                deleteRows = db.delete("Book","id=?",new String[]{bookId});
                break;
            case CATEGORY_DIR:
                deleteRows = db.delete("Category",selection,selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                deleteRows = db.delete("Category","id=?",new String[]{categoryId});
                break;
            default:
                break;
        }
        return deleteRows;
    }

    /**
     * 更新内容提供者中已有的数据
     * @param uri  确定更新哪张表的数据
     * @param values  新数据保存在Values
     * @param selection   约束更新哪些行
     * @param selectionArgs  约束更新哪些行
     * @return   返回受影响的行数
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        //更新数据
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int updateRows =0;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                updateRows = db.update("Book",values,selection,selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                updateRows = db.update("Book",values,"id=?",new String[]{bookId});
                break;
            case CATEGORY_DIR:
                updateRows = db.update("Category",values,selection,selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                updateRows = db.update("Category",values,"id=?",new String[]{categoryId});
                break;
            default:
                break;
        }
        return updateRows;
    }
}
