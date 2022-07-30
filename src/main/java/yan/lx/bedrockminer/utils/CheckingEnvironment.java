package yan.lx.bedrockminer.utils;

import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;

import static net.minecraft.block.Block.sideCoversSmallSquare;

public class CheckingEnvironment {

    public static BlockPos findNearbyFlatBlockToPlaceRedstoneTorch(ClientWorld world, BlockPos blockPos, Direction dir) {
        if (dir == Direction.UP)
        {
            if ((sideCoversSmallSquare(world, blockPos.east(), dir) && (world.getBlockState(blockPos.east().up()).getMaterial().isReplaceable()) || world.getBlockState(blockPos.east().up()).isOf(Blocks.REDSTONE_TORCH))) {
                return blockPos.east();
            } else if ((sideCoversSmallSquare(world, blockPos.west(), dir) && (world.getBlockState(blockPos.west().up()).getMaterial().isReplaceable()) || world.getBlockState(blockPos.west().up()).isOf(Blocks.REDSTONE_TORCH))) {
                return blockPos.west();
            } else if ((sideCoversSmallSquare(world, blockPos.south(), dir) && (world.getBlockState(blockPos.south().up()).getMaterial().isReplaceable()) || world.getBlockState(blockPos.south().up()).isOf(Blocks.REDSTONE_TORCH))) {
                return blockPos.south();
            } else if ((sideCoversSmallSquare(world, blockPos.north(), dir) && (world.getBlockState(blockPos.north().up()).getMaterial().isReplaceable()) || world.getBlockState(blockPos.north().up()).isOf(Blocks.REDSTONE_TORCH))) {
                return blockPos.north();
            }
        }
        else if (dir == Direction.EAST || dir == Direction.WEST || dir == Direction.NORTH || dir == Direction.SOUTH)
        {
            if ((sideCoversSmallSquare(world, blockPos.offset(dir.rotateYClockwise()), dir) && (world.getBlockState(blockPos.offset(dir.rotateYClockwise()).offset(dir)).getMaterial().isReplaceable()) || world.getBlockState(blockPos.offset(dir.rotateYClockwise()).offset(dir)).isOf(Blocks.REDSTONE_TORCH))) {
                return blockPos.offset(dir.rotateYClockwise());
            } else if ((sideCoversSmallSquare(world, blockPos.offset(dir.rotateYClockwise().rotateYClockwise().rotateYClockwise()), dir) && (world.getBlockState(blockPos.offset(dir.rotateYClockwise().rotateYClockwise().rotateYClockwise()).offset(dir)).getMaterial().isReplaceable()) || world.getBlockState(blockPos.offset(dir.rotateYClockwise().rotateYClockwise().rotateYClockwise()).offset(dir)).isOf(Blocks.REDSTONE_TORCH))) {
                return blockPos.offset(dir.rotateYClockwise().rotateYClockwise().rotateYClockwise());
            } else if ((sideCoversSmallSquare(world, blockPos.up(), dir) && (world.getBlockState(blockPos.up().offset(dir)).getMaterial().isReplaceable()) || world.getBlockState(blockPos.up().offset(dir)).isOf(Blocks.REDSTONE_TORCH))) {
                return blockPos.up();
            } else if ((sideCoversSmallSquare(world, blockPos.down(), dir) && (world.getBlockState(blockPos.down().offset(dir)).getMaterial().isReplaceable()) || world.getBlockState(blockPos.down().offset(dir)).isOf(Blocks.REDSTONE_TORCH))) {
                return blockPos.down();
            }
        }
        else if (dir == Direction.DOWN)
        {
            if (world.getBlockState(blockPos.east().down()).getMaterial().isReplaceable() || world.getBlockState(blockPos.east().down()).isOf(Blocks.REDSTONE_TORCH))
            {
                if (sideCoversSmallSquare(world, blockPos.east().south().down(), Direction.NORTH) || sideCoversSmallSquare(world, blockPos.east().north().down(), Direction.SOUTH))
                {
                    return blockPos.east();
                }
            }
            else if (world.getBlockState(blockPos.west().down()).getMaterial().isReplaceable() || world.getBlockState(blockPos.west().down()).isOf(Blocks.REDSTONE_TORCH))
            {
                if (sideCoversSmallSquare(world, blockPos.west().south().down(), Direction.NORTH) || sideCoversSmallSquare(world, blockPos.west().north().down(), Direction.SOUTH))
                {
                    return blockPos.west();
                }
            }
            else if (world.getBlockState(blockPos.north().down()).getMaterial().isReplaceable() || world.getBlockState(blockPos.north().down()).isOf(Blocks.REDSTONE_TORCH))
            {
                if (sideCoversSmallSquare(world, blockPos.north().east().down(), Direction.WEST) || sideCoversSmallSquare(world, blockPos.north().west().down(), Direction.EAST))
                {
                    return blockPos.north();
                }
            }
            else if (world.getBlockState(blockPos.south().down()).getMaterial().isReplaceable() || world.getBlockState(blockPos.south().down()).isOf(Blocks.REDSTONE_TORCH))
            {
                if (sideCoversSmallSquare(world, blockPos.south().west().down(), Direction.EAST) || sideCoversSmallSquare(world, blockPos.south().east().down(), Direction.WEST))
                {
                    return blockPos.south();
                }
            }
        }


        return null;
    }

