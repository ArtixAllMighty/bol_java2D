package game.item.crafting;

import game.entity.living.player.Player;
import game.item.Item;
import game.item.ItemStack;
import game.item.Items;
import game.util.Util;

import java.util.ArrayList;

public class Crafting {

	private static ArrayList<ItemStack[]> recipyList = new ArrayList<ItemStack[]>();
	private static ArrayList<ItemStack> resultList = new ArrayList<ItemStack>();

	public static void addRecipe(ItemStack[] stack, ItemStack result){
		recipyList.add(stack);
		resultList.add(result);
	}

	public static ItemStack[] recipe(int i){

		if(i >= recipyList.size()){
			return null;
		}

		return recipyList.get(i);
	}

	public static ItemStack result(int i) {

		if(i >= resultList.size()){
			return null ;
		}

		return resultList.get(i);
	}

	public static void loadRecipes(){

		if(recipyList.isEmpty() && resultList.isEmpty()){
			addRecipe(new ItemStack[]{new ItemStack(Items.woodChip, 1),new ItemStack(Items.woodChip, 1)}, new ItemStack(Items.stick, 2));

			addRecipe(new ItemStack[]{new ItemStack(Items.stick, 1),new ItemStack(Items.stick, 1)}, new ItemStack(Items.handle_small, 1));
			addRecipe(new ItemStack[]{new ItemStack(Items.handle_small, 1),new ItemStack(Items.stick, 1)}, new ItemStack(Items.handle_soft, 1));
			addRecipe(new ItemStack[]{new ItemStack(Items.handle_soft, 1),new ItemStack(Items.stick, 1)}, new ItemStack(Items.handle_hard, 1));

			addRecipe(new ItemStack[]{new ItemStack(Items.handle_small, 1),new ItemStack(Items.woodChip, 1)}, new ItemStack(Items.wood_sword, 1));
			addRecipe(new ItemStack[]{new ItemStack(Items.handle_small, 1),new ItemStack(Items.refinedStone, 1)}, new ItemStack(Items.stone_sword, 1));
			addRecipe(new ItemStack[]{new ItemStack(Items.handle_small, 1),new ItemStack(Items.ingot, 1)}, new ItemStack(Items.iron_sword, 1));

			addRecipe(new ItemStack[]{new ItemStack(Items.handle_soft, 1),new ItemStack(Items.woodChip, 1)}, new ItemStack(Items.wood_axe, 1));
			addRecipe(new ItemStack[]{new ItemStack(Items.handle_soft, 1),new ItemStack(Items.refinedStone, 1)}, new ItemStack(Items.stone_axe, 1));
			addRecipe(new ItemStack[]{new ItemStack(Items.handle_soft, 1),new ItemStack(Items.ingot, 1)}, new ItemStack(Items.iron_axe, 1));

			addRecipe(new ItemStack[]{new ItemStack(Items.handle_hard, 1),new ItemStack(Items.woodChip, 1)}, new ItemStack(Items.wood_pickaxe, 1));
			addRecipe(new ItemStack[]{new ItemStack(Items.handle_hard, 1),new ItemStack(Items.refinedStone, 1)}, new ItemStack(Items.stone_pickaxe, 1));
			addRecipe(new ItemStack[]{new ItemStack(Items.handle_hard, 1),new ItemStack(Items.ingot, 1)}, new ItemStack(Items.iron_pickaxe, 1));

			addRecipe(new ItemStack[]{new ItemStack(Items.leather_strap, 1),new ItemStack(Items.leather_strap, 1)}, new ItemStack(Items.belt, 1));
			addRecipe(new ItemStack[]{new ItemStack(Items.leather_fine, 1),new ItemStack(Items.leather_fine, 1)}, new ItemStack(Items.leather_strap, 1));
			addRecipe(new ItemStack[]{new ItemStack(Items.leather, 1),new ItemStack(Items.leather, 1)}, new ItemStack(Items.leather_fine, 1));

			addRecipe(new ItemStack[]{new ItemStack(Items.leather, 1),new ItemStack(Items.refinedStone, 1)}, new ItemStack(Items.whetstone, 1));

			addRecipe(new ItemStack[]{new ItemStack(Items.stone, 1),new ItemStack(Items.stone, 1)}, new ItemStack(Items.refinedStone, 1));
			addRecipe(new ItemStack[]{new ItemStack(Items.refinedStone, 1),new ItemStack(Items.refinedStone, 1)}, new ItemStack(Items.ovenBase, 1));

			addRecipe(new ItemStack[]{new ItemStack(Items.stick, 1),new ItemStack(Items.gem_blue, 1)}, new ItemStack(Items.gem_hammer, 1));
			
			addRecipe(new ItemStack[]{new ItemStack(Items.leather_fine, 1),new ItemStack(Items.plate_iron, 1)}, new ItemStack(Items.helm_iron, 1));
			addRecipe(new ItemStack[]{new ItemStack(Items.leather_fine, 1),new ItemStack(Items.plate_wood, 1)}, new ItemStack(Items.helm_wood, 1));

			addRecipe(new ItemStack[]{new ItemStack(Items.plate_wood, 1),new ItemStack(Items.plate_wood, 1)}, new ItemStack(Items.chest_wood, 1));
			addRecipe(new ItemStack[]{new ItemStack(Items.plate_iron, 1),new ItemStack(Items.plate_iron, 1)}, new ItemStack(Items.chest_iron, 1));

			addRecipe(new ItemStack[]{new ItemStack(Items.plate_leather, 1),new ItemStack(Items.plate_iron, 1)}, new ItemStack(Items.pouch, 1));

		}
	}

