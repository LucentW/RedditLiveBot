package me.stuntguy3000.java.redditlivebot.handler;

import java.util.ArrayList;

import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.object.config.Subscriber;
import me.stuntguy3000.java.redditlivebot.scheduler.ForwardMessageTask;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.message.Message;
import pro.zackpollard.telegrambot.api.user.User;

/**
 * Handles Subscriptions
 *
 * @author stunt3000
 */
public class SubscriptionHandler {
    private RedditLiveBot plugin;

    /**
     * Create a new SubscriptionHandler instance
     */
    public SubscriptionHandler() {
        this.plugin = RedditLiveBot.instance;
    }

    /**
     * Returns a list of all Subscribers
     *
     * @return ArrayList of all subscribers
     */
    public ArrayList<Subscriber> getSubscriptions() {
        return plugin.getConfigHandler().getSubscriptions().getSubscriptions();
    }

    /**
     * Returns if a Chat is subscribed
     *
     * @param chat Chat the specified chat
     *
     * @return true if chat is subscribed
     */
    public boolean isSubscribed(Chat chat) {
        return isSubscribed(chat.getId());
    }

    /**
     * Subscribes a chat
     *
     * @param chat Chat the chat to be subscribed
     */
    public void subscribeChat(Chat chat) {
        if (!isSubscribed(chat)) {
            plugin.getConfigHandler().getSubscriptions().getSubscriptions().add(new Subscriber(chat.getId(), chat.getName()));
            plugin.getConfigHandler().saveSubscriptions();
        }
    }

    /**
     * Subscribe a user
     *
     * @param user User the user to be subscribed
     */
    public void subscribeUser(User user) {
        if (!isSubscribed(String.valueOf(user.getId()))) {
            plugin.getConfigHandler().getSubscriptions().getSubscriptions().add(new Subscriber(String.valueOf(user.getId()), user.getUsername()));
            plugin.getConfigHandler().saveSubscriptions();
        }
    }

    /**
     * Returns if a user ID is subscribed
     *
     * @param id String the ID to be checked
     * @return true if id is subscribed
     */
    private boolean isSubscribed(String id) {
        for (Subscriber subscriber : getSubscriptions()) {
            if (subscriber.getUserID().equals(id)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Forward a Message to a subscriber
     * <p>@RedditLiveBot must be in the chat</p>
     *
     * @param message Message the message to forward
     */
    public void forwardMessage(Message message) {
        ThreadExecutionHandler threadExecutionHandler = RedditLiveBot.instance.getThreadExecutionHandler();

        for (Subscriber subscriber : getSubscriptions()) {
            threadExecutionHandler.queue(new ForwardMessageTask(message, subscriber.getUserID()));
        }
    }

    /**
     * Unsubscribe a chat ID
     *
     * @param id String the chat ID to unsubscribe
     */
    public void unsubscribeChat(String id) {
        for (Subscriber subscriber : new ArrayList<>(getSubscriptions())) {
            if (subscriber.getUserID().equals(id)) {
                getSubscriptions().remove(subscriber);
            }
        }

        plugin.getConfigHandler().saveSubscriptions();
    }
}
    