package ead.editor;

import ead.utils.i18n.I18N;
import java.util.Set;
import java.util.TreeSet;

/**
 * Resource index for this package (statically compiled).
 *
 * This is an AUTOMATICALLY-GENERATED file - 
 * Run class ead.utils.i18n.ResourceCreator with 
 * paramenters: "./core/editor-core/impl ead.editor"
 * where "./core/editor-core/impl" is the project location
 * and "ead.editor" is the name of the package
 * to re-create or update this class
 */
public class R {

	public static class Drawable {
		public static String EditorIcon128x128_png;
		public static String EditorIcon16x16_png;
		public static String EditorIcon32x32_png;
		public static String EditorIcon64x64_png;
		public static String SplashScreenLogo_png;
		public static String addNode_png;
		public static String deleteNode_png;
		public static String duplicateNode_png;
		public static String edit_png;
		public static String information_png;
		public static String loading_png;

		static {
			Set<String> files = new TreeSet<String>();

			files.add("EditorIcon128x128.png");
			files.add("EditorIcon16x16.png");
			files.add("EditorIcon32x32.png");
			files.add("EditorIcon64x64.png");
			files.add("SplashScreenLogo.png");
			files.add("addNode.png");
			files.add("conditions/flag16.png");
			files.add("conditions/flags.png");
			files.add("conditions/group-2.png");
			files.add("conditions/notOff.png");
			files.add("conditions/notOn.png");
			files.add("conditions/var16.png");
			files.add("conditions/vars.png");
			files.add("deleteNode.png");
			files.add("duplicateNode.png");
			files.add("edit.png");
			files.add("information.png");
			files.add("loading.png");

			I18N.initializeResources(Drawable.class.getName(), Drawable.class, files);
		}
	}
}

