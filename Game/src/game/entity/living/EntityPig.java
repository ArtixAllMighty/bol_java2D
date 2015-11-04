package game.entity.living;

import engine.game.MapObject;
import engine.game.entity.EntityLiving;
import engine.game.entity.EntityPlayer;
import engine.image.Images;
import engine.map.TileMap;
import engine.music.Music;
import game.World;
import game.entity.EntityAI;
import game.entity.living.player.Player;
import game.item.ItemStack;
import game.item.ItemTool;
import game.item.Items;

import java.awt.Graphics2D;


public class EntityPig extends EntityLiving{

	private EntityAI ai = new EntityAI();
	private boolean flicker;
	private int flickerTimer = 100;

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
		if(flicker)
			flickerTimer++;
		if(flickerTimer > 50){
			flickerTimer = 0;
			flicker = false;
		}
		if(flickerTimer % 5 == 0)
		{
			super.draw(g);
		}

	}

	@Override
	public boolean hasAnimation() {
		return true;
	}

	@Override
	public void update() {
		super.update();

		getNextPosition(); // needed for falling
		checkTileMapCollision();
		setPosition(xtemp, ytemp);

		ai.walkAroundRandomly(this);

		calmDown();
	}

	@Override
	public void onEntityHit(EntityPlayer player, MapObject mo) {

		Player p = (Player)player;
		flicker = true;

		int dmg = p.getAttackDamage();

		int wepDmg = 0;
		ItemStack wep = p.invArmor.getWeapon();

		if(wep != null && wep.getItem() instanceof ItemTool){
			ItemTool tool = (ItemTool)wep.getItem();
			if(ItemTool.SWORD == tool.getEffectiveness()){
				wepDmg = ((ItemTool)wep.getItem()).getEffectiveDamage();
			}
			wep.damageStack(1);
		}

		health -= wepDmg + dmg;

		Music.play(getEntityHitSound());
		ai.panic(this);
		if(health < 0)
			kill(p);
	}

	private ItemStack[] drops = new ItemStack[3];

	public ItemStack[] getDrops() {
		drops[0] = new ItemStack(Items.meat_pig_raw, rand.nextInt(2)+1);
		drops[1] = new ItemStack(Items.grease, rand.nextInt(2)+1);
		drops[2] = new ItemStack(Items.leather, rand.nextInt(3)+1);
		return drops;

	}

	public void kill(Player p)
	{
		if(p.setStackInNextAvailableSlot(getDrops()[rand.nextInt(drops.length)])){
			this.remove = true;
		}else{
			health = maxHealth;
		}
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
