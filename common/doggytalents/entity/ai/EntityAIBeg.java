package doggytalents.entity.ai;

import doggytalents.entity.EntityDTDoggy;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 */
public class EntityAIBeg extends EntityAIBase
{
    private EntityDTDoggy theDog;
    private EntityPlayer thePlayer;
    private World worldObject;
    private float minPlayerDistance;
    private int field_75384_e;

    public EntityAIBeg(EntityDTDoggy par1EntityDTDoggy, float playerMinDistance)
    {
        this.theDog = par1EntityDTDoggy;
        this.worldObject = par1EntityDTDoggy.worldObj;
        this.minPlayerDistance = playerMinDistance;
        this.setMutexBits(2);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        this.thePlayer = this.worldObject.getClosestPlayerToEntity(this.theDog, (double)this.minPlayerDistance);
        return this.thePlayer == null ? false : this.hasPlayerGotBoneInHand(this.thePlayer);
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean continueExecuting()
    {
        return !this.thePlayer.isEntityAlive() ? false : (this.theDog.getDistanceSqToEntity(this.thePlayer) > (double)(this.minPlayerDistance * this.minPlayerDistance) ? false : this.field_75384_e > 0 && this.hasPlayerGotBoneInHand(this.thePlayer));
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting()
    {
        this.theDog.func_70918_i(true);
        this.field_75384_e = 40 + this.theDog.getRNG().nextInt(40);
    }

    /**
     * Resets the task
     */
    @Override
    public void resetTask()
    {
        this.theDog.func_70918_i(false);
        this.thePlayer = null;
    }

    /**
     * Updates the task
     */
    @Override
    public void updateTask()
    {
        this.theDog.getLookHelper().setLookPosition(this.thePlayer.posX, this.thePlayer.posY + (double)this.thePlayer.getEyeHeight(), this.thePlayer.posZ, 10.0F, (float)this.theDog.getVerticalFaceSpeed());
        --this.field_75384_e;
    }

    /**
     * Gets if the Player has the Bone in the hand.
     */
    private boolean hasPlayerGotBoneInHand(EntityPlayer par1EntityPlayer)
    {
        ItemStack itemstack = par1EntityPlayer.inventory.getCurrentItem();
        return itemstack == null ? false : (!this.theDog.isTamed() && itemstack.itemID == Item.bone.itemID ? true : this.theDog.isBreedingItem(itemstack));
    }
}
