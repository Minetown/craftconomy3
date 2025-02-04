/**
 * This file is part of Craftconomy3.
 *
 * Copyright (c) 2011-2016, Greatman <http://github.com/greatman/>
 * Copyright (c) 2016-2017, Aztorius <http://github.com/Aztorius/>
 * Copyright (c) 2018, Pavog <http://github.com/pavog/>
 *
 * Craftconomy3 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Craftconomy3 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Craftconomy3.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.greatmancode.tools.caller.sponge;

import com.greatmancode.tools.commands.CommandSender;
import com.greatmancode.tools.commands.PlayerCommandSender;
import com.greatmancode.tools.interfaces.SpongeLoader;
import com.greatmancode.tools.interfaces.caller.PlayerCaller;
import com.greatmancode.tools.interfaces.caller.ServerCaller;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.util.Identifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpongePlayerCaller extends PlayerCaller {

    private SpongeLoader loader;

    public SpongePlayerCaller(ServerCaller caller) {
        super(caller);

        loader = ((SpongeLoader) caller.getLoader());
    }

    @Override
    public boolean checkPermission(String playerName, String perm) {
        if (playerName.equals("console")) {
            return true;
        }
        UUID uuid = getUUID(playerName);
        return checkPermission(uuid, perm);
    }

    @Override
    public boolean checkPermission(UUID uuid, String perm) {
        Optional<PermissionService> permissionService = loader.getGame().getServiceManager().provide(PermissionService.class);

        // Return if we do not have a permission service
        if (!permissionService.isPresent())
            return false;

        Optional<Subject> subject = permissionService.get().getUserSubjects().getSubject(uuid.toString());

        // Return if we do not have the player for the uuid
        if (!subject.isPresent())
            return false;

        return subject.get().hasPermission(perm);
    }

    @Override
    public void sendMessage(String playerName, String message) {
        if ("console".equals(playerName)) {
            caller.getLogger().info(message);
            return;
        }
        loader.getGame().getServer().getPlayer(playerName).get().sendMessage(((SpongeServerCaller)getCaller()).addColorSponge(message));
    }

    @Override
    public void sendMessage(UUID uuid, String message) {
        loader.getGame().getServer().getPlayer(uuid).get().sendMessage(((SpongeServerCaller)getCaller()).addColorSponge(message));
    }

    /**
     * Sends a message to a player
     * @param playerName The player name to send the message
     * @param message    The message to send
     * @param commandName the command that started the mes
     */
    @Override
    public void sendMessage(String playerName, String message, String commandName) {
        loader.getGame().getServer().getPlayer(playerName).get().sendMessage(((SpongeServerCaller)getCaller()).addColorSponge(message));
    }

    /**
     * Sends a message to a sender
     * @param sender the sender
     * @param message the message
     * @param command the initial command
     */
    @Override
    public void sendMessage(CommandSender sender, String message, String command) {
        // The commandsender can only be a player or the console
        if (sender instanceof PlayerCommandSender) {
            PlayerCommandSender playerCommandSender = (PlayerCommandSender) sender;
            UUID uuid = playerCommandSender.getUuid();
            loader.getGame().getServer().getPlayer(uuid).get().sendMessage(((SpongeServerCaller)getCaller()).addColorSponge(message));
        } else {
            loader.getGame().getServer().getConsole().sendMessage(((SpongeServerCaller)getCaller()).addColorSponge(message));
        }

    }

    /**
     * Sends a message to a player
     * @param commandName the command that started the mes
     * @param uuid The player name to send the message
     * @param message    The message to send
     */
    @Override
    public void sendMessage(UUID uuid, String message, String commandName) {
        loader.getGame().getServer().getPlayer(uuid).get().sendMessage(((SpongeServerCaller)getCaller()).addColorSponge(message));
    }

    @Override
    public String getPlayerWorld(String playerName) {
        return loader.getGame().getServer().getPlayer(playerName).get().getWorld().getName();
    }

    @Override
    public String getPlayerWorld(UUID uuid) {
        return loader.getGame().getServer().getPlayer(uuid).get().getWorld().getName();
    }

    @Override
    public boolean isOnline(String playerName) {
        return loader.getGame().getServer().getPlayer(playerName).isPresent();
    }

    @Override
    public boolean isOnline(UUID uuid) {
        return false;
    }

    @Override
    public List<String> getOnlinePlayers() {
        List<String> playerList = new ArrayList<>();
        for (Player p : loader.getGame().getServer().getOnlinePlayers()) {
            playerList.add(p.getName());
        }
        return playerList;
    }

    @Override
    public List<UUID> getUUIDsOnlinePlayers() {
        return null;
    }

    @Override
    public boolean isOp(String playerName) {
        return false;
    }

    @Override
    public boolean isOP(UUID uuid) {
        return false;
    }

    @Override
    public UUID getUUID(String playerName) {
        Optional<Player> result = loader.getGame().getServer().getPlayer(playerName);
        return result.map(Identifiable::getUniqueId).orElse(null);
    }

    @Override
    public String getPlayerName(UUID uuid) {
        return null;
    }

    @Override
    public com.greatmancode.tools.entities.Player getPlayer(UUID uuid) {
        Optional<Player> result = loader.getGame().getServer().getPlayer(uuid);
        return result.map(player -> new com.greatmancode.tools.entities.Player(player.getName(), player.getDisplayNameData().displayName().toString(), player.getWorld().getName(), player.getUniqueId())).orElse(null);
    }
    
    @Override
    public com.greatmancode.tools.entities.Player getOnlinePlayer(String name) {
        Optional<Player> result = loader.getGame().getServer().getPlayer(name);
        if (result.isPresent() && result.get().isOnline()) {
            return result.map(player ->
                    new com.greatmancode.tools.entities.Player(player.getName(), player.getDisplayNameData().displayName().toString(), player.getWorld().getName(), player.getUniqueId())).orElse(null);
        }
        return null;
    }
    
    @Override
    public com.greatmancode.tools.entities.Player getOnlinePlayer(UUID uuid) {
        Optional<Player> result = loader.getGame().getServer().getPlayer(uuid);
        if (result.isPresent() && result.get().isOnline()) {
            return result.map(player ->
                    new com.greatmancode.tools.entities.Player(player.getName(), player.getDisplayNameData().displayName().toString(), player.getWorld().getName(), player.getUniqueId())).orElse(null);
        }
        return null;
    }
}
