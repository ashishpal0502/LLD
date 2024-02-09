package WhatsAppMessenger;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Represents a user in the system
class User {
    private int userId;
    private List<User> contacts;
    private boolean isOnline;
    private final Lock lock = new ReentrantLock();

    public User(int userId) {
        this.userId = userId;
        this.contacts = new ArrayList<>();
        this.isOnline = false;
    }

    public int getUserId() {
        return userId;
    }

    public List<User> getContacts() {
        return contacts;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public void addContact(User user) {
        lock.lock();
        try {
            contacts.add(user);
        } finally {
            lock.unlock();
        }
    }
}

// Represents a message exchanged between users
class Message {
    private User sender;
    private User receiver;
    private String content;
    private Date timestamp;

    public Message(User sender, User receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.timestamp = new Date();
    }

    // Getters for sender, receiver, content, timestamp
}

// Represents a group chat
class GroupChat {
    private List<User> members;
    private List<Message> messages;
    private final Lock lock = new ReentrantLock();

    public GroupChat() {
        this.members = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    public List<User> getMembers() {
        return members;
    }

    public void addMember(User user) {
        lock.lock();
        try {
            members.add(user);
        } finally {
            lock.unlock();
        }
    }

    public void sendMessage(User sender, String content) {
        lock.lock();
        try {
            for (User member : members) {
                Message message = new Message(sender, member, content);
                messages.add(message);
            }
        } finally {
            lock.unlock();
        }
    }
}

// Messaging service responsible for sending and receiving messages
class MessagingService {
    private Map<Integer, User> users;
    private List<GroupChat> groupChats;
    private final Lock lock = new ReentrantLock();

    public MessagingService() {
        this.users = new HashMap<>();
        this.groupChats = new ArrayList<>();
    }

    public void registerUser(User user) {
        lock.lock();
        try {
            users.put(user.getUserId(), user);
        } finally {
            lock.unlock();
        }
    }

    public void createGroupChat(User creator, List<User> members) {
        lock.lock();
        try {
            GroupChat groupChat = new GroupChat();
            groupChat.addMember(creator);
            groupChat.getMembers().addAll(members);
            groupChats.add(groupChat);
        } finally {
            lock.unlock();
        }
    }

    public void sendMessage(User sender, User receiver, String content) {
        lock.lock();
        try {
            Message message = new Message(sender, receiver, content);
            // Send message to receiver
        } finally {
            lock.unlock();
        }
    }

    public void sendMessageToGroup(User sender, GroupChat groupChat, String content) {
        lock.lock();
        try {
            groupChat.sendMessage(sender, content);
        } finally {
            lock.unlock();
        }
    }

    public List<GroupChat> getGroupChats() {
        lock.lock();
        try {
            return new ArrayList<>(groupChats);
        } finally {
            lock.unlock();
        }
    }


    // Other methods for message handling, user management, etc.
}

// Main class for simulating interactions
public class WhatsAppMessenger {
    public static void main(String[] args) {
        // Initialize users
        User user1 = new User(1);
        User user2 = new User(2);
        User user3 = new User(3);

        // Initialize messaging service
        MessagingService messagingService = new MessagingService();
        messagingService.registerUser(user1);
        messagingService.registerUser(user2);
        messagingService.registerUser(user3);

        // Add contacts
        user1.addContact(user2);
        user1.addContact(user3);

        // Create and join group chat
        List<User> groupMembers = new ArrayList<>();
        groupMembers.add(user1);
        groupMembers.add(user2);
        messagingService.createGroupChat(user1, groupMembers);

        // Send message
        messagingService.sendMessage(user1, user2, "Hello!");
        messagingService.sendMessageToGroup(user1, messagingService.getGroupChats().get(0), "Group message!");
    }
}
