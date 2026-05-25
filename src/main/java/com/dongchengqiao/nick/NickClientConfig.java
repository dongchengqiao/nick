package com.dongchengqiao.nick;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.nio.file.Files;
import java.nio.file.Path;

public class NickClientConfig {
	public enum DisplayMode {
		NICK_ONLY("nick_only"),
		NICK_AND_ORIGINAL("nick_and_original"),
		HIDE("hide");

		private final String key;

		DisplayMode(String key) {
			this.key = key;
		}

		public static DisplayMode fromKey(String key) {
			for (DisplayMode mode : values()) {
				if (mode.key.equals(key)) {
					return mode;
				}
			}
			return NICK_ONLY;
		}

		public String getKey() {
			return key;
		}

		public String getDisplayName() {
			switch (this) {
				case NICK_ONLY:
					return "仅昵称";
				case NICK_AND_ORIGINAL:
					return "昵称+原名";
				case HIDE:
					return "隐藏";
				default:
					return key;
			}
		}
	}

	private static final Path CONFIG_PATH = Path.of("config/nick-client.json");
	private static DisplayMode displayMode = DisplayMode.NICK_ONLY;
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	public static void load() {
		try {
			if (Files.exists(CONFIG_PATH)) {
				JsonObject obj = GSON.fromJson(Files.readString(CONFIG_PATH), JsonObject.class);
				if (obj != null && obj.has("displayMode")) {
					displayMode = DisplayMode.fromKey(obj.get("displayMode").getAsString());
				}
			} else {
				save();
			}
		} catch (Exception e) {
			Nick.LOGGER.error("Failed to load nick client config", e);
		}
	}

	public static DisplayMode getDisplayMode() {
		return displayMode;
	}

	public static void setDisplayMode(DisplayMode mode) {
		displayMode = mode;
		save();
	}

	public static void save() {
		try {
			Files.createDirectories(CONFIG_PATH.getParent());
			JsonObject obj = new JsonObject();
			obj.addProperty("displayMode", displayMode.getKey());
			Files.writeString(CONFIG_PATH, GSON.toJson(obj));
		} catch (Exception e) {
			Nick.LOGGER.error("Failed to save nick client config", e);
		}
	}
}
