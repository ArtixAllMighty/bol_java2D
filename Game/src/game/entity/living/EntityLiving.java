package game.entity.living;

import java.awt.Graphics2D;

import engine.game.GameWorld;
import engine.game.MapObject;
import engine.music.Music;
import engine.save.DataTag;
import game.World;
import game.entity.living.enemy.IEnemy;
import game.entity.living.player.Player;
import game.item.ItemStack;
import game.item.ItemTool;

public class EntityLiving extends MapObject{

	protected float health = 1;
	protected float maxHealth = 2;

	private boolean flicker;
	private int flickerTimer = 100;


	private double knockBackStart = 20;
	/**
	 * counter set by knockbackstart that counts down to how long this entity is knocked back.
	 * while this happens, the moveSpeed is replaced by knockBackForce
	 */
	protected double knockBack;
	private boolean knockedBack;

	/**how hard the entity is knocked back*/
	protected double knockBackForce;

	private double defMaxSpeed;
	private double defMoveSpeed;

	public EntityLiving(GameWorld world, String uin) {
		super(world, uin);

		knockBackForce = 3d;
	}

	@Override
	public void draw(Graphics2D g) {

		if(flickerTimer % 5 == 0)
		{
			super.draw(g);
		}
	}

	public void hurtEntity(float f, Player player){

		Music.play(getEntityHitSound());

		health -= f;
		if(health <=0){
			this.remove=true;
			kill(player);
		}
		if(health > maxHealth)
			health = maxHealth;
	}

	public EntityLiving initHealth(float health){
		this.health = health;
		maxHealth = health;
		return this;
	}

	@Override
	public void writeToSave(DataTag data) {
		super.writeToSave(data);

		data.writeFloat("health", health);
		data.writeFloat("maxHealth", maxHealth);

	}

	@Override
	public void readFromSave(DataTag data) {
		super.readFromSave(data);

		health = data.readFloat("health");
		maxHealth = data.readFloat("maxHealth");
	}

	public float getHealth(){
		return health;
	}

	public float getMaxHealth(){
		return maxHealth;
	}

	public boolean canPlayDeathAnimation(){
		return true;
	}

	public String getEntityHitSound(){
		return null;
	}

	public String getEntityIdleSound(){
		return null;
	}

	@Override
	public void update() {
		super.update();

		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);

		if(flicker)
			flickerTimer++;
		if(flickerTimer > 50){
			flickerTimer = 0;
			flicker = false;
		}

		if(knockedBack){
			maxSpeed = knockBackForce;
			moveSpeed = knockBackForce/5;
			// if the player is looking left, knock the entity to the left
			if(((World)world).getPlayer().facingRight){
				setVector(5, -2);
				setRight(true);
				facingRight = true;
			}
			else{ 
				setVector(-5, -2);
				setLeft(true);
				facingRight = false;
			}
		}

		if(knockedBack)
			if(knockBack > 0)
				knockBack -= 0.5d;
			else{
				maxSpeed = defMaxSpeed;
				moveSpeed = defMoveSpeed;
				knockedBack = false;
				knockBack = 0;
			}
		
		EntityLiving living = this;
		if(living instanceof IEnemy){
			
		}
		
	}

	public void initMaxSpeed(double speed){
		maxSpeed = defMaxSpeed = speed;
	}

	public void initMoveSpeed(double speed){
		moveSpeed = defMoveSpeed = speed;
	}

	@Override
	public void onEntityHit(float damage) {
		hurtEntity(damage, null);
	}

	@Override
	public void onEntityHit(Player player) {

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

		hurtEntity(wepDmg + dmg, p );

		knockBack();
	}


	/**
	 * Method called when the entity has no mroe health left.
	 * Use this method to set any drops in the player inventory.
	 * if no super call is used, do not forget to set this.remove to true !
	 * Player can be null !!
	 */
	public void kill(Player player){

		if(player!= null)
			if(getDrops() != null){
				int index;

				if(getDrops().length == 1)
					index = 0;
				else
					index = rand.nextInt(getDrops().length);

				if(player.setStackInNextAvailableSlot(getDrops()[index])){
					this.remove = true;
				}else{
					health = maxHealth;
				}
			}
			else
				remove = true;
		else
			this.remove = true;
	}

	public ItemStack[] getDrops() {
		return null;
	}

	@Override
	public boolean hasAnimation() {
		return true;
	}

	@Override
	public boolean persistantUpdate() {
		return true;
	}

	public void knockBack(){
		knockedBack = true;
		knockBack = 10; 
	}

	public boolean isKnockedBack(){
		return knockedBack;
	}
}