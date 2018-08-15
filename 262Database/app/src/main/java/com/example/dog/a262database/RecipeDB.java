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
            String execStr, name, recipe, process, notes = "";

            name = "Moms Special Ale";

            recipe = "6 lbs. English Light syrup malt extract\n" +
                    "2 lbs. English Light dry mail extract\n" +
                    "2.2 lbs. Morgans Master Blend Caramalt syrup malt extract\n" +
                    "1 lb. 80 L. crystal malt\n"+
                    ".75 cup corn sugar (priming)\n";

            process = "Place 80 L. crystal malt in straining bag and suspend in 3 gallons cold water, bring to boil. "+
                    "Once water comes to boil, remove spent crystal malt grains. Add all syrup and dry malt extracts."+
                    " Boil for 30 minutes, then stir in Irish moss. Boil for an additional 25 minutes, then remove pot from flame. Cool until 100 degrees F.\n";

            notes = "This beer came out really good after only 2 weeks in the bottle. Balanced pretty nicely.\n";

            execStr = "INSERT INTO recipes VALUES (1, '" + name + "', '" + recipe + "', '" + process + "', '" + notes + "')";
            db.execSQL(execStr);

            name = "Rainy Day Porter";

            recipe = "2 pounds, Alexander extract syrup (pale)\n" +
                    "4 pounds, Yellow Dog extract syrup (amber)\n" +
                    "1-1/4 pounds, Brown Sugar\n" +
                    "1/2 pound, Black Patent\n"+
                    "1/4 pound, Roasted Barley\n";

            process = "Steep grains at 150 degrees for 40 minutes before boil. Add malt and brown sugar. "+
                    "Boil for 60 minutes. Add Nugget hops at begining of boil. Add ginger last 10 "+
                    "minutes of boil. Turn off heat add water to make total about 5.3 gallons.\n";

            notes = "I used two types of yeast pitched simultaneously for this brew.\n";

            execStr = "INSERT INTO recipes VALUES (2, '" + name + "', '" + recipe + "', '" + process + "', '" + notes + "')";
            db.execSQL(execStr);

            name = "Oatmeal Stout 9";

            recipe = "6.5 lbs light malt Extract\n" +
                    "1.5 lbs American 2-row malt\n" +
                    "1 lb. flaked oats\n" +
                    "3/4 lb. roasted barley\n"+
                    "1/4 lb. chocolate malt\n";

            process = "Steep-Mash process: Steep grains in grain bag in 4-5 gallons water, for 45 minutes at 158-159F. "+
                    "Remove grain bag and rinse grains with water at same temperature, until about 5.5-6.0 gallons in brewpot. " +
                    "Add malt extract. Bring to boil, add hops and boil for 60 minutes.\n";

            notes = "Heres a recipie that was created by the guys down at the brewshop when I walked in and said: I want to brew an Oatmeal Stout, make me a recipie.\n";

            execStr = "INSERT INTO recipes VALUES (3, '" + name + "', '" + recipe + "', '" + process + "', '" + notes + "')";
            db.execSQL(execStr);

            name = "Apricot Ale";

            recipe = "4-1/2 pounds light dry malt extract\n" +
                    "1 pound, German pilsner malt (steeped at 150 F for 1 hour)\n" +
                    "1/4 teaspoon, Irish moss\n" +
                    "1 ounce, Chinook hops (12.2% alpha)\n"+
                    "2 1/2 pounds, frozen, pitted, halved apricots";

            process = "Steep pilsner malt at 150 degrees for 1 hour. Strain and sparge grain. Add malt extract. "+
                    "Bring to boil and boil for 60 minutes. Add 1 ounce Chinook hops at 30 minutes. "+
                    "Add Mt. Hood in the last 2 minutes. The apricots were added at the end of the boil.\n";

            notes = "Nice golden amber color with a good hop bite. About half way through a mug, I start noticing the taste of cloves.\n";

            execStr = "INSERT INTO recipes VALUES (4, '" + name + "', '" + recipe + "', '" + process + "', '" + notes + "')";
            db.execSQL(execStr);


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
