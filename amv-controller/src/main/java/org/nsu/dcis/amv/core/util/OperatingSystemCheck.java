package org.nsu.dcis.amv.core.util;
/**
 * This is utility class for checking Operating System.
 *
 * Modification Log:
 *
 * Date        Modifier             Description
 * ----------  -------------------  --------------------------------------------
 * 5/25/2017   J. Jorgensen         Initial version.
 */
public class OperatingSystemCheck {
	private static String OS = System.getProperty("os.name").toLowerCase();

	public static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}

	public static boolean isMac() {
		return (OS.indexOf("mac") >= 0);
	}

	public static boolean isUnix() {
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 ||
				OS.indexOf("aix") > 0 );
	}

	public static boolean isSolaris() {
		return (OS.indexOf("sunos") >= 0);
	}

	public static String getOs() {
		return OS;
	}
}