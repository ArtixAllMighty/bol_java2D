package game.item;

import engine.save.DataList;
import engine.save.DataTag;
import game.item.tool.ToolModifier;

import java.util.ArrayList;
import java.util.List;

public class ItemStack
{
	public int stackSize;
	private Item item;

	private int damage; 

	protected List <String> tooltipList;

	private ToolModifier[] mods;

	private int modifiersCount;

	public ItemStack(Item i, int size) {
		item = i;
		stackSize = size;

		modifiersCount = i.getModifiers();

		initMaxDamage();
		initToolTips();
	}

	private void initToolTips(){
		tooltipList = new ArrayList<String>();

		//always add name first
		tooltipList.add(getItem().getDisplayName());

		//add item info
		if(getItem().getInfo(this) != null)
			for(String s : getItem().getInfo(this)){
				tooltipList.add(s);
			}

		//if item can be damaged
		if(getMaxDamage() > 0)
			tooltipList.add(getDamage()+"/"+getMaxDamage());

		if(getItem().hasModifiers()){
			if(mods != null){
				int count = 0;

				for(ToolModifier mod : mods){
					if(mod != null)
						count ++;
				}
				tooltipList.add("Mods : " + count + "/"  + (Math.max(getItem().getModifiers(), getMods().length)));
			}else{
				tooltipList.add("Mods : " + "0/"+ getItem().getModifiers());
			}
		}
		
		if(mods != null){
			String name = tooltipList.get(0);
			String prefix = getModNameForStack();
			tooltipList.set(0, prefix+name);
		}
		
	}

	public ItemStack(Item i) {
		this(i, 1);
	}

	public Item getItem(){
		return item;
	}

	public int getMaxDamage(){
		return getItem().getMaxDurability(this);
	}

	private ItemStack initMaxDamage(){
		damage = getItem().getMaxDurability(this);
		return this;
	}

	public void setDamage(int i){
		//		if(i > getMaxDamage())
		//			i = getMaxDamage();

		damage = i;

		initToolTips();
	}
	public int getDamage(){
		return damage;
	}

	public void damageStack(int i){
		damage-= i;
		initToolTips();
	}

	public static ItemStack createFromSave(DataTag data){
		String s = data.readString("uniqueItemName");
		Item item = Items.getItemFromUIN(s);

		if(item == null){
			System.out.println("the loaded itemstack was errored. " + s + " is not an item !");
			return null;
		}
		int size = data.readInt("stacksize");
		int dmg = data.readInt("dmg");
		item.readFromSave(data);

		ItemStack is = new ItemStack(item, size);
		is.setDamage(dmg);

		DataList modList = data.readList("mods");

		if(modList != null){
			is.mods = new ToolModifier[is.item.getModifiers()];
			for(int i = 0; i < modList.data().size(); i ++){
				DataTag dt = modList.readArray(i);
				ToolModifier mod = new ToolModifier(dt);
				is.mods[i] = mod;
			}
		}

		is.initToolTips();

		return is;
	}

	public void writeToSave(DataTag data){
		data.writeString("uniqueItemName", item.getUIN());
		data.writeInt("stacksize", stackSize);
		data.writeInt("dmg", getDamage());
		item.writeToSave(data);

		DataList modList = null;

		if(mods != null){
			modList = new DataList();
			for(ToolModifier mod : mods){
				if(mod != null){
					DataTag tag = new DataTag();
					mod.saveModifier(tag);
					modList.write(tag);
				}
			}
		}

		data.writeList("mods", modList);

	}

	public ItemStack copy(){

		ItemStack stack = new ItemStack(item, stackSize);
		stack.setDamage(damage);

		if(mods != null){
			stack.mods = new ToolModifier[mods.length];
			for(int i = 0; i < mods.length; i++)
				stack.mods[i] = mods[i];
		}

		stack.initToolTips();

		return stack;
	}

	@Override
	public boolean equals(Object obj) {

		if(obj instanceof ItemStack){
			ItemStack compared = (ItemStack)obj;
			if(compared.item != null){
				if(compared.item.getUIN().equals(item.getUIN())){
					if(compared.damage == damage){
						if(compareMods(mods, compared.mods))
							return true;
					}
				}
			}
		}

		return false;
	}

	private boolean compareMods(ToolModifier[] mods, ToolModifier[] compared) {
		if(mods == null && compared == null)
			return true;

		//if any installed, compare
		else if( mods != null && compared != null && mods.length == compared.length){
			int count = 0;
			for(int i = 0; i < mods.length ; i++)
				if(mods[i] != null && compared[i] != null){
					if(mods[i].equals(compared[i]))
						count++;
				}else if (mods[i] == null && compared[i] == null)
					count ++;

			return count == mods.length;
		}
		return false;
	}

	public boolean canAddModifier(){

		if(mods == null)
			return true;

		int count = 0;

		for(ToolModifier mod : mods)
			if(mod == null)
				count ++;

		return count > 0;
	}

	public void addModifier(ToolModifier mod){

		if(mods == null){
			mods = new ToolModifier[modifiersCount];
			mods[0] = mod;
		}

		else{
			for(int i = 0; i < mods.length; i++){
				if(mods[i] == null){
					mods[i] = mod;
					break;
				}
			}
		}

		initToolTips();
	}

	/**
	 * Allows for stack related info to be added to the tooltip
	 */
	public List<String> getToolTip(){

		return tooltipList;
	}

	public ToolModifier[] getMods(){
		return mods;
	}

	/**returns modifier amount from mod if any was applied*/
	public int getBonus(int modID){
		int bonus = 0;

		if(getMods() != null)
			for(ToolModifier mod : getMods()){
				if(mod != null){
					if(mod.getModID2() == modID)
						bonus += mod.getModifier2();
					if(mod.getModID() == modID)
						bonus += mod.getModifier();
				}
			}
		return bonus;
	}

	public int getModifierCount(int modID){
		int count = 0;

		if(getMods() != null)
			for(ToolModifier mod : getMods()){
				if(mod != null){
					if(mod.getModID2() == modID)
						count ++;
					if(mod.getModID() == modID)
						count ++;
				}
			}
		return count;
	}

	private String getModNameForStack(){

		String name = "";

		if(getBonus(ToolModifier.DMG) >= 20)
			name += "Ultra Sharp ";
		else if(getBonus(ToolModifier.DMG) >= 10)
			name += "Pretty Sharp ";
		else if(getBonus(ToolModifier.DMG) >= 5)
			name += "Sharp ";

		if(getModifierCount(ToolModifier.DUR) >= 10)
			name += "Imperishable ";
		else if(getModifierCount(ToolModifier.DUR) >= 5)
			name += "Strong ";
		else if(getModifierCount(ToolModifier.DUR) >= 3)
			name += "Robust ";

		if(getModifierCount(ToolModifier.EFF) >= 5)
			name += "Virtuous ";
		else if(getModifierCount(ToolModifier.EFF) >= 3)
			name += "Shrewd ";

		return name;
	}

	@Override
	public String toString() {
		return getItem().getDisplayName() + " " + damage + " " + getMaxDamage() + " ";
	}
}