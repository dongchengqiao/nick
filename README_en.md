# Nick - Nickname Mod

[中文](README.md) | **English**

A Carpet-based Fabric nickname mod supporting Unicode names, multi-language translations, and real-time rule toggling.

## Dependencies

- Fabric Loader ≥0.19.2
- Fabric API ≥0.149.0+26.1.2
- **Carpet** (any version)

## Commands

| Command                           | Permission | Description                   |
| --------------------------------- | ---------- | ----------------------------- |
| `/nick set <nickname>`          | Everyone   | Set your own nickname         |
| `/nick set <target> <nickname>` | OP         | Set another player's nickname |
| `/nick reset`                   | Everyone   | Reset your own nickname       |
| `/nick reset <target>`          | OP         | Reset another player's nickname |

- Unicode names (Chinese, Japanese, Korean, etc.) work without quotes
- `<target>` supports player names and selectors

## Carpet Rules

| Rule                   | Default | Description                              |
| ---------------------- | ------- | ---------------------------------------- |
| `commandNick`          | `true`  | Enable/disable `/nick` command           |
| `commandPlayerCN`      | `true`  | Allow Chinese names in `/player` command |
| `commandPlayerCNNoSpawn` | `true`| `/player` Chinese names don't spawn entity |

- `/nick` requires `commandNick = true`
- Rules take effect immediately, no restart needed

## Features

- ✅ Chat messages display nickname (visible to all clients)
- ✅ Tab list displays nickname
- ✅ **Name tag displays nickname (server-side, no client mod required)**
- ✅ Player lookup by nickname
- ✅ Unicode support
- ✅ Optional client mod with three display modes

## Installation

### Server

1. Place `nick-*.jar` into `mods/` (requires `fabric-carpet-*.jar`)
2. Restart the server

### Client (Optional)

When installed, use ModMenu to configure display modes per location (nickname only / nickname+MCID / MCID only). Without the client mod, all nickname effects are still fully visible.

## Client Configuration

Open `ModMenu` → `Nick` to configure per-location display:

| Mode                   | Effect                      | Example              |
| ---------------------- | --------------------------- | -------------------- |
| `nick_only`            | Show nickname only (default)| `Xiao Ming`          |
| `nick_and_original`    | Nickname + MCID             | `[Xiao Ming]zxdnb`   |
| `hide`                 | Show original name only     | `zxdnb`              |

## Server Config

`config/nick.json`:

```json
{
  "zxdnb": {"nick": "UBB owner"},
  "dongchengqiao": {"nick": "mod owner"}
}
```

## Technical Info

- Minecraft version: 26.1
- Framework: Fabric Loader 0.19.2 / Fabric API 0.149.0+26.1.2
- Hard dependency: Carpet (translation & rule system)
- Main class: `com.dongchengqiao.nick.Nick`
