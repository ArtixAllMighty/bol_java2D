package game.entity.block.breakable;

import engine.music.Music;
import engine.save.DataTag;
import game.World;
import game.entity.block.Block;
import game.entity.living.player.Player;
import game.item.ItemStack;
import game.item.tool.ItemTool;
import game.item.tool.ItemTool.EnumTools;

import java.awt.Color;
import java.awt.Graphics2D;


public class BlockBreakable extends Block{

	private int health;
	private boolean jiggle;

	/**the tool that can be used to destroy this block*/
	private EnumTools effectiveTool;

	int tracker = 0;

	public BlockBreakable(World world, String uin) {
		super(world, uin);
	}

	public BlockBreakable(World world, String uin, EnumTools toolEffectiveness) {
		this(world, uin);
		setEffectiveTool(toolEffectiveness);
	}

	public BlockBreakable setEffectiveTool(EnumTools tool){
		effectiveTool = tool;
		return this;
	}

	public EnumTools getEffectiveTool(){
		return effectiveTool;
	}

	private int defaultHealth;

	public BlockBreakable setHealth(int i){
		health = i;
		defaultHealth = i;
		return this;
	}

	public void resetHealth(){
		health = defaultHealth;
	}

	public int getHealth(){
		return health;
	}

	@Override
	public void draw(Graphics2D g) {

		setMapPosition();

		if(jiggle){
			tracker ++;
			if(tracker < 2)
				xmap +=4;
			else if(tracker < 4)
				xmap-=4;
			else {
				jiggle = false;
				tracker = 0;
			}
		}
		if (facingRight)
			g.drawImage(getAnimation().getImage(),
					(int) ((xScreen + xmap) - (width / 2)),
					(int) ((yScreen + ymap) - (height / 2)), null);
		else
			g.drawImage(getAnimation().getImage(),
					(int) (((xScreen + xmap) - (width / 2)) + width),
					(int) ((yScreen + ymap) - (height / 2)), -width, height, null);

		if (getWorld().showBoundingBoxes) {
			g.setColor(Color.WHITE);
			g.draw(getRectangle());
		}
	}

	@Override
	public void onEntityHit(Player player) {

		int wepDmg = 0;

		ItemStack weaponStack = player.armorInventory.getWeapon();
		ItemTool tool = null;

		if(weaponStack != null && weaponStack.getItem() instanceof ItemTool)
			tool = ((ItemTool)weaponStack.getItem());

		if(tool != null)
			wepDmg = tool.getEffectiveDamage(weaponStack);

		if(tool == null){
			if(!needsToolToMine()){
				hit();
				health -= player.getAttackDamage();
			}
		}else{
			if(effectiveTool == tool.getEffectiveness()){
				hit();
				health -=wepDmg;
			}
		}

		if(health <= 0)
			mine(player);
	}

	@Override
	public void onEntityHit(float damage) {

		hit();

		health -= damage;

		if(health <= 0)
			remove = true;

	}

	private void hit(){
		jiggle = true;

		switch (getType()) {
		case ROCK:
			Music.play("hit_rock_" + (rand.nextInt(4)+1));
			break;
		case WOOD:
			Music.play("hit_wood_" + (rand.nextInt(5)+1));
			break;
		default:
			break;
		}
	}

	protected void mine(Player p){
		if(getDrop() != null){
			if(p.getInventory().setStackInNextAvailableSlot(getDrop())){
				remove = true;

				if(p.armorInventory.getWeapon() != null)
					p.armorInventory.getWeapon().damageStack(1);

			}else {
				remove = false;
				health = defaultHealth;
			}
		}else{
			health = defaultHealth;
		}
	}

	@Override
	public void writeToSave(DataTag data) {
		super.writeToSave(data);
		data.writeInt("punchHealth", health);

	}

	@Override
	public void readFromSave(DataTag data) {
		super.readFromSave(data);
		health = data.readInt("punchHealth");
	}

	public boolean needsToolToMine(){
		return false;
	}

	public World getWorld(){
		return (World)world;
	}
}
