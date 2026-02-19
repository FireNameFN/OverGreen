package overgreen;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.biome.Biome;

final class HudFormatter {
    private final StringBuilder builder = new StringBuilder();

    private Replacer[][] replacers;

    public void updateFormat(String format) {
        ArrayList<Replacer[]> lists = new ArrayList<>();

        ArrayList<Replacer> list = new ArrayList<>();

        int position = 0;

        while(true) {
            int index = format.indexOf('{', position);

            if(index < 0)
                break;

            int closeIndex = format.indexOf('}', index + 1);

            if(closeIndex < 0)
                break;

            addFlat(list, format, position, index);

            Replacer replacer = getReplacer(format, format.substring(index + 1, closeIndex));

            closeIndex++;

            if(replacer instanceof LineReplacer) {
                lists.add(list.toArray(Replacer[]::new));

                list = new ArrayList<>();
            } else if(replacer == null)
                addFlat(list, format, position, closeIndex);
            else
                list.add(replacer);

            position = closeIndex;
        }

        addFlat(list, format, position, format.length());

        if(list.size() > 0)
            lists.add(list.toArray(Replacer[]::new));

        replacers = lists.toArray(Replacer[][]::new);
    }

    private static void addFlat(ArrayList<Replacer> list, String str, int start, int end) {
        if(start >= end)
            return;

        list.add(new FlatReplacer(str.substring(start, end)));
    }

    private static Replacer getReplacer(String str, String key) {
        NumberFormat format = NumberFormat.getIntegerInstance();

        int index = key.indexOf(':');

        if(index >= 0) {
            format = new DecimalFormat(key.substring(index + 1));

            key = key.substring(0, index);
        }

        return switch(key) {
            case "fps" -> (builder, entity, reduced) -> builder.append(Minecraft.getInstance().getFps());
            case "ping" -> (builder, entity, reduced) -> {
                ClientPacketListener listener = Minecraft.getInstance().getConnection();

                PlayerInfo player = listener.getPlayerInfo(listener.getLocalGameProfile().id());

                if(player == null) {
                    builder.append('-');
                    return;
                }

                builder.append(player.getLatency());
            };
            case "x" -> new ReducedDoubleReplacer(format, entity -> entity.getX());
            case "y" -> new ReducedDoubleReplacer(format, entity -> entity.getY());
            case "z" -> new ReducedDoubleReplacer(format, entity -> entity.getZ());
            case "bx" -> (ReducedReplacer)(builder, entity) -> builder.append(entity.getBlockX());
            case "by" -> (ReducedReplacer)(builder, entity) -> builder.append(entity.getBlockY());
            case "bz" -> (ReducedReplacer)(builder, entity) -> builder.append(entity.getBlockZ());
            case "dimension" -> (ReducedReplacer)(builder, entity) -> append(builder, entity.level().dimension().identifier());
            case "biome" -> (ReducedReplacer)(builder, entity) -> {
                Optional<ResourceKey<Biome>> resourceKey = entity.level().getBiome(entity.blockPosition()).unwrapKey();

                if(resourceKey.isEmpty()) {
                    builder.append('-');
                    return;
                }

                append(builder, resourceKey.get().identifier());
            };
            case "dir" -> (Replacer)(builder, entity, reduced) -> builder.append(entity.getDirection().getName());
            case "day" -> (Replacer)(builder, entity, reduced) -> builder.append(entity.level().getDayTime() / 24000 + 1);
            case "nl" -> new LineReplacer();
            default -> null;
        };
    }

    private static void append(StringBuilder builder, Identifier identifier) {
        builder.append(identifier.getNamespace());
        builder.append(':');
        builder.append(identifier.getPath());
    }

    public void format(List<String> list) {
        Minecraft minecraft = Minecraft.getInstance();

        Entity entity = minecraft.getCameraEntity();

        boolean reduced = minecraft.showOnlyReducedInfo();

        for(Replacer[] lineReplacers : replacers) {
            for(Replacer replacer : lineReplacers)
                replacer.replace(builder, entity, reduced);

            list.add(builder.toString());

            builder.setLength(0);
        }
    }

    private static final class FlatReplacer implements Replacer {
        private final String str;

        public FlatReplacer(String str) {
            this.str = str;
        }

        @Override
        public void replace(StringBuilder builder, Entity entity, boolean reduced) {
            builder.append(str);
        }
    }

    private static final class ReducedDoubleReplacer implements ReducedReplacer {
        private final NumberFormat format;

        private final RawDoubleReplacer replacer;

        public ReducedDoubleReplacer(NumberFormat format, RawDoubleReplacer replacer) {
            this.format = format;
            this.replacer = replacer;
        }

        @Override
        public void replace(StringBuilder builder, Entity entity) {
            builder.append(format.format(replacer.replace(entity)));
        }

        public static interface RawDoubleReplacer {
            public double replace(Entity entity);
        }
    }

    private static interface ReducedReplacer extends Replacer {
        @Override
        public default void replace(StringBuilder builder, Entity entity, boolean reduced) {
            if(reduced) {
                builder.append('-');
                return;
            }

            replace(builder, entity);
        }

        public void replace(StringBuilder builder, Entity entity);
    }

    private static final class LineReplacer implements Replacer {
        public void replace(StringBuilder builder, Entity entity, boolean reduced) { }
    }

    private static interface Replacer {
        public void replace(StringBuilder builder, Entity entity, boolean reduced);
    }
}
