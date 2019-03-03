package ie.holiday.inked;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    //login and register classes
    private static final int Database_Version = 1;
    private static final String Database_Name = "contacts.db";
    private static final String Table_Name = "contacts";
    private static final String Colum_ID = "id";
    private static final String Colum_Name = "name";
    private static final String Colum_Email = "email";
    private static final String Colum_Password = "pass";







    SQLiteDatabase db;

    private static final String Table_Create = "create table contacts (id integer primary key not null, name text not null ,email text not null ,  pass text not null)";




    public DatabaseHelper(Context context) {

        super(context, Database_Name, null, Database_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Table_Create);

        this.db = db;

    }

    public void insertContact(Contact c) {
        db = this.getWritableDatabase();

        //create content values

        ContentValues values = new ContentValues();

        String query = "select * from contacts";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();


        values.put(Colum_ID, count);
        values.put(Colum_Name, c.getName());
        values.put(Colum_Email, c.getEmail());
        values.put(Colum_Password, c.getPass());

        db.insert(Table_Name, null, values);
        db.close();
    }

    public String searchPass(String email) {
        db = this.getReadableDatabase();
        String query = "select email, pass from " + Table_Name;
        Cursor cursor = db.rawQuery(query, null);
        String a, b;
        b = "not found";


        if (cursor.moveToFirst()) {
            do {
                a = cursor.getString(0);


                if (a.equals(email)) {
                    b = cursor.getString(1);
                    break;
                }
            }
            while (cursor.moveToNext());
        }

        return b;
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String query = "DROP TABLE IF EXISTS " + Table_Name;
        db.execSQL(query);
        this.onCreate(db);

        onCreate(db);

    }







}



