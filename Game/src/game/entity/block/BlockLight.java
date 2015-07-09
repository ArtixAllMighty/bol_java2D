package game.entity.block;

import game.World;
import game.content.Images;
import game.content.save.DataTag;
import game.entity.Animation;
import game.entity.MapObject;
import game.entity.inventory.IInventory;
import game.entity.living.player.Player;
import game.gui.GuiFire;
import game.item.ItemStack;
import game.item.ItemTool;
import game.item.Items;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import base.tilemap.TileMap;

public class BlockLight extends BlockBreakable implements IInventory{

	public int lightRadius;

	public int timer = 0;

	Animation fire = new Animation();

	public BlockLight(TileMap tm, World world, String uin) {
		super(tm, world, uin, ItemTool.NOTHING);
		setHealth(2);
		fire.setFrames(Images.loadMultiImage("/blocks/camp_fire.png", 32, 0, 4));
		fire.setDelay(90);
	}

	public Block setRadius(int rad){
		lightRadius = rad;
		return this;
	}

	public int getRadius(){
		int t = timer == 0 ? 0 : (timer > (800 * 6) && timer < (800*8)) ? 200 : timer > (800*3) && timer < (800*6) ? 125 : timer > 0 && timer < (800*3) ? 75 : 250;
		return  t;
	}

	@Override
	public BufferedImage getEntityTexture() {
		return Items.campfire.getTexture();
	}

	@Override
	public ItemStack getDrop() {
		return new ItemStack(Items.campfire, 1);
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);

		if(timer > 0)
			g.drawImage(fire.getImage(),
					(int) ((xScreen + xmap) - (width / 2)),
					(int) ((yScreen + ymap) - (height / 2)), null);

	}

	@Override
	public void interact(Player p, MapObject o) {
		GuiFire gui = new GuiFire(this, p);
		gui.setBlock(this);
		getWorld().displayGui(gui);
	}

	public boolean isLit(){
		return timer > 0;
	}

	@Override
	public void update() {
		super.update();

		if(timer > 0)
			timer --;

		if(getStackInSlot(0) != null)
			if(getStackInSlot(0).getItem().isFuel()){
				timer += getStackInSlot(0).getItem().getFuelTimer();
				getStackInSlot(0).stackSize--;
				if(getStackInSlot(0).stackSize <=0)
					setStackInSlot(0, null);
			}

		if(timer > 0)
			fire.update();
	}

	@Override
	protected void mine(Player p) {
		//oven can not be destroyed with items in it
		if(timer <= 0){
			super.mine(p);
		}else{
			System.out.println("Fire can not be recovered while it is burning !");
			resetHealth();
		}
	}

	public void readFromSave(DataTag data) {
		super.readFromSave(data);
		timer = data.readInt("timer");
	}
	
	public void writeToSave(DataTag data) {
		super.writeToSave(data);
		data.writeInt("timer", timer);
		
	}
	
	
	/*===============INVENTORY==============*/

	ItemStack[] inventory = new ItemStack[1];

	@Override
	public ItemStack[] getItems() {
		return inventory;
	}

	@Override
	public boolean hasStack(ItemStack stack) {
		if(inventory[0] != null && stack != null && stack.getItem().equals(inventory[0].getItem()))
			return true;

		return false;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventory[0];
	}

	@Override
	public int getMaxSlots() {
		return 1;
	}

	@Override
	public boolean setStackInNextAvailableSlot(ItemStack item) {

		if(getStackInSlot(0) == null)
			setStackInSlot(0, item);

		if(item != null && getStackInSlot(0) != null && item.getItem().equals(getStackInSlot(0).getItem())){
			setStackInSlot(0, item);
			return true;
		}

		return false;
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		if(inventory[slot] == null)
			inventory[slot] = stack;
		else if (stack == null && inventory[slot] != null)
			inventory[slot] = null;
		else if(inventory[slot].getItem().equals(stack.getItem()))
			inventory[slot].stackSize += stack.stackSize;
		else
			System.out.println("something tried to replace the existing item" + inventory[slot].getItem().getUIN() +
					" by "+ stack.getItem().getUIN() );
	}

	@Override
	public void removeStack(int slot) {
		inventory[0] = null;
	}

	@Override
	public boolean hasStackInSlot(int slot) {
		return inventory[slot] != null;
	}

	@Override
	public IInventory getInventory() {
		return this;
	}

	@Override
	public int getSlotForStack(ItemStack stack) {
		return 0;
	}
}
