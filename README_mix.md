# Nick - 昵称模组 / Nickname Mod

[中文](README.md) | [English](README_en.md) | **混合 mix**

一个基于 Carpet 的 Fabric 昵称模组，支持中文/Unicode昵称、多语言翻译、Carpet规则实时切换。
A Carpet-based Fabric nickname mod supporting Unicode names, multi-language translations, and real-time rule toggling.

## 前置 / Dependencies

- Fabric Loader ≥0.19.5
- Fabric API ≥0.149.0+26.1.2（请使用对应游戏版本的 Fabric API / use the Fabric API for your game version）
- **Carpet** (任何版本 any version)

---

## 命令 / Commands

### 设置昵称 / Set Nickname

| 命令 / Command                              | 权限 / Permission | 说明 / Description                             |
| ------------------------------------------- | ----------------- | ---------------------------------------------- |
| `/nick set <昵称/nickname>`               | 所有人 Everyone   | 设置自己的昵称 Set your own nickname           |
| `/nick set <目标/target> <昵称/nickname>` | OP                | 设置他人的昵称 Set another player's nickname   |
| `/nick reset`                             | 所有人 Everyone   | 重置自己的昵称 Reset your own nickname         |
| `/nick reset <目标/target>`               | OP                | 重置他人的昵称 Reset another player's nickname |

- 昵称直接填写，无需引号，支持中文/日文/韩文等 Unicode
- 无须引号 Unicode names without quotes
- `<目标/target>` 支持玩家名、选择器 Supports player names and selectors

---

## Carpet 规则 / Carpet Rules

| 规则 Rule               | 默认 Default | 说明 Description                                     |
| ----------------------- | ------------ | ----------------------------------------------------- |
| `commandNick`             | `false`      | 启用/禁用 `/nick` 命令  Toggle `/nick` command                     |
| `unicodeArgumentsSupport` | `false`      | 允许命令参数使用非ASCII字符 Allow non-ASCII command arguments      |
| `commandPlayerCNNoSpawn`  | `false`      | `/player` 中文假名不生成实体 Don't spawn entity                    |

- `/nick` 需要 `commandNick = true`
- 规则即时生效，无需重启 Rules take effect immediately

---

## 效果 / Features

| 功能 Feature                                                                                                    | 状态 Status |
| --------------------------------------------------------------------------------------------------------------- | ----------- |
| 聊天消息显示昵称（所有客户端可见）Chat messages display nickname (all clients)                                   | ✅          |
| Tab 列表显示昵称 Tab list displays nickname                                                                     | ✅          |
| 头顶名签显示昵称（服务端原生，无需客户端安装）Name tag displays nickname (server-side, no client mod required)    | ✅          |
| 客户端可选安装，提供三种显示模式 Optional client mod with three display modes                                    | ✅          |
| 昵称查找玩家 Lookup players by nickname                                                                          | ✅          |
| 支持中文/Unicode Unicode support                                                                                | ✅          |

---

## 安装 / Installation

### 服务端 / Server

1. 放入 `mods/`（需要 `fabric-carpet-*.jar`）Place into `mods/` (requires `fabric-carpet-*.jar`)
2. 重启服务器 Restart the server

### 客户端（可选）/ Client (Optional)

安装后可用 ModMenu 配置显示模式，不安装则所有昵称效果仍完全可见。
When installed, use ModMenu to configure display modes. Without it, all nickname effects remain fully visible.

---

## 客户端配置 / Client Configuration

按 `ModMenu` → `Nick` 配置 / Open `ModMenu` → `Nick` to configure:

| 模式 Mode              | 效果 Effect                         | 示例 Example    |
| ---------------------- | ----------------------------------- |-----------------|
| `nick_only`            | 仅显示昵称（默认）Show nickname only | `脏小豆`        |
| `nick_and_original`    | 昵称+MCID Nickname + MCID           | `[脏小豆]zxdnb` |
| `hide`                 | 隐藏昵称，显示原名 Show original    | `zxdnb`         |

---

## 服务器配置 / Server Config

`config/nick.json`:

```json
{
  "zxdnb": {"nick": "脏小豆"},
  "dongchengqiao": {"nick": "董丞乔"}
}
```

---

## 技术信息 / Technical Info

- Minecraft 版本 Version: 26.1 – 26.4（26.4 为快照 snapshot-5，请下载对应版本文件 / download the matching file）
- 框架 Framework: Fabric Loader 0.19.5 / Fabric API 0.149.0+26.1.2 – 0.164.1+26.4
- 硬依赖 Hard dependency: Carpet（翻译、规则系统 / translation & rule system）
- 主类 Main class: `com.dongchengqiao.nick.Nick`
