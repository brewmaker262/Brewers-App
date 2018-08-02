package com.example.dog.a262database;

import android.app.ActionBar;
import android.app.Activity;
import android.view.MenuItem;
import android.widget.EditText;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;



public class ViewActivity extends Activity
{
    private ListView recipeListView;
    private RecipeDB db;
    private TextView recipeNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        recipeNameTextView = (TextView) findViewById(R.id.recipeNameTextView);
        recipeListView = (ListView) findViewById(R.id.recipeListView);
        db = new RecipeDB(this);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        ArrayList<Recipe> recipes = db.getRecipe();
        RecipeListAdapter adapter = new RecipeListAdapter(this, recipes);
        final String t=  Integer.toString(adapter.getCount());
        //adapter.getItem();
        recipeListView.setAdapter(adapter);
        recipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
//                Toast.makeText(getBaseContext(),"this is the position " + id,
//                        Toast.LENGTH_SHORT).show();

                Intent recipesView = new Intent(ViewActivity.this, RecipesView.class);
                recipesView.putExtra("_id",position);
                startActivity(recipesView);


            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
