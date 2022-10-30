package io.mkrzywanski.gpn.scrapper.domain.steam;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

class JsonMappingErrorHandlingDeserializer<T> extends JsonDeserializer<T> {
    private final StdDeserializer<T> delegate;

    JsonMappingErrorHandlingDeserializer(final StdDeserializer<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public T deserialize(final JsonParser jp, final DeserializationContext ctxt) throws IOException {
        try {
            return delegate.deserialize(jp, ctxt);
        } catch (final JsonMappingException e) {
            // If a JSON Mapping occurs, simply returning null instead of blocking things
            return null;
        }
    }
}
