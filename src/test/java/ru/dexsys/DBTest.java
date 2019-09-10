package ru.dexsys;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.NoSuchElementException;

import java.sql.*;

class DBTest { //Вначале задаю драйвер для подключения к mySQL, URL, логин и пароль, чтоб не растягивать всё в большую "портянку")
    private static final String DRIVERNAME = "com.mysql.cj.jdbc.Driver";
    private static final String TIMEZONE = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String URL = "jdbc:mysql://db4free.net:3306/dexautomation" + TIMEZONE;
    private static final String USERNAME = "dexautomation";
    private static final String PASSWORD = "dexautomation";

    @Test
    void dbTest() { //в методе сразу описываю ВСЕ исключения для подключения)
        try { //проверка драйвера для mySQL
            Class.forName(DRIVERNAME);
        } catch (ClassNotFoundException e) {
            System.out.println("Can't get class. No driver found");
            e.printStackTrace();
            return;
        }
        Connection connection;
        try { //подключаюсь к БД
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Can't get connection. Incorrect URL");
            e.printStackTrace();
            return;
        }

//          Здесь пишутся все запросы к БД
        try { //создаю объект Statement'а для подключения к БД, прописываю данные
            Statement statement = connection.createStatement();
            String sqlCommandValidation = "SELECT * from Students where (firstName = 'Ivan' and lastName = 'Matveev' and age = 24 and phone = 89127684213)";

            ResultSet resultSet = statement.executeQuery(sqlCommandValidation);
            boolean consistance = false;
            try {
                while (resultSet.first()) {
                    int id = resultSet.getInt(1);
                    String firstName = resultSet.getString(2);
                    String lastName = resultSet.getString(3);
                    int age = resultSet.getInt(4);
                    long phone = resultSet.getLong(5);
                    String idAnswer = "id: " + id + " ";
                    String someAnswer =  "firstName: " + firstName + " lastName: " + lastName + " age: " + age + " phone: " + phone;
                    String rightAnswer = "firstName: Ivan lastName: Matveev age: 24 phone: 89127684213";
                    if (someAnswer.equals(rightAnswer)) {
                        consistance = true;
                        System.out.println(idAnswer + someAnswer);
                    }
                }
                try{
                    Assert.assertTrue(consistance);
                }catch (AssertionError e){
                    System.out.println("There is no me in this DataBase");
                }
            } catch (NoSuchElementException e) {
                System.out.println("There is no me in this DataBase");
//                statement.execute(sqlCommandAddStudent);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } /*А здесь все запросы заканчиваются) */

        try { //проверка закрытия подключения
            connection.close();
        } catch (SQLException e) {
            System.out.println("Can't close connection");
            e.printStackTrace(); // отличается от toString() тем, что вызывает весь стек, что полезно для отладки.
        }
    }
}