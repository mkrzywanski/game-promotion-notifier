package io.mkrzywanski.gpn.scrapper.domain.steam;

record GameInfo(boolean success, GameData data) {
    boolean isOnSale() {
        return data.priceInfo().discount_percent() > 0;
    }

    boolean isSuccess() {
        return this.success;
    }

    static class Builder {
        private boolean success;
        private GameData gameData = GameData.empty();

        Builder withSuccessValue(final boolean success) {
            this.success = success;
            return this;
        }

        Builder withSuccess() {
            this.success = true;
            return this;
        }

        Builder withFailure() {
            this.success = false;
            return this;
        }

        Builder withGameData(final GameData gameData) {
            this.gameData = gameData;
            return this;
        }

        GameInfo build() {
            return new GameInfo(success, gameData);
        }
    }
}
