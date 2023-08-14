package yan.lx.bedrockminer;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import yan.lx.bedrockminer.utils.BreakingFlowController;

public class MinerCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("miner3000")
                        .executes(context -> execute(context))
        );
    }

    private static int execute(CommandContext<ServerCommandSource> context) {
        BreakingFlowController.switchOnOff();
        return 0;
    }
}
