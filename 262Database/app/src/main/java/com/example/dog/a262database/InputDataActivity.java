package com.example.dog.a262database;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class InputDataActivity extends Activity implements TextView.OnEditorActionListener, View.OnClickListener
{
    //variables for the widgets
    private EditText recipeNameEditText;
    private EditText recipeEditText;
    private EditText processEditText;
    private EditText recipeNotesEditText;
    private Button addButton;
    private Button viewButton;
    private String recipeNameString = "";
    private String recipeString = "";
    private String processString = "";
    private String recipeNotesString = "";


    // set up preferences
    private SharedPreferences prefs;

    // define db instance variable
    RecipeDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data);

        //get widget refs
        recipeNameEditText = (EditText) findViewById(R.id.recipeNameEditText);
        recipeEditText = (EditText) findViewById(R.id.recipeEditText);
        processEditText = (EditText) findViewById(R.id.processEditText);
        recipeNotesEditText = (EditText) findViewById(R.id.recipeNotesEditText);
        addButton = (Button) findViewById(R.id.addButton);
        viewButton = (Button) findViewById(R.id.viewButton);

        // get default SharedPreferences object
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        recipeNameEditText.setOnEditorActionListener(this);
        addButton.setOnClickListener(this);
        viewButton.setOnClickListener(this);
        db = new RecipeDB(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.addButton:
                //add recipe to db
                recipeNameString = recipeNameEditText.getText().toString();
                recipeString = recipeEditText.getText().toString();
                processString = processEditText.getText().toString();
                recipeNotesString = recipeNotesEditText.getText().toString();
                if (recipeNameString.equals("")) recipeNameString = "Generic";
                // create and insert recipe
                Recipe recipe = new Recipe();
                recipe.setRecipeName(recipeNameString);
                recipe.setRecipe(recipeString);
                recipe.setProcess(processString);
                recipe.setNotes(recipeNotesString);
                db.insertRecipe(recipe);
                // update UI
                recipeNameEditText.setText("");
                recipeEditText.setText("");
                processEditText.setText("");
                recipeNotesEditText.setText("");
                Toast.makeText(getBaseContext(),"Recipe Added", Toast.LENGTH_SHORT).show();
                break;
            case R.id.viewButton:
                //view of all recipes in db
                startActivity(new Intent(this, ViewActivity.class));
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
    {
        if (actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_UNSPECIFIED)
        {
            //calculateAndDisplay();
        }
        return false;
    }
}
