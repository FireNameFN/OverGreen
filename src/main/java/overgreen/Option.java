package overgreen;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

interface Option {
    public void write(JsonWriter writer) throws IOException;

    public void read(JsonReader reader) throws IOException;
}
