package tech.teslex.mpes.ll.commands;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import tech.teslex.mpes.ll.LibLoader;
import tech.teslex.mpes.ll.Library;

import java.util.List;

public class LibCommand extends Command {

	public LibCommand() {
		super("libs", "show loaded libs", "/libs", new String[]{});
	}

	public boolean execute(CommandSender commandSender, String s, String[] strings) {
		String libStr = libsToStr(LibLoader.getLibraries());
		commandSender.sendMessage("Loaded " + LibLoader.getLibraries().size() + (LibLoader.getLibraries().size() == 1 ? " lib" : " libs"));
		commandSender.sendMessage(libStr);

		return false;
	}

	private String libsToStr(List<Library> libs) {
		StringBuilder libsStr = new StringBuilder();

		for (Library lib : libs) {
			libsStr.append(lib.file().getName()).append(", ");
		}

		return libsStr.length() < 3 ? libsStr.toString() : libsStr.substring(0, libsStr.length() - 2);
	}
}
