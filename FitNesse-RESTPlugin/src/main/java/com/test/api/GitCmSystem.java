package com.test.api;

import fitnesse.components.CommandRunner;

public class GitCmSystem {
	static String path = "/usr/bin/git";
	static String wDir = "";

	public static void cmUpdate(String file, String payload) throws Exception {
		getPath();
		getDir();
		execute("cmUpdate",String.format("%s --git-dir=%s/.git --work-tree=%s add %s", path, wDir, wDir, file));
		execute("cmUpdate",String.format("%s --git-dir=%s/.git --work-tree=%s commit -m Updated-%s", path, wDir, wDir, file));
	}

	public static void cmEdit(String file, String payload) {
	}

	public static void cmDelete(String file, String payload) throws Exception {
		getPath();
		getDir();
		execute("cmDelete",String.format("%s --git-dir=%s/.git --work-tree=%s rm -rf --cached %s", path, wDir, wDir, file));
		execute("cmDelete",String.format("%s --git-dir=%s/.git --work-tree=%s commit -m Updated-%s", path, wDir, wDir, file));
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
	}
	
	private static void getDir() throws Exception {
		CommandRunner runner = new CommandRunner("locate -b fitnesseroot", "");
		runner.run();
		wDir = runner.getOutput();
	}
}