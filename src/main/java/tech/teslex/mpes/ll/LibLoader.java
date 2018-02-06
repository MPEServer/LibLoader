package tech.teslex.mpes.ll;

import cn.nukkit.Server;
import cn.nukkit.plugin.PluginBase;
import tech.teslex.mpes.ll.commands.LibCommand;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.List;

public class LibLoader extends PluginBase {

	private static List<Library> libraries = new LinkedList<>();

	public static List<Library> getLibraries() {
		return libraries;
	}

	private void loadLib(Library library) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, URISyntaxException, MalformedURLException {

		URLClassLoader loader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		URL url = library.file().toURI().toURL();

		for (URL l : loader.getURLs())
			if (l.equals(url))
				return;

		Server.getInstance().getLogger().info("Loading lib: " + library.file().getName());

		Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
		method.setAccessible(true);
		method.invoke(loader, url);

		libraries.add(library);

	}

	private void loadAllInDir(File dir) throws URISyntaxException, NoSuchMethodException, MalformedURLException, IllegalAccessException, InvocationTargetException {

		if (dir.isDirectory()) {
			File[] libs = dir.listFiles((d, name) -> name.endsWith(".jar"));

			assert libs != null;
			for (File file : libs) {
				if (file.isFile()) {
					loadLib(() -> file);
				}
			}
		}

	}

	@Override
	public void onLoad() {

		File libs = new File(getServer().getDataPath() + File.separator + "libraries");
		libs.mkdirs();

		try {
			loadAllInDir(libs);
		} catch (URISyntaxException | NoSuchMethodException | MalformedURLException | InvocationTargetException | IllegalAccessException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onEnable() {
		getServer().getCommandMap().register("libs", new LibCommand());
	}
}
