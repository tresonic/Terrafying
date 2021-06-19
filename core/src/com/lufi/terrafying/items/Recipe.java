package com.lufi.terrafying.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

public class Recipe {
	enum RecipeType {
		NORMAL,
		CRAFTING,
		OVEN,
		
	}
	
	private int id;
	
	private ArrayList<ItemStack> ingredients;
	private ItemStack result;
	private RecipeType type;
	
	private static HashMap<Integer, Recipe> recipeMap = new HashMap<Integer, Recipe>();
	
	public Recipe(int nId, RecipeType nType, ArrayList<ItemStack> nIngredients, ItemStack nResult) {
		ingredients = nIngredients;
		result = nResult;
		id = nId;
		type = nType;
	}
	
	public ArrayList<ItemStack> getIngredients() {
		return ingredients;
	}
	
	public ItemStack getResult() {
		return result;
	}
	
	
	
	
	public static void registerRecipes() {
		int c = 0;
		registerRecipe(c++, RecipeType.NORMAL, new ArrayList<ItemStack>(Arrays.asList(new ItemStack(Item.getItemByName("stone"), 3))), new ItemStack(Item.getItemByName("uranium"), 1));
		registerRecipe(c++, RecipeType.NORMAL, new ArrayList<ItemStack>(Arrays.asList(new ItemStack(Item.getItemByName("stone"), 1))), new ItemStack(Item.getItemByName("copper"), 1));
		registerRecipe(c++, RecipeType.NORMAL, new ArrayList<ItemStack>(Arrays.asList(new ItemStack(Item.getItemByName("oakwood"), 1))), new ItemStack(Item.getItemByName("oakwoodplanks"), 4));
		//Items Stone
		registerRecipe(c++, RecipeType.NORMAL, new ArrayList<ItemStack>(Arrays.asList(new ItemStack(Item.getItemByName("stone"), 2), new ItemStack(Item.getItemByName("oakwoodplanks"), 2))), new ItemStack(Item.getItemByName("stonesword"), 1));
		registerRecipe(c++, RecipeType.NORMAL, new ArrayList<ItemStack>(Arrays.asList(new ItemStack(Item.getItemByName("stone"), 2), new ItemStack(Item.getItemByName("oakwoodplanks"), 2))), new ItemStack(Item.getItemByName("stonehoe"), 1));
		registerRecipe(c++, RecipeType.NORMAL, new ArrayList<ItemStack>(Arrays.asList(new ItemStack(Item.getItemByName("stone"), 3), new ItemStack(Item.getItemByName("oakwoodplanks"), 2))), new ItemStack(Item.getItemByName("stonepickaxe"), 1));
		registerRecipe(c++, RecipeType.NORMAL, new ArrayList<ItemStack>(Arrays.asList(new ItemStack(Item.getItemByName("stone"), 3), new ItemStack(Item.getItemByName("oakwoodplanks"), 2))), new ItemStack(Item.getItemByName("stoneaxe"), 1));
		//Items Iron	
		registerRecipe(c++, RecipeType.CRAFTING, new ArrayList<ItemStack>(Arrays.asList(new ItemStack(Item.getItemByName("iron"), 2), new ItemStack(Item.getItemByName("oakwoodplanks"), 2))), new ItemStack(Item.getItemByName("ironsword"), 1));
		registerRecipe(c++, RecipeType.CRAFTING, new ArrayList<ItemStack>(Arrays.asList(new ItemStack(Item.getItemByName("iron"), 2), new ItemStack(Item.getItemByName("oakwoodplanks"), 2))), new ItemStack(Item.getItemByName("ironhoe"), 1));
		registerRecipe(c++, RecipeType.CRAFTING, new ArrayList<ItemStack>(Arrays.asList(new ItemStack(Item.getItemByName("iron"), 3), new ItemStack(Item.getItemByName("oakwoodplanks"), 2))), new ItemStack(Item.getItemByName("ironpickaxe"), 1));
		registerRecipe(c++, RecipeType.CRAFTING, new ArrayList<ItemStack>(Arrays.asList(new ItemStack(Item.getItemByName("iron"), 3), new ItemStack(Item.getItemByName("oakwoodplanks"), 2))), new ItemStack(Item.getItemByName("ironaxe"), 1));
		//Items Diamond
		registerRecipe(c++, RecipeType.CRAFTING, new ArrayList<ItemStack>(Arrays.asList(new ItemStack(Item.getItemByName("diamond"), 2), new ItemStack(Item.getItemByName("oakwoodplanks"), 2))), new ItemStack(Item.getItemByName("diamondsword"), 1));
		registerRecipe(c++, RecipeType.CRAFTING, new ArrayList<ItemStack>(Arrays.asList(new ItemStack(Item.getItemByName("diamond"), 2), new ItemStack(Item.getItemByName("oakwoodplanks"), 2))), new ItemStack(Item.getItemByName("diamondhoe"), 1));
		registerRecipe(c++, RecipeType.CRAFTING, new ArrayList<ItemStack>(Arrays.asList(new ItemStack(Item.getItemByName("diamond"), 3), new ItemStack(Item.getItemByName("oakwoodplanks"), 2))), new ItemStack(Item.getItemByName("diamondpickaxe"), 1));
		registerRecipe(c++, RecipeType.CRAFTING, new ArrayList<ItemStack>(Arrays.asList(new ItemStack(Item.getItemByName("diamond"), 3), new ItemStack(Item.getItemByName("oakwoodplanks"), 2))), new ItemStack(Item.getItemByName("diamondaxe"), 1));
		//other Items
		registerRecipe(c++, RecipeType.NORMAL, new ArrayList<ItemStack>(Arrays.asList(new ItemStack(Item.getItemByName("wool"), 1), new ItemStack(Item.getItemByName("oakwoodplanks"), 2))), new ItemStack(Item.getItemByName("brush"), 1));
		registerRecipe(c++, RecipeType.NORMAL, new ArrayList<ItemStack>(Arrays.asList(new ItemStack(Item.getItemByName("iron"), 20), new ItemStack(Item.getItemByName("coal"), 50))), new ItemStack(Item.getItemByName("jetpack"), 1));
	}
	