    public static BlockPos findPossibleSlimeBlockPos(ClientWorld world, BlockPos blockPos, Direction dir) {
        if (dir == Direction.UP)
        {
            if (world.getBlockState(blockPos.east()).getMaterial().isReplaceable() && (world.getBlockState(blockPos.east().up()).getMaterial().isReplaceable())) {
                return blockPos.east();
            } else if (world.getBlockState(blockPos.west()).getMaterial().isReplaceable() && (world.getBlockState(blockPos.west().up()).getMaterial().isReplaceable())) {
                return blockPos.west();
            } else if (world.getBlockState(blockPos.south()).getMaterial().isReplaceable() && (world.getBlockState(blockPos.south().up()).getMaterial().isReplaceable())) {
                return blockPos.south();
            } else if (world.getBlockState(blockPos.north()).getMaterial().isReplaceable() && (world.getBlockState(blockPos.north().up()).getMaterial().isReplaceable())) {
                return blockPos.north();
            }
        }
        else if (dir == Direction.EAST || dir == Direction.WEST || dir == Direction.NORTH || dir == Direction.SOUTH)
        {
            if (world.getBlockState(blockPos.up()).getMaterial().isReplaceable() && (world.getBlockState(blockPos.up().offset(dir)).getMaterial().isReplaceable())) {
                return blockPos.up();
            } else if (world.getBlockState(blockPos.down()).getMaterial().isReplaceable() && (world.getBlockState(blockPos.down().offset(dir)).getMaterial().isReplaceable())) {
                return blockPos.down();
            } else if (world.getBlockState(blockPos.offset(dir.rotateYClockwise())).getMaterial().isReplaceable() && (world.getBlockState(blockPos.offset(dir.rotateYClockwise()).offset(dir)).getMaterial().isReplaceable())) {
                return blockPos.offset(dir.rotateYClockwise());
            } else if (world.getBlockState(blockPos.offset(dir.rotateYClockwise().rotateYClockwise().rotateYClockwise())).getMaterial().isReplaceable() && (world.getBlockState(blockPos.offset(dir.rotateYClockwise().rotateYClockwise().rotateYClockwise()).offset(dir)).getMaterial().isReplaceable())) {
                return blockPos.offset(dir.rotateYClockwise().rotateYClockwise().rotateYClockwise());
            }
        }
        else if(dir == Direction.DOWN)
        {
            if (world.getBlockState(blockPos.east().north().down()).getMaterial().isReplaceable())
            {
                if (world.getBlockState(blockPos.east().down()).getMaterial().isReplaceable() || world.getBlockState(blockPos.north().down()).getMaterial().isReplaceable())
                {
                    return blockPos.east().north().down();
                }
            }
            else if (world.getBlockState(blockPos.east().south().down()).getMaterial().isReplaceable())
            {
                if (world.getBlockState(blockPos.east().down()).getMaterial().isReplaceable() || world.getBlockState(blockPos.south().down()).getMaterial().isReplaceable())
                {
                    return blockPos.east().south().down();
                }
            }
            else if (world.getBlockState(blockPos.west().north().down()).getMaterial().isReplaceable())
            {
                if (world.getBlockState(blockPos.west().down()).getMaterial().isReplaceable() || world.getBlockState(blockPos.north().down()).getMaterial().isReplaceable())
                {
                    return blockPos.west().north().down();
                }
            }
            else if (world.getBlockState(blockPos.west().south().down()).getMaterial().isReplaceable())
            {
                if (world.getBlockState(blockPos.west().down()).getMaterial().isReplaceable() || world.getBlockState(blockPos.south().down()).getMaterial().isReplaceable())
                {
                    return blockPos.west().south().down();
                }
            }
        }
        return null;
    }

    public static Direction wherePlaceTorch(ClientWorld world, BlockPos redstoneTorchPos, Direction dir)
    {
        if (dir == Direction.UP)
        {
            return Direction.UP;
        }
        else if (dir == Direction.EAST || dir == Direction.WEST || dir == Direction.NORTH || dir == Direction.SOUTH)
        {
            return dir;
        }
        else if (dir == Direction.DOWN)
        {
            if(sideCoversSmallSquare(world, redstoneTorchPos.north(), Direction.SOUTH) && !world.getBlockState(redstoneTorchPos.north()).isOf(Blocks.PISTON))
            {

            }
        }
        return null;
    }

    public static boolean has2BlocksOfPlaceToPlacePiston(ClientWorld world, BlockPos blockPos) {
        if (world.getBlockState(blockPos.up()).getHardness(world, blockPos.up()) == 0) {
            BlockBreaker.breakBlock(world, blockPos.up());
        }
        return world.getBlockState(blockPos.up()).getMaterial().isReplaceable() && world.getBlockState(blockPos.up().up()).getMaterial().isReplaceable();
    }

    public static ArrayList<BlockPos> findNearbyRedstoneTorch(ClientWorld world, BlockPos pistonBlockPos) {
        ArrayList<BlockPos> list = new ArrayList<>();
        if (world.getBlockState(pistonBlockPos.east()).isOf(Blocks.REDSTONE_TORCH)) {
            list.add(pistonBlockPos.east());
        }
        if (world.getBlockState(pistonBlockPos.west()).isOf(Blocks.REDSTONE_TORCH)) {
            list.add(pistonBlockPos.west());
        }
        if (world.getBlockState(pistonBlockPos.south()).isOf(Blocks.REDSTONE_TORCH)) {
            list.add(pistonBlockPos.south());
        }
        if (world.getBlockState(pistonBlockPos.north()).isOf(Blocks.REDSTONE_TORCH)) {
            list.add(pistonBlockPos.north());
        }
        return list;
    }
}
