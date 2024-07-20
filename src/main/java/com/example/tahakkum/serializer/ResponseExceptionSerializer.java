package com.example.tahakkum.serializer;

import com.example.tahakkum.exception.ResponseException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

public class ResponseExceptionSerializer extends StdSerializer<ResponseException> {

    public ResponseExceptionSerializer() {
        this(null);
    }

    public ResponseExceptionSerializer(Class<ResponseException> t) {
        super(t);
    }

    @Override
    public void serialize(ResponseException value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("status", value.statusCode.value());
        gen.writeArrayFieldStart("errors");
        for (ResponseException.ErrorObject error : value.errors) {
            gen.writeStartObject();
            gen.writeStringField("message", error.message);
            gen.writeStringField("detail", error.detail);
            gen.writeEndObject();
        }
        gen.writeEndArray();
        gen.writeEndObject();
    }
}
