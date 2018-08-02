package com.example.dog.a262database;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RecipeLayout extends RelativeLayout
{
    private TextView recipeNameTextView;
    private TextView abvTextView;
    private Context context;
    private RecipeDB db;
    private Recipe recipe;

    public RecipeLayout(Context context) {super(context);}
    public RecipeLayout(final Context context, final Recipe recipe)
    {
        super(context);
        db = new RecipeDB(context);

        //inflate the layout
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.listview_recipe, this, true);

        //get widget refs
        recipeNameTextView = (TextView) findViewById(R.id.recipeNameTextView);

        //set task data on the widget
        setRecipe(recipe);
        recipeNameTextView.setText(recipe.getRecipeName());
    }

    public void setRecipe(Recipe recipe)
    {
        this.recipe = recipe;
    }


}
