package com.dongchengqiao.nick;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class NickClientConfig {
	public enum DisplayMode {
		DEFAULT("default", "默认"),
		NICK_ONLY("nick_only", "仅昵称"),
		NICK_AND_ORIGINAL("nick_and_original", "昵称+MCID"),
		HIDE("hide", "仅MCID");

		private final String key;
		private final String displayName;

		DisplayMode(String key, String displayName) {
			this.key = key;
			this.displayName = displayName;
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
			return displayName;
		}

		public DisplayMode resolve(DisplayMode fallback) {
			return this == DEFAULT ? fallback : this;
		}
	}

	public enum DisplayLocation {
		NAMETAG("nametag", "\u5934\u9876"),
		CHAT("chat", "\u804a\u5929"),
		TARGET_SELECTOR("target_selector", "\u76ee\u6807\u9009\u62e9\u5668"),
		TAB_LIST("tab_list", "Tab\u5217\u8868");

		private final String key;
		private final String displayName;

		DisplayLocation(String key, String displayName) {
			this.key = key;
			this.displayName = displayName;
		}

		public String getKey() {
			return key;
		}

		public String getDisplayName() {
			return displayName;
		}

		public static DisplayLocation fromKey(String key) {
			for (DisplayLocation loc : values()) {
				if (loc.key.equals(key)) {
					return loc;
				}
			}
			return NAMETAG;
		}
	}

	private static final Path CONFIG_PATH = Path.of("config/nick-client.json");
	private static DisplayMode defaultMode = DisplayMode.NICK_ONLY;
	private static final Map<DisplayLocation, DisplayMode> overrides = new LinkedHashMap<>();
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	public static void load() {
		try {
			if (Files.exists(CONFIG_PATH)) {
				JsonObject obj = GSON.fromJson(Files.readString(CONFIG_PATH), JsonObject.class);
				if (obj != null) {
					if (obj.has("default")) {
						defaultMode = DisplayMode.fromKey(obj.get("default").getAsString());
					}
					for (DisplayLocation loc : DisplayLocation.values()) {
						if (obj.has(loc.getKey())) {
							overrides.put(loc, DisplayMode.fromKey(obj.get(loc.getKey()).getAsString()));
						}
					}
				}
			} else {
				save();
			}
		} catch (Exception e) {
			Nick.LOGGER.error("Failed to load nick client config", e);
		}
	}

	public static DisplayMode getDefaultMode() {
		return defaultMode;
	}

	public static void setDefaultMode(DisplayMode mode) {
		defaultMode = mode;
		save();
	}

	public static DisplayMode getDisplayMode(DisplayLocation location) {
		DisplayMode override = overrides.get(location);
		if (override != null) {
			return override.resolve(defaultMode);
		}
		return defaultMode;
	}

	public static DisplayMode getOverride(DisplayLocation location) {
		return overrides.get(location);
	}

	public static void setOverride(DisplayLocation location, DisplayMode mode) {
		if (mode == DisplayMode.DEFAULT) {
			overrides.remove(location);
		} else {
			overrides.put(location, mode);
		}
		save();
	}

	public static void save() {
		try {
			Files.createDirectories(CONFIG_PATH.getParent());
			JsonObject obj = new JsonObject();
			obj.addProperty("default", defaultMode.getKey());
			for (Map.Entry<DisplayLocation, DisplayMode> entry : overrides.entrySet()) {
				obj.addProperty(entry.getKey().getKey(), entry.getValue().getKey());
			}
			Files.writeString(CONFIG_PATH, GSON.toJson(obj));
		} catch (Exception e) {
			Nick.LOGGER.error("Failed to save nick client config", e);
		}
	}
}
