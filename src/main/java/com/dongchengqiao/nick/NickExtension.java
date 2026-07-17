package com.dongchengqiao.nick;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.api.settings.SettingsManager;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;

public class NickExtension implements CarpetExtension {
	@Override
	public void onGameStarted() {
		CarpetServer.settingsManager.parseSettingsClass(NickSettings.class);
		SettingsManager.registerGlobalRuleObserver((source, rule, newValue) -> {
			if (rule.name().equals("commandNick") && source != null && source.getServer() != null) {
				for (ServerPlayer p : source.getServer().getPlayerList().getPlayers()) {
					source.getServer().getCommands().sendCommands(p);
				}
			}
		});
	}

	@Override
	public void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
		NickCommand.registerCommands(dispatcher);
	}

	@Override
	public Map<String, String> canHasTranslations(String lang) {
		return switch (lang) {
			case "zh_cn" -> Map.ofEntries(
				Map.entry("carpet.category.NICK", "Nick"),
				Map.entry("carpet.rule.commandNick.name", "昵称命令"),
				Map.entry("carpet.rule.commandNick.desc", "启用或禁用 /nick 命令"),
				Map.entry("carpet.rule.commandNick.extra", "启用或禁用 /nick 命令"),
				Map.entry("carpet.rule.commandPlayerCN.name", "/player支持中文"),
				Map.entry("carpet.rule.commandPlayerCN.desc", "启用后 /player 命令可使用中文玩家名"),
				Map.entry("carpet.rule.commandPlayerCN.extra", "启用后 /player 命令可使用中文玩家名"),
				Map.entry("carpet.rule.commandPlayerCNNoSpawn.name", "禁止非标准名称假人生成"),
				Map.entry("carpet.rule.commandPlayerCNNoSpawn.desc", "启用后 /player xxx spawn 拒绝含非字母、数字和下划线的名称"),
				Map.entry("carpet.rule.commandPlayerCNNoSpawn.extra", "启用后 /player xxx spawn 拒绝含非字母、数字和下划线的名称"),
				Map.entry("nick.command.set", "§a昵称已设置为: %s"),
				Map.entry("nick.command.set.other", "§a已设置 %s 的昵称为: %s"),
				Map.entry("nick.command.reset", "§a昵称已重置"),
				Map.entry("nick.command.reset.other", "§a已重置 %s 的昵称")
			);
			case "zh_tw" -> Map.ofEntries(
				Map.entry("carpet.category.NICK", "Nick"),
				Map.entry("carpet.rule.commandNick.name", "暱稱指令"),
				Map.entry("carpet.rule.commandNick.desc", "啟用或停用 /nick 指令"),
				Map.entry("carpet.rule.commandNick.extra", "啟用或停用 /nick 指令"),
				Map.entry("carpet.rule.commandPlayerCN.name", "/player支援中文"),
				Map.entry("carpet.rule.commandPlayerCN.desc", "啟用後 /player 指令可使用中文玩家名"),
				Map.entry("carpet.rule.commandPlayerCN.extra", "啟用後 /player 指令可使用中文玩家名"),
				Map.entry("carpet.rule.commandPlayerCNNoSpawn.name", "禁止非標準名稱假人生成"),
				Map.entry("carpet.rule.commandPlayerCNNoSpawn.desc", "啟用後 /player xxx spawn 拒絕含非字母、數字和底線的名稱"),
				Map.entry("carpet.rule.commandPlayerCNNoSpawn.extra", "啟用後 /player xxx spawn 拒絕含非字母、數字和底線的名稱"),
				Map.entry("nick.command.set", "§a暱稱已設為: %s"),
				Map.entry("nick.command.set.other", "§a已將 %s 的暱稱設為: %s"),
				Map.entry("nick.command.reset", "§a暱稱已重設"),
				Map.entry("nick.command.reset.other", "§a已重設 %s 的暱稱")
			);
			case "es_ar" -> Map.ofEntries(
				Map.entry("carpet.category.NICK", "Nick"),
				Map.entry("carpet.rule.commandNick.name", "Comando Nick"),
				Map.entry("carpet.rule.commandNick.desc", "Activar o desactivar el comando /nick"),
				Map.entry("carpet.rule.commandNick.extra", "Activar o desactivar el comando /nick"),
				Map.entry("carpet.rule.commandPlayerCN.name", "/player Soporte Chino"),
				Map.entry("carpet.rule.commandPlayerCN.desc", "Permite nombres chinos en /player"),
				Map.entry("carpet.rule.commandPlayerCN.extra", "Permite nombres chinos en /player"),
				Map.entry("carpet.rule.commandPlayerCNNoSpawn.name", "Sin Fake Player no Estándar"),
				Map.entry("carpet.rule.commandPlayerCNNoSpawn.desc", "Evita /player xxx spawn con nombres que contengan caracteres no alfanuméricos (excepto guión bajo)"),
				Map.entry("carpet.rule.commandPlayerCNNoSpawn.extra", "Evita /player xxx spawn con nombres que contengan caracteres no alfanuméricos (excepto guión bajo)"),
				Map.entry("nick.command.set", "§aNick establecido a: %s"),
				Map.entry("nick.command.set.other", "§aNick de %s establecido a: %s"),
				Map.entry("nick.command.reset", "§aNick reiniciado"),
				Map.entry("nick.command.reset.other", "§aNick de %s reiniciado")
			);
			case "fr_fr" -> Map.ofEntries(
				Map.entry("carpet.category.NICK", "Nick"),
				Map.entry("carpet.rule.commandNick.name", "Commande Nick"),
				Map.entry("carpet.rule.commandNick.desc", "Activer ou désactiver la commande /nick"),
				Map.entry("carpet.rule.commandNick.extra", "Activer ou désactiver la commande /nick"),
				Map.entry("carpet.rule.commandPlayerCN.name", "/player Support Chinois"),
				Map.entry("carpet.rule.commandPlayerCN.desc", "Permet les noms chinois dans /player"),
				Map.entry("carpet.rule.commandPlayerCN.extra", "Permet les noms chinois dans /player"),
				Map.entry("carpet.rule.commandPlayerCNNoSpawn.name", "Pas de PNJ non standard"),
				Map.entry("carpet.rule.commandPlayerCNNoSpawn.desc", "Empêche /player xxx spawn avec des noms contenant des caractères non alphanumériques (sauf trait de soulignement)"),
				Map.entry("carpet.rule.commandPlayerCNNoSpawn.extra", "Empêche /player xxx spawn avec des noms contenant des caractères non alphanumériques (sauf trait de soulignement)"),
				Map.entry("nick.command.set", "§aPseudo défini sur: %s"),
				Map.entry("nick.command.set.other", "§aPseudo de %s défini sur: %s"),
				Map.entry("nick.command.reset", "§aPseudo réinitialisé"),
				Map.entry("nick.command.reset.other", "§aPseudo de %s réinitialisé")
			);
			case "pt_br" -> Map.ofEntries(
				Map.entry("carpet.category.NICK", "Nick"),
				Map.entry("carpet.rule.commandNick.name", "Comando Nick"),
				Map.entry("carpet.rule.commandNick.desc", "Ativar ou desativar o comando /nick"),
				Map.entry("carpet.rule.commandNick.extra", "Ativar ou desativar o comando /nick"),
				Map.entry("carpet.rule.commandPlayerCN.name", "/player Suporte Chinês"),
				Map.entry("carpet.rule.commandPlayerCN.desc", "Permite nomes chineses em /player"),
				Map.entry("carpet.rule.commandPlayerCN.extra", "Permite nomes chineses em /player"),
				Map.entry("carpet.rule.commandPlayerCNNoSpawn.name", "Sem Fake Player não Padrão"),
				Map.entry("carpet.rule.commandPlayerCNNoSpawn.desc", "Impede /player xxx spawn com nomes contendo caracteres não alfanuméricos (exceto sublinhado)"),
				Map.entry("carpet.rule.commandPlayerCNNoSpawn.extra", "Impede /player xxx spawn com nomes contendo caracteres não alfanuméricos (exceto sublinhado)"),
				Map.entry("nick.command.set", "§aApelido definido para: %s"),
				Map.entry("nick.command.set.other", "§aApelido de %s definido para: %s"),
				Map.entry("nick.command.reset", "§aApelido redefinido"),
				Map.entry("nick.command.reset.other", "§aApelido de %s redefinido")
			);
			default -> Map.ofEntries(
				Map.entry("carpet.category.NICK", "Nick"),
				Map.entry("carpet.rule.commandNick.name", "Nick Command"),
				Map.entry("carpet.rule.commandNick.desc", "Enable or disable the /nick command"),
				Map.entry("carpet.rule.commandNick.extra", "Enable or disable the /nick command"),
				Map.entry("carpet.rule.commandPlayerCN.name", "/player Chinese Support"),
				Map.entry("carpet.rule.commandPlayerCN.desc", "Allows Chinese player names in /player command"),
				Map.entry("carpet.rule.commandPlayerCN.extra", "Allows Chinese player names in /player command"),
				Map.entry("carpet.rule.commandPlayerCNNoSpawn.name", "No Non-Standard Fake Player Spawn"),
				Map.entry("carpet.rule.commandPlayerCNNoSpawn.desc", "Prevents /player xxx spawn with names containing non-alphanumeric characters (except underscores)"),
				Map.entry("carpet.rule.commandPlayerCNNoSpawn.extra", "Prevents /player xxx spawn with names containing non-alphanumeric characters (except underscores)"),
				Map.entry("nick.command.set", "§aNickname set to: %s"),
				Map.entry("nick.command.set.other", "§aSet %s's nickname to: %s"),
				Map.entry("nick.command.reset", "§aNickname reset"),
				Map.entry("nick.command.reset.other", "§aReset %s's nickname")
			);
		};
	}

	@Override
	public String version() {
		return "nick";
	}
}
