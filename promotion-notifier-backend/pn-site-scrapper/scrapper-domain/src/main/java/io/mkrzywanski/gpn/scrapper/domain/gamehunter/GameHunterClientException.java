package io.mkrzywanski.gpn.scrapper.domain.gamehunter;

class GameHunterClientException extends Exception {
    GameHunterClientException(final String message) {
        super(message);
    }

    GameHunterClientException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
