package com.example.utadborda;

import org.json.JSONObject;

public class Session {
    String sessionKey;
    MatchActivity matchActivity;

    private Session(String key, MatchActivity matcher) {
        sessionKey = key;
        matchActivity = matcher;
    }

    private boolean notifyMembers(Object data) throws InterruptedException {
        boolean notified = false;
        try {
            // notift all session members with data
            // notified = x;
            if (notified) return true;
            else return false;
        } catch (Exception e) {
            System.out.println("Failed to notify session members.");
        }
        Thread.sleep(1000);
        return notifyMembers(data, 0);
    }

    private boolean notifyMembers(Object data, int n) throws InterruptedException {
        if (n < 5) {
            boolean notified = false;
            try {
                // notify all session members with data
                // notified = x;
                if (notified) return true;
                else return false;
            } catch (Exception e) {
                System.out.println("Failed to notify session members.");
            }
            return notifyMembers(data, n+1);
            //Thread.sleep(1000);
        } else {
            return false;
        }
    }
}
