package com.test.api;

import fitnesse.components.CommandRunner;

public class GitCmSystem {
	static String path = "/usr/bin/git";
    static String wDir = "/Users/ssullivan/Git/fitnesseroot";

	public static void cmUpdate(String file, String payload) throws Exception {
		getPath();
		// getDir();
		execute("cmUpdate", path + " --git-dir=" + wDir + "/.git --work-tree=" + wDir + " add " + file);
		execute("cmUpdate", path + " --git-dir=" + wDir + "/.git --work-tree=" + wDir + " commit -m Updated-" + file);
	}

	public static void cmEdit(String file, String payload) {
	}

	public static void cmDelete(String file, String payload) throws Exception {
		getPath();
		// getDir();
		execute("cmDelete", path + " --git-dir=" + wDir + "/.git --work-tree=" + wDir + " rm -rf --cached " + file);
		execute("cmDelete", path + " --git-dir=" + wDir + "/.git --work-tree=" + wDir + "commit -m Updated-" + file);
	}

	public static void cmPreDelete(String file, String payload)
			throws Exception {
	}

	private static void execute(String method, String command) throws Exception {
		CommandRunner runner = new CommandRunner(command, "");
		runner.run();
		System.err.println(command);
		if (runner.getOutput().length() + runner.getError().length() > 0) {
			System.err.println(method + " command: " + command);
			System.err.println(method + " exit code: " + runner.getExitCode());
			System.err.println(method + " out:" + runner.getOutput());
			System.err.println(method + " err:" + runner.getError());
		}
	}

	private static void getPath() throws Exception {
		CommandRunner runner = new CommandRunner("which git", "");
		runner.run();
		path = runner.getOutput();
        path = path.trim();
	}
	
	private static void getDir() throws Exception {
		CommandRunner runner = new CommandRunner("find . -name fitnesseroot", "");
		runner.run();
		wDir = runner.getOutput();
        wDir = wDir.trim();
	}
}