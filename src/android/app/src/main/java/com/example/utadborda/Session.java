package com.example.utadborda;

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
                if (notified) return true;
                else return false;
            } catch (Exception e) {
                System.out.println("Failed to notify session members.");
            }
            return notifyMembers(data, n+1);
        } else {
            return false;
        }
    }
}
