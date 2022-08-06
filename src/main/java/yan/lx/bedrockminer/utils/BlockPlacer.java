package yan.lx.bedrockminer.utils;

import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class BlockPlacer {
    public static void simpleBlockPlacement(BlockPos pos, ItemConvertible item) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();

        InventoryManager.switchToItem(item);
        BlockHitResult hitResult = new BlockHitResult(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), Direction.UP, pos, false);
        placeBlockWithoutInteractingBlock(minecraftClient, hitResult);
    }

    public static void advancedPlacement(BlockPos pos, Direction placeDirection, Direction blockSideDirection, ItemConvertible item) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        double x = pos.getX();

        //Directly issue the packet to change the perspective of the player entity on the server side
                PlayerEntity player = minecraftClient.player;
                float pitch;
                float yaw;
                switch (placeDirection) {
                    case UP:
                        pitch = 90f;
                        yaw = 0f;
                        break;
                    case DOWN:
                        pitch = -90f;
                        yaw = 0f;
                        break;
                    case EAST:
                        pitch = 0f;
                        yaw = 90f;
                        break;
                    case WEST:
                        pitch = 0f;
                        yaw = -90f;
                        break;
                    case NORTH:
                        pitch = 0f;
                        yaw = 0f;
                        break;
                    case SOUTH:
                        pitch = 0f;
                        yaw = -180f;
                        break;
                    default:
                        pitch = 90f;
                        yaw = 0f;
                        break;
                }

                System.out.println(yaw + " " + pitch);

                minecraftClient.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(yaw, pitch, player.isOnGround()));

        Vec3d vec3d = new Vec3d(x, pos.getY(), pos.getZ());

        InventoryManager.switchToItem(item);
        BlockHitResult hitResult = new BlockHitResult(vec3d, blockSideDirection, pos, false);
        placeBlockWithoutInteractingBlock(minecraftClient, hitResult);
    }

    private static void placeBlockWithoutInteractingBlock(MinecraftClient minecraftClient, BlockHitResult hitResult) {
        ClientPlayerEntity player = minecraftClient.player;
        ItemStack itemStack = player.getStackInHand(Hand.MAIN_HAND);

        minecraftClient.getNetworkHandler().sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, hitResult));

        if (!itemStack.isEmpty() && !player.getItemCooldownManager().isCoolingDown(itemStack.getItem())) {
            ItemUsageContext itemUsageContext = new ItemUsageContext(player, Hand.MAIN_HAND, hitResult);
            itemStack.useOnBlock(itemUsageContext);

        }
    }
}
