package control;

import java.util.LinkedList;
import java.util.TreeMap;

import recipes.Recipe;

public class Craftables {

	static final int REND_X = 4;
	static final int REND_Y = 4;
	static final int MAX_TYPES = REND_X * REND_Y;
	public static LinkedList<Recipe> recipes;
	{
		// Initializes recipes
		recipes = new LinkedList<>();
		recipes.add(new recipes.Accelerator());
		recipes.add(new recipes.Empowerer());
		recipes.add(new recipes.UpgradeCore());
		recipes.add(new recipes.IntegrityUpgrade());
		recipes.add(new recipes.EnergyUpgrade());
		recipes.add(new recipes.InventoryUpgrade());
		recipes.add(new recipes.Portal());
	}

	TreeMap<String, Recipe> content;

	public Craftables() {
		content = new TreeMap<>();
	}

	public Recipe getRecipe(String name) {
		return content.get(name);
	}

	public Recipe[] getRecipes() {
		Recipe[] out = new Recipe[content.size()];
		content.values().toArray(out);
		return out;
	}

	public boolean hasRecipe(String name) {
		return content.containsKey(name);
	}

	public void addRecipe(Recipe Recipe) {
		content.put(Recipe.getName(), Recipe);
	}
}
