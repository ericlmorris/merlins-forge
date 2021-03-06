package commands;

import java.io.File;
import java.io.IOException;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import configuration.SchematicPathConfiguration;
import events.EventPlayerTickLoad;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import schematics.StructureLoad;

public class CommandLoadSchematic {
	
	public static ITextComponent textComponent = null;

	static ArgumentBuilder<CommandSource, ?> register() {

		return Commands.literal("load").then(Commands.argument("schematic", StringArgumentType.word())
				.executes(context -> execute(context, StringArgumentType.getString(context, "schematic"))));
	}

	private static int execute(final CommandContext<CommandSource> context, final String name)
			throws CommandSyntaxException {

		textComponent = new TranslationTextComponent("Loading: " + name);
		textComponent.getStyle().setColor(TextFormatting.WHITE);
		EventPlayerTickLoad.thePlayer.sendMessage(textComponent);
		
		loadSchematic(name);
		
		return 0;
	}

	public static void loadSchematic(String sName) {

		String myFile = SchematicPathConfiguration.SCHEMATIC_PATH.get() + "schematics/" + sName + ".schematic";		
		String passFile = sName;
		File fileName = new File(myFile);
		try {
			//SchematicLoad.loadSchematic(passFile, fileName, "00", EventPlayerTickLoad.thePlayer, EventPlayerTickLoad.tickWorld);
			StructureLoad.loadSchematic(passFile, fileName, "00", EventPlayerTickLoad.thePlayer, EventPlayerTickLoad.tickWorld);

		} catch (IOException e) {
			textComponent = new TranslationTextComponent("Schematic not found! Check your config file.");
			textComponent.getStyle().setColor(TextFormatting.RED);
			EventPlayerTickLoad.thePlayer.sendMessage(textComponent);
			e.printStackTrace();
		}

	}

}
