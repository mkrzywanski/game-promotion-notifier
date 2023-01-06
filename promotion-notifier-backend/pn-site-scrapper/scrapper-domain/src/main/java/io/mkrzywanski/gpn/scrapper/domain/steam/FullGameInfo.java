package io.mkrzywanski.gpn.scrapper.domain.steam;

record FullGameInfo(Integer gameId, String gameName, GameInfo gameInfo) {
    boolean isSuccess() {
        return gameInfo().isSuccess();
    }

    boolean isOnSale() {
        return gameInfo.isOnSale();
    }
}
