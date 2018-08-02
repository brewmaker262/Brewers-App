package com.example.dog.a262database;

public class Recipe
{

    //fields
    private long id;
    private String recipeName;
    private String recipe;
    private String process;
    private String notes;

    //constructors
    public Recipe()
    {
        setId(0);
        setRecipeName("test");

    }

    public Recipe(long id, String recipeName, String recipe, String process, String notes)
    {
        this.setId(id);
        this.setRecipeName(recipeName);
        this.setRecipe(recipe);
        this.setProcess(process);
        this.setNotes(notes);
    }


    //getters and setters
    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getRecipeName()
    {
        return recipeName;
    }

    public void setRecipeName(String recipeName)
    {
        this.recipeName = recipeName;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getNotes() { return notes; }

    public void setNotes(String notes) {this.notes = notes; }
}

