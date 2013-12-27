/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the ThaumicTinkerer Mod.
 *
 * ThaumicTinkerer is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 *
 * ThaumicTinkerer is a Derivative Work on Thaumcraft 4.
 * Thaumcraft 4 (c) Azanor 2012
 * (http://www.minecraftforum.net/topic/1585216-)
 *
 * File Created @ [14 Sep 2013, 01:13:25 (GMT)]
 */
package vazkii.tinkerer.common.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.wands.ItemWandCasting;
import vazkii.tinkerer.client.core.helper.IconHelper;
import vazkii.tinkerer.common.ThaumicTinkerer;
import vazkii.tinkerer.common.block.tile.TileEnchanter;
import vazkii.tinkerer.common.core.handler.ConfigHandler;
import vazkii.tinkerer.common.item.ModItems;
import vazkii.tinkerer.common.lib.LibGuiIDs;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockEnchanter extends BlockModContainer {

	Random random;

	Icon iconBottom;
	Icon iconTop;
	Icon iconSides;

	protected BlockEnchanter(int par1) {
		super(par1, Material.rock);
		setBlockBounds(0F, 0F, 0F, 1F, 0.75F, 1F);
		setHardness(5.0F);
		setResistance(2000.0F);

		random = new Random();
	}

	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
		if(!par1World.isRemote) {
			TileEntity tile = par1World.getBlockTileEntity(par2, par3, par4);
			if(tile != null) {
				PacketDispatcher.sendPacketToAllInDimension(tile.getDescriptionPacket(), par1World.provider.dimensionId);
				par5EntityPlayer.openGui(ThaumicTinkerer.instance, LibGuiIDs.GUI_ID_ENCHANTER, par1World, par2, par3, par4);
			}
		}

		return true;
	}

	@Override
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
        TileEnchanter enchanter = (TileEnchanter) par1World.getBlockTileEntity(par2, par3, par4);

        if (enchanter != null) {
            for (int j1 = 0; j1 < enchanter.getSizeInventory(); ++j1) {
                ItemStack itemstack = enchanter.getStackInSlot(j1);

                if (itemstack != null) {
                    float f = random.nextFloat() * 0.8F + 0.1F;
                    float f1 = random.nextFloat() * 0.8F + 0.1F;
                    EntityItem entityitem;

                    for (float f2 = random.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; par1World.spawnEntityInWorld(entityitem)) {
                        int k1 = random.nextInt(21) + 10;

                        if (k1 > itemstack.stackSize)
                            k1 = itemstack.stackSize;

                        itemstack.stackSize -= k1;
                        entityitem = new EntityItem(par1World, par2 + f, par3 + f1, par4 + f2, new ItemStack(itemstack.itemID, k1, itemstack.getItemDamage()));
                        float f3 = 0.05F;
                        entityitem.motionX = (float)random.nextGaussian() * f3;
                        entityitem.motionY = (float)random.nextGaussian() * f3 + 0.2F;
                        entityitem.motionZ = (float)random.nextGaussian() * f3;

                        if (itemstack.hasTagCompound())
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                    }
                }
            }

            par1World.func_96440_m(par2, par3, par4, par5);
        }

        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister) {
		iconBottom = IconHelper.forBlock(par1IconRegister, this, 0);
		iconTop = IconHelper.forBlock(par1IconRegister, this, 1);
		iconSides = IconHelper.forBlock(par1IconRegister, this, 2);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int par1, int par2) {
		return par1 == ForgeDirection.UP.ordinal() ? iconTop : par1 == ForgeDirection.DOWN.ordinal() ? iconBottom : iconSides;
	}

	@Override
	public boolean renderAsNormalBlock() {
        return false;
    }

	@Override
	public boolean isOpaqueCube() {
        return false;
    }

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEnchanter();
	}

}