	public static void registerRecipe(int nId, RecipeType nType, ArrayList<ItemStack> nIngredients, ItemStack nResult) {
		recipeMap.put(nId, new Recipe(nId, nType, nIngredients, nResult));
	}
	
	public static Recipe getRecipeByName(String name) {
		for(Recipe r : recipeMap.values()) {
			if(name.equals(r.result.getName())) {
				return r;
			}
		}
		return null;
	}
	
	public static Recipe getRecipe(Item item) {
		for(Recipe r : recipeMap.values()) {
			if(item.getId() == r.result.getId()) {
				return r;
			}
		}
		return null;
	}
	
	public static int getRecipeCraftableCount(Inventory inv, Recipe recipe) {
		int minCount = ItemStack.STACK_MAX;
		for(ItemStack stack : recipe.ingredients) {
			int itemCount = inv.getItemCount(stack.item);
			if(itemCount >= stack.count) {
				minCount = Math.min(minCount, itemCount / stack.count);
			}
			else {
				minCount = 0;
			}
		}
		return minCount;
	}
	
	public static ArrayList<ItemStack> getCraftableItemStacks(Inventory inv) {
		ArrayList<ItemStack> craftableStacks = new ArrayList<ItemStack>();
		for(Recipe r : recipeMap.values()) {
			int craftableCount = getRecipeCraftableCount(inv, r);
			if(craftableCount > 0) {
				craftableStacks.add(new ItemStack(Item.getItemById(r.getResult().getId()), craftableCount));
			}
		}
		return craftableStacks;
	}
	
	public static void doCraft(Inventory inv, Item result) {
		Recipe recipe = getRecipe(result);
		if(getRecipeCraftableCount(inv, recipe) > 0) {
			for(ItemStack stack : recipe.ingredients) {
				inv.removeItem(stack);
			}
			inv.addItem(recipe.result.clone());
		}		
	}
}
