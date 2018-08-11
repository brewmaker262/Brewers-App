package com.example.dog.a262database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecipeDB
{
    //database constants
    public static final String DB_NAME = "recipes.db";
    public static final int    DB_VERSION = 1;
    public static final String TABLE_RECIPES = "recipes";

    public static final String RECIPE_ID = "_id";
    public static final int COLUMN_RECIPE_ID = 0;

    public static final String RECIPE_NAME = "recipename";
    public static final int COLUMN_RECIPE_NAME = 1;

    public static final String RECIPE = "recipe";
    public static final int COLUMN_RECIPE = 2;

    public static final String PROCESS = "process";
    public static final int COLUMN_PROCESS = 3;

    public static final String NOTES = "notes";
    public static final int COLUMN_NOTES = 4;

    public static final String CREATE_RECIPES_TABLE =
            "CREATE TABLE " + TABLE_RECIPES + " (" +
                    RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    RECIPE_NAME + " TEXT," +
                    RECIPE + " TEXT," +
                    PROCESS + " TEXT," +
                    NOTES + " TEXT" + ")";


    public static final String DROP_RECIPE_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_RECIPES;


    //DBHelper
    private static class DBHelper extends SQLiteOpenHelper
    {
        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)

        {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            // create tables
            db.execSQL(CREATE_RECIPES_TABLE);

            // creates test recipe
            db.execSQL("INSERT INTO recipes VALUES (1, 'Best Beer Eva', '2 Barley, 4 Hops, 1 fairy dust', 'Mix well, and brew', 'Here are important notes')");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.d("Brewers Tool", "Upgrading db from version "
                    + oldVersion + " to " + newVersion);

            db.execSQL(RecipeDB.DROP_RECIPE_TABLE);
            onCreate(db);
        }
    }
    //End DBHelper class

    // database and database helper objects
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    // constructor
    public RecipeDB(Context context)
    {
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
    }

    // private methods
    private void openReadableDB() {
        db = dbHelper.getReadableDatabase();
    }

    private void openWriteableDB() {
        db = dbHelper.getWritableDatabase();
    }

    private void closeDB()
    {
        if (db != null)
            db.close();
    }

    // public methods
    public ArrayList<Recipe> getRecipe()
    {
        this.openReadableDB();
        Cursor cursor = db.query(TABLE_RECIPES,
                null, null, null, null, null, null);
        ArrayList<Recipe> recipes = new ArrayList<>();
        while (cursor.moveToNext())
        {
            Recipe recipe = new Recipe(
                    cursor.getLong(COLUMN_RECIPE_ID),
                    cursor.getString(COLUMN_RECIPE_NAME),
                    cursor.getString(COLUMN_RECIPE),
                    cursor.getString(COLUMN_PROCESS),
                    cursor.getString(COLUMN_NOTES));

            recipes.add(recipe);
        }
        if (cursor != null) {
            cursor.close();
        }
        this.closeDB();

        return recipes;
    }

    //pre all values will be empty strings if not defined
    //post inserts a new recipe into the db
    public long insertRecipe(Recipe recipe)
    {
        ContentValues cv = new ContentValues();
        cv.put(RECIPE_NAME, recipe.getRecipeName());
        cv.put(RECIPE, recipe.getRecipe());
        cv.put(PROCESS, recipe.getProcess());
        cv.put(NOTES, recipe.getNotes());
        this.openWriteableDB();
        long rowID = db.insert(TABLE_RECIPES, null, cv);
        this.closeDB();

        return rowID;
    }

    //post deletes the recipe from the db
    public long deleteRecipe(long id)
    {
        String where = RECIPE_ID + "= ?";
        String[] whereArgs = { String.valueOf(id) };
        this.openWriteableDB();
        int rowCount = db.delete(TABLE_RECIPES, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    //post updates the current recipe, if values are not defined they will be empty strings
    public boolean updateRecipe(long id, String recipeName, String recipe,String process, String notes)
    {
        ContentValues cv = new ContentValues();
        cv.put(RECIPE_NAME, recipeName);
        cv.put(RECIPE, recipe);
        cv.put(PROCESS, process);
        cv.put(NOTES, notes);
        this.openWriteableDB();
        db.update(TABLE_RECIPES, cv, "_id = ?", new String[]{String.valueOf(id)});
        db.close();
        return true;
    }



}
