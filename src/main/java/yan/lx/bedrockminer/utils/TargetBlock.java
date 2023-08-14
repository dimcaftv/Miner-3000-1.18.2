package yan.lx.bedrockminer.utils;

import net.minecraft.block.Blocks;
import net.minecraft.block.PistonBlock;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;

public class TargetBlock {
    private final BlockPos blockPos;
    private BlockPos redstoneTorchBlockPos;
    private final BlockPos pistonBlockPos;
    private final ClientWorld world;
    private Status status;
    private BlockPos slimeBlockPos;
    private int tickTimes;
    private boolean hasTried;
    private int stuckTicksCounter;
    private final Direction placeDir;

    public TargetBlock(BlockPos pos, ClientWorld world) {
        this.placeDir = Direction.UP;
        this.hasTried = false;
        this.stuckTicksCounter = 0;
        this.status = Status.UNINITIALIZED;
        this.blockPos = pos;
        this.world = world;
        this.pistonBlockPos = pos.offset(placeDir);
        this.redstoneTorchBlockPos = CheckingEnvironment.findNearbyFlatBlockToPlaceRedstoneTorch(this.world, this.blockPos);
        if (redstoneTorchBlockPos == null) {
            this.slimeBlockPos = CheckingEnvironment.findPossibleSlimeBlockPos(world, pos);
            if (slimeBlockPos != null) {
                BlockPlacer.simpleBlockPlacement(slimeBlockPos, Blocks.SLIME_BLOCK);
                redstoneTorchBlockPos = CheckingEnvironment.findNearbyFlatBlockToPlaceRedstoneTorch(this.world, this.blockPos);
            } else {
                this.status = Status.FAILED;
            }
        }
    }

    public Status tick() {
        this.tickTimes++;
        updateStatus();
        System.out.println(this.status);
        switch (this.status) {
            case UNINITIALIZED:
                BlockPlacer.advancedPlacement(pistonBlockPos, placeDir, Blocks.PISTON);
                BlockPlacer.simpleBlockPlacement(redstoneTorchBlockPos, Blocks.REDSTONE_TORCH);
                break;
            case UNEXTENDED_WITH_POWER_SOURCE:
            case NEEDS_WAITING:
                break;
            case EXTENDED:
                ArrayList<BlockPos> nearByRedstoneTorchPosList = CheckingEnvironment.findNearbyRedstoneTorch(world, pistonBlockPos);
                for (BlockPos pos : nearByRedstoneTorchPosList) {
                    BlockBreaker.breakBlock(pos);
                }
                BlockBreaker.breakBlock(pistonBlockPos);
                BlockPlacer.advancedPlacement(pistonBlockPos, placeDir.getOpposite(), Blocks.PISTON);
                hasTried = true;
                break;
            case RETRACTED:
                BlockBreaker.breakBlock(blockPos);
                BlockBreaker.breakBlock(pistonBlockPos);
                BlockBreaker.breakBlock(pistonBlockPos.offset(placeDir));
                if (this.slimeBlockPos != null) {
                    BlockBreaker.breakBlock(slimeBlockPos);
                }
                return Status.RETRACTED;
            case RETRACTING:
                return Status.RETRACTING;
            case UNEXTENDED_WITHOUT_POWER_SOURCE:
                BlockPlacer.simpleBlockPlacement(redstoneTorchBlockPos, Blocks.REDSTONE_TORCH);
                break;
            case FAILED:
                BlockBreaker.breakBlock(pistonBlockPos);
                BlockBreaker.breakBlock(pistonBlockPos.offset(placeDir));
                return Status.FAILED;
            case STUCK:
                BlockBreaker.breakBlock(pistonBlockPos);
                BlockBreaker.breakBlock(pistonBlockPos.offset(placeDir));
                break;
        }
        return null;
    }

    enum Status {
        FAILED,
        UNINITIALIZED,
        UNEXTENDED_WITH_POWER_SOURCE,
        UNEXTENDED_WITHOUT_POWER_SOURCE,
        EXTENDED,
        NEEDS_WAITING,
        RETRACTING,
        RETRACTED,
        STUCK
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public ClientWorld getWorld() {
        return world;
    }

