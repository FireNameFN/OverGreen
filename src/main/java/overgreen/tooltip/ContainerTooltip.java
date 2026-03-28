package overgreen.tooltip;

import java.util.List;
import java.util.Optional;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStackTemplate;

public record ContainerTooltip(List<Optional<ItemStackTemplate>> contents) implements TooltipComponent { }
