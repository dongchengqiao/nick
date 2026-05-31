# Nick - Nickname Mod

[中文](README.md) | **English** | [混合 mix](README_mix.md)

[![Modrinth](https://img.shields.io/modrinth/dt/VtG7yP1S?label=Modrinth%20Downloads)](https://modrinth.com/mod/nick-mod)

> This mod is AI-generated (DeepSeek V4 via opencode)

A Fabric server-side mod that allows players to change their display name (nickname), supports team colors, prefixes/suffixes, and allows player lookup by nickname.

## Commands

### Set Nickname

| Command                           | Permission | Description                     |
| --------------------------------- | ---------- | ------------------------------- |
| `/nick set <nickname>`          | Everyone   | Set your own nickname           |
| `/nick set <target> <nickname>` | OP         | Set another player's nickname   |
| `/nick reset`                   | Everyone   | Reset your own nickname         |
| `/nick reset <target>`          | OP         | Reset another player's nickname |

`<target>` supports player names, `@p`, `@a`, `@r`, `@s` and other selectors.

### Nickname Lookup

Once a nickname is set, all commands can use the nickname in place of the player name, e.g.:

- `/tp Xiaoming`
- `/msg Xiaoming`
- `/kick Xiaoming`

**Note**: If a nickname and a real player name are identical, the real name takes priority.

### Nickname Format

- Single word: `/nick set Xiaoming`
- Nickname with spaces, use quotes: `/nick set "Xiao Ming"`
- Displays team color, prefix, and suffix when applicable

## Features

- ✅ Chat messages display nickname
- ✅ Tab list displays nickname (all clients, no mod required)
- ✅ Name tag displays nickname (singleplayer) / requires client mod (server mode)
- ✅ Per-location client display modes: default, nickname only, nickname+original, hide
- ✅ Team colors apply
- ✅ Lookup players by nickname (`/tp`, `/msg`, `/kick`, `@e[name=nickname]`, tab-completion)
- ✅ Server console/logs show original MCID (MCDR compatible)
- ✅ MCDR compatibility: command output and system messages always use original MCID

## Installation

### Server

1. Place `nick-*.jar` into the `mods/` directory
2. Restart the server

### Client (Optional)

When installed on the client, name tags display nicknames in server mode, and the client configuration screen becomes available to customize display per location. Without the client mod, chat and tab list still show nicknames.

## Client Configuration

When the client mod is installed, `config/nick-client.json` is auto-generated. It supports a hierarchical display configuration: a global default mode plus per-location overrides.

### Display Modes

| Mode                    | Effect                       | Example              |
| ----------------------- | ---------------------------- | -------------------- |
| `"nick_only"`         | Show nickname only (default) | `Xiao Ming`        |
| `"nick_and_original"` | Nickname + original name     | `[Xiao Ming]zxdnb` |
| `"hide"`              | Hide nickname, show original | `zxdnb`            |

### Locations

| Location            | Effect                        |
| ------------------- | ----------------------------- |
| `NAMETAG`         | Overhead name tag display     |
| `CHAT`            | Chat message sender display   |
| `TARGET_SELECTOR` | Selector resolution display   |
| `TAB_LIST`        | Tab list player entry display |

## Server Config

`config/nick.json`:

```json
{
  "zxdnb": {"nick": "Xiao Ming"},
  "dongchengqiao": {"nick": "Dong Chengqiao"}
}
```

## Technical Info

- Minecraft version: 26.1
- Framework: Fabric Loader 0.19.2 / Fabric API 0.149.0+26.1.2
- Maven group: `com.dongchengqiao.nick`
- Main class: `com.dongchengqiao.nick.Nick`
