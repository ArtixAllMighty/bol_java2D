package game.item.block;

import engine.game.MapObject;
import engine.map.TileMap;
import game.World;
import game.block.Blocks;
import game.entity.living.player.Player;
import game.item.Item;
import game.item.ItemStack;
import game.util.Util;

public class ItemBlock extends Item{

	public ItemBlock(String s, String displayName) {
		super(s, displayName);
	}

	public void placeBlock(World world, Player p){
		MapObject mo = Blocks.loadBlockFromString(getUIN(), world);
		mo.setPosition(p.getPosX(), p.getPosY());
		world.addEntity(mo);
	}

	@Override
	public void useItem(ItemStack item, TileMap map, World world, Player player, int key) {
		ItemBlock ib = (ItemBlock)item.getItem();
		ib.placeBlock(world, player);

		Util.decreaseStack(player.getInventory(), key, 1);
	}

	@Override
	public boolean hasInventoryCallBack(Player player) {
		return true;
	}

	@Override
	public void inventoryCallBack(int slot, Player player) {

		if(player.getStackInSlot(slot).getItem() != null){
			//place down blocks
			if(player.getStackInSlot(slot).getItem() instanceof ItemBlock){
				ItemBlock ib = (ItemBlock)player.getStackInSlot(slot).getItem();
				ib.placeBlock(player.getWorld(), player);

				Util.decreaseStack(player.getInventory(), slot, 1);
			}
		}

	}

}
