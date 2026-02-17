package overgreen;

import java.io.IOException;
import java.util.function.Supplier;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

final class OptionAdapter extends TypeAdapter<Option> {
    private final Supplier<Option> factory;

    public OptionAdapter(Supplier<Option> factory) {
        this.factory = factory;
    }

    @Override
    public void write(JsonWriter writer, Option value) throws IOException {
        value.write(writer);
    }

    @Override
    public Option read(JsonReader reader) throws IOException {
        Option option = factory.get();

        option.read(reader);

        return option;
    }
}
