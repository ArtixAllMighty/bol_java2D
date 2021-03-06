package game.item.tool;

import game.item.ItemStack;
import game.item.Items;

public class ItemPickaxe extends ItemTool {

	public ItemPickaxe(String uin, String displayName, EnumMaterial material) {
		super(uin, displayName, material);
	}

	@Override
	public void craftingCallBack(ItemStack component, ItemStack base) {
		super.craftingCallBack(component, base);
		
		if(component.getItem().equals(Items.woodChip) && getMaterial() == EnumMaterial.WOOD){
			mod = 8;
			base.addModifier(new ToolModifier(ToolModifier.DUR, mod, ToolModifier.EFF, 1));
			base.damageStack(-mod);
		}
		else if(component.getItem().equals(Items.refinedStone) && getMaterial() == EnumMaterial.STONE){
			mod = 8;
			base.addModifier(new ToolModifier(ToolModifier.DUR, mod, ToolModifier.EFF, 1));
			base.damageStack(-mod);
		}
		else if(component.getItem().equals(Items.ingot) && getMaterial() == EnumMaterial.IRON){
			mod = 24;
			base.addModifier(new ToolModifier(ToolModifier.DUR, mod, ToolModifier.EFF, 2));
			base.damageStack(-mod);
		}
	}
}
