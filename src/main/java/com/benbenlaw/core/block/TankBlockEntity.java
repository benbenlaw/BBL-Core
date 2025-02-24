package com.benbenlaw.core.block;

import com.benbenlaw.core.block.entity.SyncableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;



public class TankBlockEntity extends SyncableBlockEntity {


    public TankBlockEntity(BlockPos pos, BlockState state) {
        //super(TestBlockEntities.TANK_BLOCK_ENTITY.get(), pos, state);
        super(null, pos, state);
    }


    public final FluidTank FLUID_TANK = new FluidTank(1000) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            sync();
        }
    };

    private final IFluidHandler fluidHandler = new IFluidHandler() {
        @Override
        public int getTanks() {
            return 1;
        }

        @Override
        public FluidStack getFluidInTank(int tank) {
            return FLUID_TANK.getFluid();
        }

        @Override
        public int getTankCapacity(int tank) {
            return FLUID_TANK.getCapacity();
        }

        @Override
        public boolean isFluidValid(int tank, FluidStack stack) {
            return FLUID_TANK.isFluidValid(stack);
        }
        @Override
        public int fill(FluidStack resource, FluidAction action) {
            if (resource.getFluid() == FLUID_TANK.getFluid().getFluid() || FLUID_TANK.isEmpty()) {
                return FLUID_TANK.fill(resource, action);
            }
            return 0;
        }

        @Override
        public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
            assert level != null;

            if (resource.getFluid() == FLUID_TANK.getFluid().getFluid()) {
                return FLUID_TANK.drain(resource.getAmount(), action);
            }
            return FluidStack.EMPTY;
        }

        @Override
        public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
            if (FLUID_TANK.getFluidAmount() > 0) {
                return FLUID_TANK.drain(maxDrain, action);
            }
            return FluidStack.EMPTY;
        }
    };

    public IFluidHandler getFluidHandlerCapability() {
        return fluidHandler;
    }

    public void setFluid(FluidStack stack) {
        this.FLUID_TANK.setFluid(stack);
    }

    public void getFluid(FluidStack stack) {
        FLUID_TANK.setFluid(stack);
    }

    public FluidStack getFluidStack() {
        return this.FLUID_TANK.getFluid();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        super.saveAdditional(compoundTag, provider);
        compoundTag.put("tank_content", FLUID_TANK.writeToNBT(provider, new CompoundTag()));
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        FLUID_TANK.readFromNBT(provider, compoundTag.getCompound("tank_content"));
        super.loadAdditional(compoundTag, provider);

    }

    public boolean onPlayerUse(Player player, InteractionHand hand) {
        return FluidUtil.interactWithFluidHandler(player, hand, FLUID_TANK);
    }

    public void tick() {
        sync();
    }

}
