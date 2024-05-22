package me.redstoner2019.events;

import me.redstoner2019.server.ODClientHandler;

public interface ClientConnectEvent {
    void onEvent(ODClientHandler handler);
}
