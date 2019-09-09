package ru.dexsys;

public class DBStartButton {
    static final String URL = "jdbc:mysql://db4free.net:3306/dexautomation" + DBTest.getTIMEZONE();
    static final String USERNAME = "dexautomation";
    static final String PASSWORD = "dexautomation";

    public static void main(String[] args) {
        DBTest app = new DBTest();
        app.run();
    }
}
