package com.example.dog.a262database;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class UpdateActivity extends Activity implements View.OnClickListener
{
    private EditText updateName;
    private EditText updateRecipe;
    private EditText updateProcess;
    private EditText updateNotes;
    private String recipeNameString = "";
    private String recipeString = "";
    private String processString = "";
    private String notesString = "";
    private Button updateButton;
    private Recipe current;
    private RecipeDB db;
    private int position;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        //get recipe by id number from db
        db = new RecipeDB(this);
        ArrayList<Recipe> recipes = db.getRecipe();
        Bundle extras = getIntent().getExtras();
        position = extras.getInt("position");
        current = recipes.get(position);



        Toast.makeText(getBaseContext(),"this is the position " + position,
                        Toast.LENGTH_SHORT).show();

        //get widget refs
        updateName = (EditText) findViewById(R.id.updateRecipeName);
        updateRecipe = (EditText) findViewById(R.id.updateRecipe);
        updateProcess = (EditText) findViewById(R.id.updateProcess);
        updateNotes = (EditText) findViewById(R.id.updateNotes);
        updateButton = (Button) findViewById(R.id.updateButton);

        //set widget text
        updateName.setText(current.getRecipeName());
        updateRecipe.setText(current.getRecipe());
        updateProcess.setText(current.getProcess());
        updateNotes.setText(current.getNotes());

        //update button on click
        updateButton.setOnClickListener(this);

        //adding a back button to the action bar
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onClick(View v)
    {
        recipeNameString = updateName.getText().toString();
        recipeString = updateRecipe.getText().toString();
        processString = updateProcess.getText().toString();
        notesString = updateNotes.getText().toString();
        if (recipeNameString.equals("")) recipeNameString = "Generic";
        // create and insert recipe
        current.setRecipeName(recipeNameString);
        current.setRecipe(recipeString);
        current.setProcess(processString);
        current.setNotes(notesString);
        db.updateRecipe(current.getId(), current.getRecipeName(), current.getRecipe(), current.getProcess(), current.getNotes());
        startActivity(new Intent(this, ViewActivity.class));
        Toast.makeText(getBaseContext(),"Recipe updated", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