	public static ItemStack resultAdvanced(int i){
		return null;
	}

	public static ItemStack[] getRecipeAdvanced(int i){
		return null;
	}

	public static String getCraftResultName(ItemStack stack, ItemStack stack2, int slot, int slot2){
		if(stack == null && stack2 == null)
			return "no stacks...";

		if(stack.equals(stack2) && slot == slot2){
			if(stack.stackSize <= 1){
				return "stacksize to low...";
			}
		}

		for(int i = 0 ; i < recipyList.size(); i++){

			ItemStack[] compare = recipe(i);

			if(compare == null || compare[0] == null || compare[1] == null)
				continue;

			if(compare.length == 2){
				//any match. does not allow for order based crafting

				if(compare[0].equals(stack) && compare[1].equals(stack2) || 
						compare[0].equals(stack2) && compare[1].equals(stack)){

					return result(i).getItem().getDisplayName();
				}
			}
		}

		return "nothing";
	}

	public static void craft(Player player, ItemStack[] stacks, int[] slots){

		if(stacks[0] == null || stacks[1] == null)
			return;

		if(stacks[0].equals(stacks[1]) && slots[0] == slots[1]){
			if(stacks[0].stackSize <= 1){

				System.out.println("Cannot combine one item with itself !");

				return;	
				//if both objects are the same, and come from the same slot,
				//their stacksize has to be more then one to be able to be combined !
			}
		}

		boolean callBack = true;

		for(int i = 0 ; i < recipyList.size(); i++){

			ItemStack[] compare = recipe(i);

			if(compare == null || compare[0] == null || compare[1] == null)
				continue;

			if(compare.length == 2){
				//any match. does not allow for order based crafting

				if(compare[0].equals(stacks[0]) && compare[1].equals(stacks[1]) || 
						compare[0].equals(stacks[1]) && compare[1].equals(stacks[0])){

					boolean flag = false;

					for(int content = 0; content < player.getInventory().getMaxSlots(); content++){

						if(player.getStackInSlot(content) == null ||
								player.getStackInSlot(content) != null && player.getStackInSlot(content).getItem().getUIN().equals(resultList.get(i).getItem().getUIN())){

							Util.decreaseStack(player, slots[0], 1);
							Util.decreaseStack(player, slots[1], 1);
							flag = true;
							callBack = false;
							break;
						}
					}

					if(flag)
						player.setStackInNextAvailableSlot(result(i));
				}
			}else{
				System.out.println("Recipe was longer then 2 !");
				return;
			}
		}

		ItemStack craftingItem = null;
		ItemStack craftedItem = null;
		int toggle = -1;

		if(stacks[0].getItem() instanceof ICraftingItem){
			craftingItem = stacks[0];
			craftedItem = stacks[1];
			toggle = 1;
		}
		else if (stacks[1].getItem() instanceof ICraftingItem){
			craftingItem = stacks[1];
			craftedItem = stacks[0];
			toggle = 0;
		}

		if(craftingItem != null){

			ICraftingItem item = ((ICraftingItem)craftingItem.getItem());
			
			if(item.canCraft(craftedItem)){
				item.craft(player, craftedItem, slots[toggle]);
				callBack = false;	
			}
		}

		//no recipe matched, nor any of the items was a crafting item,  try craftingCallBack
		if(callBack){
			System.out.println("no recipe match found... ressorting to callback");

			ItemStack copy = stacks[1].copy();
			Item item = stacks[1].getItem();

			item.craftingCallBack(stacks[0], stacks[1]);

			player.setStackInSlot(slots[1], null);
			player.setStackInSlot(slots[1], stacks[1]);

			if(!copy.equals(stacks[1])){

				Util.decreaseStack(player, slots[0], 1);
			}
		}
	}

	public static void craft(Player player, int recipe, boolean advanced){

		ItemStack[] input = null;
		ItemStack result = null;

		if(!advanced){
			input = recipe(recipe);
			result = result(recipe);
		}else{
			input = getRecipeAdvanced(recipe);
			result = resultAdvanced(recipe);
		}

		if(input == null)
			return;
		if(result == null)
			return;


		int index = 0;

		for(ItemStack st: input){
			if(st != null)
				index++;
		}

		boolean flag[] = new boolean[index];

		for(int it = 0; it < input.length; it++){
			for(int i = 0; i < player.getInventory().getItems().length; i++){
				if(player.getStackInSlot(i) != null){
					if(input[it] != null){
						if(player.getStackInSlot(i).getItem().equals(input[it].getItem())){
							if(player.getStackInSlot(i).stackSize >= input[it].stackSize){
								flag[it] = true;
							}
						}
					}
				}
			}
		}

		for(boolean b : flag)
			if(!b){
				System.out.println("Not all components are aquiered.");
				return;
			}

		//Remove items before crafting the item
		for(int a = 0; a < input.length; a++){
			if(input[a] != null){
				int i = player.getInventory().getSlotForStack(input[a]);
				if(i == -1)
				{
					System.out.println("components requiered cannot be reached !");
					return;
				}
				player.getItems()[i].stackSize -= input[a].stackSize;
				if(player.getItems()[i].stackSize == 0)
					player.getItems()[i] = null;
			}
		}

		player.setStackInNextAvailableSlot(result);

	}
}