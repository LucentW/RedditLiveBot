package me.stuntguy3000.java.redditlivebot;

import lombok.Getter;
import me.stuntguy3000.java.redditlivebot.handler.CommandHandler;
import me.stuntguy3000.java.redditlivebot.handler.ConfigHandler;
import me.stuntguy3000.java.redditlivebot.handler.LogHandler;
import me.stuntguy3000.java.redditlivebot.hook.TelegramHook;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

// @author Luke Anderson | stuntguy3000
public class RedditLiveBot {
    public static int BUILD = 0;
    public static boolean DEV_MODE = false;
    @Getter
    public static RedditLiveBot instance;
    @Getter
    private CommandHandler commandHandler;
    @Getter
    private ConfigHandler configHandler;

    private void connectTelegram() {
        LogHandler.log("Connecting to Telegram...");
        DEV_MODE = getConfigHandler().getBotSettings().getDevMode();
        LogHandler.log("Developer Mode is set to " + DEV_MODE);
        new TelegramHook(configHandler.getBotSettings().getTelegramKey(), this);
    }

    private void main() {
        instance = this;
        configHandler = new ConfigHandler();

        /**
         * Initialize Build Number
         */
        File build = new File("build");

        if (!build.exists()) {
            try {
                build.createNewFile();
                PrintWriter writer = new PrintWriter(build, "UTF-8");
                writer.print(0);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            BUILD = Integer.parseInt(FileUtils.readFileToString(build));
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * Begin CLI for RedditLive
         */
        LogHandler.log("======================================");
        LogHandler.log(" RedditLive build " + BUILD + " by @stuntguy3000");
        LogHandler.log("======================================");

        commandHandler = new CommandHandler();

        connectTelegram();
    }

    public static void main(String[] args) {
        new RedditLiveBot().main();
    }
}
    