package com.test.api;

import fitnesse.components.CommandRunner;
import fitnesse.ComponentFactory;

import java.io.FileInputStream;
import java.net.InetAddress;
import java.text.DateFormat;
import java.util.Date;
import java.util.Properties;

public class GitCmSystem {
    private static String MACHINE_NAME = null;
    private static String COMMIT_TOKEN = null;
    private static String path;
    private static String gitdir;
    private static String workdir;
    private static boolean pullmode;

    public static void cmUpdate(String file, String payload) throws Exception {
        // Config
        config();
        getPath();
        // Add the changes to Git
        execute("cmUpdate", path + " --git-dir=" + gitdir + " --work-tree=" + workdir + " add " + file);
        execute("cmDelete", path + " --git-dir=" + gitdir + " --work-tree=" + workdir + " commit -m Updated-" + freshToken().replaceAll(" ", "-") + "-" + file);
    }

    // Commands for when the user hits the edit button
    public static void cmEdit(String file, String payload) throws Exception {
        // Config
        config();
        getPath();
        // If it is in pull mode pull before every edit
        if(pullmode) {
            execute("cmEdit", path + " --git-dir=" + gitdir + " --work-tree=" + workdir + " pull --rebase");
        }
    }

    // Commands for when a page is deleted
    public static void cmDelete(String file, String payload) throws Exception {
        // Configs
        config();
        getPath();
        // Remove the files for Git
        execute("cmDelete", path + " --git-dir=" + gitdir + " --work-tree=" + workdir + " rm -rf --cached " + file);
        execute("cmDelete", path + " --git-dir=" + gitdir + " --work-tree=" + workdir + " commit -m Removed-" + freshToken().replaceAll(" ", "-") + "]-" + file);
    }

    public static void cmPreDelete(String file, String payload) throws Exception {
    }

    // Run bash command
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
        return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM).format(new Date());
    }

    private static void getPath() throws Exception {
        CommandRunner runner = new CommandRunner("which git", "");
        runner.run();
        path = runner.getOutput();
        path = path.trim();
    }

    private static void config() {
        try {
            // Read the plugins.properties
            FileInputStream inputStream = new FileInputStream(ComponentFactory.PROPERTIES_FILE);
            Properties properties = new Properties();
            properties.load(inputStream);
            inputStream.close();

            // Look for the git.path property
            path = properties.getProperty("git.path");
            if (path == null) {
                path = "/usr/bin/git";
            }

            // Specify working directory
            workdir = properties.getProperty("git.workdir");
            gitdir =  workdir + "/.git";

            pullmode = Boolean.getBoolean(properties.getProperty("git.pullmode"));
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}