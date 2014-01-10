package com.test.api;

import fitnesse.components.CommandRunner;
import java.net.InetAddress;
import java.text.DateFormat;
import java.util.Date;

public class GitCmSystem {
    static String MACHINE_NAME = null;
    static String COMMIT_TOKEN = null;
    static String path = "/usr/bin/git";

    public static void cmUpdate(String file, String payload) throws Exception {
        getPath();
        execute("cmUpdate", path + " add " + file);
        execute("cmDelete", path + " commit -m Updated-" + freshToken() + "-" + file);
    }

    public static void cmEdit(String file, String payload) {
    }

    public static void cmDelete(String file, String payload) throws Exception {
        getPath();
        execute("cmDelete", path + " rm -rf --cached " + file);
        execute("cmDelete", path + " commit -m Removed-" + freshToken() + "-" + file);
    }

    public static void cmPreDelete(String file, String payload) throws Exception {
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

    protected static String commitMessage() {
        try {
            if (MACHINE_NAME == null) MACHINE_NAME = InetAddress.getLocalHost().getHostName();
            if (COMMIT_TOKEN == null) COMMIT_TOKEN = freshToken();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "Committed from " + MACHINE_NAME + " with token [" + COMMIT_TOKEN + "]";
    }

    private static String freshToken() {
        return DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(new Date());
    }

    private static void getPath() throws Exception {
        CommandRunner runner = new CommandRunner("which git", "");
        runner.run();
        path = runner.getOutput();
        path = path.trim();
    }
}