package edu.guilford;

import java.util.ArrayList;

public class BlackjackGame {
    
        private ArrayList<User> users = new ArrayList<User>();
        
        public void addUser(User user) {
            users.add(user);
        }
        
        public User getUser(String username, String password) {
            for (User user : users) {
                if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                    return user;
                }
            }
            return null;
        }
    }