    public Status getStatus() {
        return status;
    }

    private void updateStatus() {
        if (this.tickTimes > 40) {
            this.status = Status.FAILED;
            return;
        }
        this.redstoneTorchBlockPos = CheckingEnvironment.findNearbyFlatBlockToPlaceRedstoneTorch(this.world, this.blockPos);
        if (this.redstoneTorchBlockPos == null) {
            this.slimeBlockPos = CheckingEnvironment.findPossibleSlimeBlockPos(world, blockPos);
            if (slimeBlockPos != null) {
                BlockPlacer.simpleBlockPlacement(slimeBlockPos, Blocks.SLIME_BLOCK);
                redstoneTorchBlockPos = CheckingEnvironment.findNearbyFlatBlockToPlaceRedstoneTorch(this.world, this.blockPos);
            } else {
                this.status = Status.FAILED;
                Messager.actionBar("Failed to place redstone torch.");
            }
        } else if (this.world.getBlockState(this.pistonBlockPos).isOf(Blocks.PISTON) && (this.world.getBlockState(this.blockPos).isAir())) {
            this.status = Status.RETRACTED;
        } else if (this.world.getBlockState(this.pistonBlockPos).isOf(Blocks.PISTON) && this.world.getBlockState(this.pistonBlockPos).get(PistonBlock.EXTENDED)) {
            this.status = Status.EXTENDED;
        } else if (this.world.getBlockState(this.pistonBlockPos).isOf(Blocks.MOVING_PISTON)) {
            this.status = Status.RETRACTING;
        }  else if (this.world.getBlockState(this.pistonBlockPos).isOf(Blocks.PISTON) && !this.world.getBlockState(this.pistonBlockPos).get(PistonBlock.EXTENDED) && CheckingEnvironment.findNearbyRedstoneTorch(this.world, this.pistonBlockPos).size() != 0 && !(this.world.getBlockState(this.blockPos).isAir())) {
            this.status = Status.UNEXTENDED_WITH_POWER_SOURCE;
        } else if (this.hasTried && this.world.getBlockState(this.pistonBlockPos).isOf(Blocks.PISTON) && this.stuckTicksCounter < 15) {
            this.status = Status.NEEDS_WAITING;
            this.stuckTicksCounter++;
        } else if (this.world.getBlockState(this.pistonBlockPos).isOf(Blocks.PISTON) && this.world.getBlockState(this.pistonBlockPos).get(PistonBlock.FACING) == Direction.DOWN && !this.world.getBlockState(this.pistonBlockPos).get(PistonBlock.EXTENDED) && CheckingEnvironment.findNearbyRedstoneTorch(this.world, this.pistonBlockPos).size() != 0 && !(this.world.getBlockState(this.blockPos).isAir())) {
            this.status = Status.STUCK;
            this.hasTried = false;
            this.stuckTicksCounter = 0;
        } else if (this.world.getBlockState(this.pistonBlockPos).isOf(Blocks.PISTON) && !this.world.getBlockState(this.pistonBlockPos).get(PistonBlock.EXTENDED) && this.world.getBlockState(this.pistonBlockPos).get(PistonBlock.FACING) == Direction.UP && CheckingEnvironment.findNearbyRedstoneTorch(this.world, this.pistonBlockPos).size() == 0 && !(this.world.getBlockState(this.blockPos).isAir())) {
            this.status = Status.UNEXTENDED_WITHOUT_POWER_SOURCE;
        } else if (CheckingEnvironment.has2BlocksOfPlaceToPlacePiston(world, this.blockPos)) {
            this.status = Status.UNINITIALIZED;
        } else if (!CheckingEnvironment.has2BlocksOfPlaceToPlacePiston(world, this.blockPos)) {
            this.status = Status.FAILED;
            Messager.actionBar("Failed to place piston.");
        } else {
            this.status = Status.FAILED;
        }
    }

}
