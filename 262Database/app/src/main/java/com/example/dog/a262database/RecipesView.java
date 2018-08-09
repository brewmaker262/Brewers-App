package com.example.dog.a262database;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Logger;

public class
RecipesView extends Activity
{
    private TextView nameTextView;
    private TextView recipeTextView;
    private TextView processTextView;
    private TextView notesTextView;
    private Recipe current;
    private int position;
   // private Button deleteButton;
    private RecipeDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_view);
        db = new RecipeDB(this);
        ArrayList<Recipe> recipes = db.getRecipe();

        Bundle extras = getIntent().getExtras();
        position = extras.getInt("_id");

        current = recipes.get(position);


        //get widget refs
        nameTextView = (TextView) findViewById(R.id.nameTextView);
        recipeTextView = (TextView) findViewById(R.id.recipeTextView);
        processTextView = (TextView) findViewById(R.id.processTextView);
        notesTextView = (TextView) findViewById(R.id.notesTextView);
        //deleteButton = (Button) findViewById(R.id.deleteButton);

        //set the text view
        nameTextView.setText(current.getRecipeName());
        recipeTextView.setText(current.getRecipe());
        processTextView.setText(current.getProcess());
        notesTextView.setText(current.getNotes());

        //adding a back button to the action bar
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    // menu bar selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            case R.id.deleteMenuItem:
                // deletes current recipe from the db
                db.deleteRecipe(current.getId());
                startActivity(new Intent(this, ViewActivity.class));
                Toast.makeText(getBaseContext(),"Recipe deleted", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.updateRecipe:
                //launches the update recipe activity
                Intent updateActivity = new Intent(this, UpdateActivity.class);
                updateActivity.putExtra("position", position);
                startActivity(updateActivity);
                Toast.makeText(getBaseContext(),"this is at position " + position,
                        Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //options menu for update and delete
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menubuttons, menu);
        return true;
    }


    public void launchConversion(View view) {
        // This method is tied to the "Convert" button
        // TODO Add code to launch conversion activity here
    }

}
