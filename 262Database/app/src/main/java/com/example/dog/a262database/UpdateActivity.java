package com.example.dog.a262database;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Logger;

public class UpdateActivity extends Activity implements ThermodoListener
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

    //Thermodo variables
    private Thermodo thermodoUpdate;
    private TextView temperatureUpdateTextView;
    private float myTemperatureUpdate;
    private static Logger sLog = Logger.getLogger(UpdateActivity.class.getName());


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

        // create thermodoUpdate object and set listener
        thermodoUpdate = ThermodoFactory.getThermodoInstance(this);
        thermodoUpdate.setThermodoListener(this);

        Toast.makeText(getBaseContext(),"this is the position " + position,
                        Toast.LENGTH_SHORT).show();

        //get widget refs
        updateName = (EditText) findViewById(R.id.updateRecipeName);
        updateRecipe = (EditText) findViewById(R.id.updateRecipe);
        updateProcess = (EditText) findViewById(R.id.updateProcess);
        updateNotes = (EditText) findViewById(R.id.updateNotes);
        updateButton = (Button) findViewById(R.id.updateButton);
        temperatureUpdateTextView = (TextView) findViewById(R.id.temperatureUpdateTextView);

        //set widget text
        updateName.setText(current.getRecipeName());
        updateRecipe.setText(current.getRecipe());
        updateProcess.setText(current.getProcess());
        updateNotes.setText(current.getNotes());

        //adding a back button to the action bar
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    public void onUpdate(View v)
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

    public void launchConversion(View view) {
        // This method is tied to the "Convert" button
        // TODO Add code to launch conversion activity here
    }

    public void logTemperature(View view) {
        String notes = updateNotes.getText().toString();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

        notes = notes +  String.format("%s: %,.2f%n", timeStamp, myTemperatureUpdate);

        updateNotes.setText(notes);
    }

    public float celsiousToFahrenheit(float celsius) {
        return (celsius * (9/5)) + 32;
    }

    @Override
    public void onStartedMeasuring() {
        sLog.info("Started measuring");
    }

    @Override
    public void onStoppedMeasuring() {
        temperatureUpdateTextView.setText(getString(R.string.thermodo_unplugged));
        sLog.info("Stopped measuring");
    }

    @Override
    public void onTemperatureMeasured(float temperature) {
        temperatureUpdateTextView.setText(Float.toString(celsiousToFahrenheit(temperature)));
        myTemperatureUpdate = celsiousToFahrenheit(temperature);
        sLog.fine("Got temperature: " + celsiousToFahrenheit(temperature));
    }

    @Override
    public void onErrorOccurred(int what) {
        Toast.makeText(this, "An error has occurred: " + what, Toast.LENGTH_SHORT).show();
        switch (what) {
            case Thermodo.ERROR_AUDIO_FOCUS_GAIN_FAILED:
                sLog.severe("An error has occurred: Audio Focus Gain Failed");
                temperatureUpdateTextView.setText(getString(R.string.thermodo_unplugged));
                break;
            case Thermodo.ERROR_AUDIO_RECORD_FAILURE:
                sLog.severe("An error has occurred: Audio Record Failure");
                break;
            case Thermodo.ERROR_SET_MAX_VOLUME_FAILED:
                sLog.warning("An error has occurred: The volume could not be set to maximum");
                break;
            default:
                sLog.severe("An unidentified error has occurred: " + what);
        }
    }

    @Override
    public void onPermissionsMissing() {
        Log.d("DC", "permissions are missing");
    }

    @Override
    public void onThermodoPlugged(Boolean isPlugged) {
        if (isPlugged) {
            temperatureUpdateTextView.setText(getString(R.string.thermodo_plugged));
        } else {
            temperatureUpdateTextView.setText(getString(R.string.thermodo_unplugged));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        thermodoUpdate.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        thermodoUpdate.stop();
    }

}
