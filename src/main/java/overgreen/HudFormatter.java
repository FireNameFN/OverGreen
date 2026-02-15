package overgreen;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.entity.Entity;

public final class HudFormatter {
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
            case "x" -> new DoubleReplacer(format, entity -> entity.getX());
            case "y" -> new DoubleReplacer(format, entity -> entity.getY());
            case "z" -> new DoubleReplacer(format, entity -> entity.getZ());
            case "bx" -> (builder, entity) -> builder.append(entity.getBlockX());
            case "by" -> (builder, entity) -> builder.append(entity.getBlockY());
            case "bz" -> (builder, entity) -> builder.append(entity.getBlockZ());
            case "dir" -> (builder, entity) -> builder.append(entity.getDirection().getName());
            case "day" -> (builder, entity) -> builder.append(entity.level().getDayTime() / 24000 + 1);
            case "nl" -> new LineReplacer();
            default -> null;
        };
    }

    public void format(List<String> list, Entity entity) {
        for(Replacer[] lineReplacers : replacers) {
            for(Replacer replacer : lineReplacers)
                replacer.replace(builder, entity);

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
        public void replace(StringBuilder builder, Entity entity) {
            builder.append(str);
        }
    }

    private static class DoubleReplacer implements Replacer {
        private final NumberFormat format;

        private final RawDoubleReplacer replacer;

        public DoubleReplacer(NumberFormat format, RawDoubleReplacer replacer) {
            this.format = format;
            this.replacer = replacer;
        }

        public void replace(StringBuilder builder, Entity entity) {
            builder.append(format.format(replacer.replace(entity)));
        }

        public static interface RawDoubleReplacer {
            public double replace(Entity entity);
        }
    }

    private static class LineReplacer implements Replacer {
        public void replace(StringBuilder builder, Entity entity) { }
    }

    private static interface Replacer {
        public void replace(StringBuilder builder, Entity entity);
    }
}
