package game.entity;

import engine.game.GameWorld;
import engine.game.entity.EntityLiving;
import engine.map.TileMap;
import game.World;
import game.entity.living.EntityBat;
import game.entity.living.EntityPig;
import game.entity.living.environement.EntityDeathAnim;

public class Entity {

	public static final String PIG = "pig";
	public static final String BAT = "bat";
	public static final String DEATHANIM = "da";
	
	public static EntityLiving createEntityFromUIN(String s, TileMap tm, GameWorld world){
		
		switch (s) {
		case PIG:
			return new EntityPig(tm, (World)world, PIG);
		case DEATHANIM :
			return new EntityDeathAnim(tm, (World)world);
		case BAT :
			return new EntityBat(tm, (World)world, BAT);
			
		}
		return null;
	}
}
