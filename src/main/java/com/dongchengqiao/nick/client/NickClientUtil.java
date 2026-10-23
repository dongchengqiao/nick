package com.dongchengqiao.nick.client;

import com.dongchengqiao.nick.NickClientConfig;
import com.dongchengqiao.nick.NickClientConfig.DisplayLocation;
import com.dongchengqiao.nick.NickClientConfig.DisplayMode;
import com.dongchengqiao.nick.mixin.PlayerInfoAccessor;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.PlayerTeam;

import java.util.Locale;
import java.util.UUID;

public final class NickClientUtil {
	private NickClientUtil() {
	}

	public static String getNickname(Player player) {
		Component customName = player.getCustomName();
		if (customName != null && !customName.getString().isEmpty()) {
			return customName.getString();
		}
		return getNicknameByUuid(player.getUUID());
	}

	public static String getNicknameByUuid(UUID uuid) {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.level != null) {
			for (Player player : minecraft.level.players()) {
				if (player.getUUID().equals(uuid)) {
					Component customName = player.getCustomName();
					if (customName != null && !customName.getString().isEmpty()) {
						return customName.getString();
					}
					break;
				}
			}
		}
		if (minecraft.getConnection() != null) {
			PlayerInfo info = minecraft.getConnection().getPlayerInfo(uuid);
			if (info != null) {
				Component displayName = ((PlayerInfoAccessor) info).nick$getRawTabListDisplayName();
				if (displayName != null) {
					String text = displayName.getString();
					String originalName = info.getProfile().name();
					boolean isOriginal = text.equalsIgnoreCase(originalName)
						|| text.toLowerCase(Locale.ROOT).contains(originalName.toLowerCase(Locale.ROOT));
					if (!text.isEmpty() && !isOriginal) {
						return text;
					}
				}
			}
		}
		return null;
	}

	public static Component formatWithTeam(PlayerTeam team, Component name) {
		if (team == null) return name;
		MutableComponent result = Component.empty();
		Component prefix = team.getPlayerPrefix();
		if (prefix != null) {
			result.append(prefix);
		}
		MutableComponent coloredName = name.copy();
		applyTeamColor(team, coloredName);
		result.append(coloredName);
		Component suffix = team.getPlayerSuffix();
		if (suffix != null) {
			result.append(suffix);
		}
		return result;
	}

	private static void applyTeamColor(PlayerTeam team, MutableComponent name) {
		try {
			Object color = PlayerTeam.class.getMethod("getColor").invoke(team);
			if (color instanceof ChatFormatting formatting) {
				if (formatting != ChatFormatting.RESET) {
					name.withStyle(formatting);
				}
				return;
			}
			if (color instanceof java.util.Optional<?> optional) {
				Object teamColor = optional.orElse(null);
				if (teamColor != null) {
					Object textColor = teamColor.getClass().getMethod("textColor").invoke(teamColor);
					if (textColor instanceof net.minecraft.network.chat.TextColor tc) {
						name.withStyle(style -> style.withColor(tc));
					}
				}
			}
		} catch (ReflectiveOperationException ignored) {
		}
	}

	public static Component rewriteSystemMessage(Component message) {
		DisplayMode mode = NickClientConfig.getDisplayMode(DisplayLocation.CHAT);
		if (mode != DisplayMode.HIDE && mode != DisplayMode.NICK_AND_ORIGINAL) {
			return message;
		}
		rewriteComponent(message, mode);
		return message;
	}

	private static void rewriteComponent(Component component, DisplayMode mode) {
		if (component.getContents() instanceof TranslatableContents translatable) {
			int senderIndex = senderIndex(translatable.getKey());
			Object[] args = translatable.getArgs();
			if (senderIndex >= 0 && senderIndex < args.length && args[senderIndex] instanceof Component sender) {
				PlayerInfo info = findSenderPlayer(sender);
				if (info != null) {
					String originalName = info.getProfile().name();
					String nickname = getNicknameByUuid(info.getProfile().id());
					Component displayName = null;
					if (mode == DisplayMode.HIDE) {
						displayName = Component.literal(originalName);
					} else if (nickname != null && !nickname.equals(originalName)) {
						displayName = Component.literal("[" + nickname + "]" + originalName);
					}
					if (displayName != null) {
						args[senderIndex] = formatWithTeam(info.getTeam(), displayName);
					}
				}
			}
			for (Object arg : args) {
				if (arg instanceof Component nested) {
					rewriteComponent(nested, mode);
				}
			}
		}
		for (Component sibling : component.getSiblings()) {
			rewriteComponent(sibling, mode);
		}
	}

	private static int senderIndex(String key) {
		return switch (key) {
			case "chat.type.text", "chat.type.emote", "chat.type.announcement",
				"commands.message.display.incoming" -> 0;
			case "chat.type.team.text", "chat.type.team.sent" -> 1;
			default -> -1;
		};
	}

	private static PlayerInfo findSenderPlayer(Component sender) {
		String text = sender.getString();
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.getConnection() == null) return null;
		for (PlayerInfo info : minecraft.getConnection().getOnlinePlayers()) {
			String nickname = getNicknameByUuid(info.getProfile().id());
			if (matchesPlayerName(text, nickname)) {
				return info;
			}
			if (matchesPlayerName(text, info.getProfile().name())) {
				return info;
			}
		}
		return null;
	}

	private static boolean matchesPlayerName(String text, String name) {
		if (name == null || name.isEmpty()) return false;
		return text.equals(name) || text.endsWith(name);
	}
}
