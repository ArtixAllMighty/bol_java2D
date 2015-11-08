package game.entity.living;

import java.awt.Graphics2D;

import engine.game.entity.EntityLiving;
import engine.image.Images;
import engine.map.TileMap;
import game.World;
import game.entity.EntityAI;
import game.entity.living.player.Player;
import game.item.ItemStack;
import game.item.Items;


public class EntityPig extends EntityLiving{

	private EntityAI ai = new EntityAI();

	private double defMaxSpeed;
	private double defMoveSpeed;

	public EntityPig(TileMap tm, World world, String uin) {
		super(tm, world, uin);

		initHealth(8f);

		getAnimation().setFrames(Images.loadMultiImage("/entity/piggy.png", 32, 0, 4));
		getAnimation().setDelay(150);

		entitySizeX = 32;
		entitySizeY = 32;

		width = 32;
		height = 32;

		moveSpeed = defMoveSpeed = 0.05 + rand.nextDouble();  // inital walking speed. you speed up as you walk
		maxSpeed = defMaxSpeed = 0.5 + rand.nextDouble(); // change to jump farther and walk faster
		stopSpeed = 0.1;
		fallSpeed = 0.15; // affects falling and jumping
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;

		boolean b = rand.nextBoolean();
		facingRight = b;
		right = b;
		left = !b;

	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
	}

	@Override
	public void update() {
		super.update();

		ai.walkAroundRandomly(this);

		calmDown();
	}

	private ItemStack[] drops = new ItemStack[2];

	public ItemStack[] getDrops() {
		drops[0] = new ItemStack(Items.meat_pig_raw, rand.nextInt(2)+1);
		drops[1] = new ItemStack(Items.leather, rand.nextInt(3)+1);
		return drops;
	}

	public void kill(Player p)
	{
		if(p!= null)
			if(p.setStackInNextAvailableSlot(getDrops()[rand.nextInt(drops.length)])){
				this.remove = true;
			}else{
				health = maxHealth;
			}
		else
			super.kill(p);
	}

	/**gets the entity's speed back to normal after it panics (/gets hit by the player)*/
	private void calmDown(){

		if(moveSpeed > defMoveSpeed)
			moveSpeed-=0.1d;
		else if(moveSpeed < defMoveSpeed)
			moveSpeed +=0.1d;

		if(maxSpeed > defMaxSpeed)
			maxSpeed-=0.01d;
		else if(maxSpeed < defMaxSpeed)
			maxSpeed +=0.01d;

	}

	@Override
	public String getEntityHitSound() {
		return "hitpig_" + (rand.nextInt(5)+1);
	}
}
