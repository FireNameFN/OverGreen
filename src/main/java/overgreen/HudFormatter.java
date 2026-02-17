package overgreen;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.world.entity.Entity;

final class HudFormatter {
    private final StringBuilder builder = new StringBuilder();

    private Replacer[][] replacers;

    public void updateFormat(String format) {
        ArrayList<ArrayList<Replacer>> lists = new ArrayList<>();

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
                lists.add(list);

                list = new ArrayList<>();
            } else if(replacer == null)
                addFlat(list, format, position, closeIndex);
            else
                list.add(replacer);

            position = closeIndex;
        }

        addFlat(list, format, position, format.length());

        if(list.size() > 0)
            lists.add(list);

        Replacer[][] array = new Replacer[lists.size()][];

        for(int i = 0; i < array.length; i++)
            array[i] = lists.get(i).toArray(Replacer[]::new);

        replacers = array;
    }

    private static void addFlat(ArrayList<Replacer> list, String str, int start, int end) {
        if(start >= end)
            return;

        list.add(new FlatReplacer(str.substring(start, end)));
    }

    private static final Replacer getReplacer(String str, String key) {
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
            case "dir" -> (Replacer)(builder, entity, reduced) -> builder.append(entity.getDirection().getName());
            case "day" -> (Replacer)(builder, entity, reduced) -> builder.append(entity.level().getDayTime() / 24000 + 1);
            case "nl" -> new LineReplacer();
            default -> null;
        };
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

    private static class FlatReplacer implements Replacer {
        private final String str;

        public FlatReplacer(String str) {
            this.str = str;
        }

        @Override
        public void replace(StringBuilder builder, Entity entity, boolean reduced) {
            builder.append(str);
        }
    }

    private static class ReducedDoubleReplacer implements ReducedReplacer {
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

    private static class LineReplacer implements Replacer {
        public void replace(StringBuilder builder, Entity entity, boolean reduced) { }
    }

    private static interface Replacer {
        public void replace(StringBuilder builder, Entity entity, boolean reduced);
    }
}
