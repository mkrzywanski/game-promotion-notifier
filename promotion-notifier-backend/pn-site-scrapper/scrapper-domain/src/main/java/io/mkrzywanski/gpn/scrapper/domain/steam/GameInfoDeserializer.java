package io.mkrzywanski.gpn.scrapper.domain.steam;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

/**
 * Since steam api sometimes returns json array in "data" field instead of object, this serializer is used
 * to detect such cases and skip them as failures.
 */
class GameInfoDeserializer extends StdDeserializer<GameInfo> {
    protected GameInfoDeserializer() {
        super(GameInfo.class);
    }

    @Override
    public GameInfo deserialize(final JsonParser parser, final DeserializationContext ctxt) throws IOException {

        final var gameDataBuilder = new GameInfo.Builder();
        while (!parser.isClosed()) {
            final JsonToken jsonToken = parser.nextToken();

            if (JsonToken.FIELD_NAME.equals(jsonToken)) {
                final String fieldName = parser.getCurrentName();

                parser.nextToken();

                if ("success".equals(fieldName)) {
                    gameDataBuilder.withSuccessValue(parser.getBooleanValue());
                } else if ("data".equals(fieldName)) {
                    handleDataFieldDeserialization(parser, gameDataBuilder);
                }
            }
        }
        return gameDataBuilder.build();
    }

    private void handleDataFieldDeserialization(final JsonParser parser, final GameInfo.Builder gameData) throws IOException {
        final boolean isNotArrayStart = !JsonToken.START_ARRAY.equals(parser.currentToken());
        if (isNotArrayStart) {
            gameData.withGameData(parser.readValueAs(GameData.class));
        } else {
            gameData.withFailure();
        }
    }
}
