package com.example.dog.a262database;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class RecipeListAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<Recipe> recipes;

    public RecipeListAdapter(Context context, ArrayList<Recipe>recipes)
    {
        this.context = context;
        this.recipes = recipes;
    }

    @Override
    public int getCount()
    {
        return recipes.size();
    }

    @Override
    public Object getItem(int position)
    {
        return recipes.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        RecipeLayout recipeLayout = null;
        Recipe recipe = recipes.get(position);

        if (convertView == null)
        {
            recipeLayout = new RecipeLayout(context, recipe);
        }
        else
        {
            recipeLayout = (RecipeLayout) convertView;
            recipeLayout.setRecipe(recipe);
        }
        return recipeLayout;
    }
}
