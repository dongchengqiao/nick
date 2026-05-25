package com.dongchengqiao.nick;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class NickConfig {
	private static final Path CONFIG_PATH = Path.of("config/nick.json");
	private static final Map<String, String> data = new HashMap<>();
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	public static void load() {
		try {
			if (Files.exists(CONFIG_PATH)) {
				JsonObject obj = GSON.fromJson(Files.readString(CONFIG_PATH), JsonObject.class);
				if (obj != null) {
					for (String key : obj.keySet()) {
						String nick = obj.getAsJsonObject(key).get("nick").getAsString();
						data.put(key, nick);
					}
				}
			}
		} catch (Exception e) {
			Nick.LOGGER.error("Failed to load nick config", e);
		}
	}

	public static void save() {
		try {
			Files.createDirectories(CONFIG_PATH.getParent());
			JsonObject obj = new JsonObject();
			for (var entry : data.entrySet()) {
				JsonObject inner = new JsonObject();
				inner.addProperty("nick", entry.getValue());
				obj.add(entry.getKey(), inner);
			}
			Files.writeString(CONFIG_PATH, GSON.toJson(obj));
		} catch (Exception e) {
			Nick.LOGGER.error("Failed to save nick config", e);
		}
	}

	public static String getNick(String username) {
		return data.get(username);
	}

	public static void setNick(String username, String nick) {
		data.put(username, nick);
		save();
	}

	public static void removeNick(String username) {
		data.remove(username);
		save();
	}

	public static String getPlayerByNick(String nick) {
		for (var entry : data.entrySet()) {
			if (entry.getValue().equals(nick)) {
				return entry.getKey();
			}
		}
		return null;
	}
}
