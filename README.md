# Nick - 昵称模组

**中文** | [English](README_en.md)

一个基于 Carpet 的 Fabric 昵称模组，支持中文/Unicode昵称、多语言翻译、Carpet规则实时切换。

## 前置

- Fabric Loader ≥0.19.2
- Fabric API ≥0.149.0+26.1.2
- **Carpet** (任何版本)

## 命令

| 命令                        | 权限   | 说明           |
| --------------------------- | ------ | -------------- |
| `/nick set <昵称>`          | 所有人 | 设置自己的昵称 |
| `/nick set <目标> <昵称>`   | OP     | 设置他人的昵称 |
| `/nick reset`               | 所有人 | 重置自己的昵称 |
| `/nick reset <目标>`        | OP     | 重置他人的昵称 |

- 昵称直接填写即可，无需引号，支持中文/日文/韩文等Unicode字符
- `<目标>` 支持玩家名、选择器

## Carpet 规则

| 规则                    | 默认值  | 说明                              |
| ----------------------- | ------- | --------------------------------- |
| `commandNick`              | `false` | 启用/禁用 `/nick` 命令                        |
| `unicodeArgumentsSupport`  | `false` | 允许命令参数使用非ASCII字符（中文、日文、韩文等） |
| `commandPlayerCNNoSpawn`   | `false` | `/player` 中文假人名不生成实体                |

- 使用 `/nick` 命令需要 `commandNick = true`
- `unicodeArgumentsSupport` 开启后 `/player spawn 小明` 等命令参数可直接使用中文
- 规则即时生效，无需重启

## 效果

- ✅ 聊天消息显示昵称（所有客户端可见）
- ✅ Tab 列表显示昵称
- ✅ **头顶名签显示昵称（服务端原生，无需客户端安装）**
- ✅ 昵称查找玩家
- ✅ 支持中文/Unicode
- ✅ 客户端可选安装，提供三种显示模式

## 安装

### 服务端

1. 放入 `mods/`（需要 `fabric-carpet-*.jar`）
2. 重启服务器

### 客户端（可选）

安装后可用 ModMenu 配置显示模式（仅昵称 / 昵称+MCID / 仅MCID），不安装则所有昵称效果仍完全可见。

## 客户端配置

按 `ModMenu` → `Nick` 进入配置界面，可分别设置头顶、聊天、Tab列表的显示方式：

| 模式                    | 效果               | 示例              |
| ----------------------- | ------------------ | ----------------- |
| `nick_only`             | 仅显示昵称（默认） | `脏小豆`          |
| `nick_and_original`     | 昵称+MCID          | `[脏小豆]zxdnb`   |
| `hide`                  | 隐藏昵称，显示原名 | `zxdnb`           |

## 配置文件

`config/nick.json`：

```json
{
  "zxdnb": {"nick": "脏小豆"},
  "dongchengqiao": {"nick": "董丞乔"}
}
```

## 技术信息

- Minecraft 版本：26.1
- 框架：Fabric Loader 0.19.2 / Fabric API 0.149.0+26.1.2
- 硬依赖：Carpet（翻译、规则系统）
- 主类：`com.dongchengqiao.nick.Nick`
