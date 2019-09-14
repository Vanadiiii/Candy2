package ru.dexsys;

import org.junit.*; //играюсь с записью библиотек)
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;

import java.sql.*;
import java.util.LinkedHashSet;

public class DBTest { //Вначале задаю драйвер для подключения к mySQL, URL, логин и пароль, чтоб не растягивать всё в большую "портянку")
    private static final String DRIVERNAME = "com.mysql.cj.jdbc.Driver";
    private static final String TIMEZONE = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String URL = "jdbc:mysql://db4free.net:3306/dexautomation" + TIMEZONE;
    private static final String USERNAME = "dexautomation";
    private static final String PASSWORD = "dexautomation";
    private Connection connection;

      @Before
    public void init() { //в методе сразу описываю ВСЕ исключения для подключения)
        try { //проверка драйвера для mySQL 
            Class.forName(DRIVERNAME);
        } catch (ClassNotFoundException e) {
            System.out.println("Can't get class. No driver found");
            e.printStackTrace();
            return;
        }
        try { //подключаюсь к БД
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Can't get connection. Incorrect URL");
            e.printStackTrace();
        }
    }

      @Test
    public void dbTest1() { //проверяю, есть ли я в БД вообще
            try {
                Statement statement = connection.createStatement(); //создаю объект Statement'а для подключения к БД, прописываю данные
                String sqlCommandValidation1 = "SELECT * from Students where (firstName = 'Ivan' and lastName = 'Matveev' and age = 24 and phone = 89127684213)";
                ResultSet resultSet = statement.executeQuery(sqlCommandValidation1);

                boolean consistence = false;
                try {
                    resultSet.first();
                    int id = resultSet.getInt("id");
                    String firstName = resultSet.getString("firstName");
                    String lastName = resultSet.getString("lastName");
                    int age = resultSet.getInt("age");
                    long phone = resultSet.getLong("phone");
                    String idAnswer = "id: " + id + " ";
                    String someAnswer = "firstName: " + firstName + " lastName: " + lastName + " age: " + age + " phone: " + phone;
                    String rightAnswer = "firstName: Ivan lastName: Matveev age: 24 phone: 89127684213";
                    if (someAnswer.equals(rightAnswer)) {
                        consistence = true;
                        System.out.print("I'm consist of that DataBase: \n" + idAnswer + someAnswer);
                    }
                    try {
                        Assert.assertTrue(consistence);
                    } catch (AssertionError e) {
                        System.out.println("There is no me in this DataBase");
                    }
                } catch (NoSuchElementException e) {
                    System.out.println("There is no me in this DataBase");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

      @Test
    public void dbTest2() { //проверка совпадения строки (и количества совпадений)
            try {
                Statement statement = connection.createStatement(); //создаю объект Statement'а для подключения к БД, прописываю данные
//                String sqlCommandValidation2 = "SELECT COUNT(id) from Students where (firstName = 'Ivan' and lastName = 'Matveev' and age = 24 and phone = 89127684213)";
//                String sqlCommandValidation2 = "SELECT COUNT(id) from Students where firstName = 'Иван'";
                String sqlCommandValidation3 = "SELECT id from Students where (firstName = 'Ivan' and lastName = 'Matveev' and age = 24 and phone = 89127684213)";
//                String sqlCommandValidation3 = "SELECT id from Students where firstName = 'Иван'";


                ResultSet resultSet2 = statement.executeQuery(sqlCommandValidation3);
                LinkedHashSet<Integer> listOfNumbers = new LinkedHashSet<>(20);

                while (resultSet2.next()){
                    listOfNumbers.add(resultSet2.getInt(1));
                }
                Assert.assertTrue(listOfNumbers.size()>0);

                StringBuilder stringOfNumbers = new StringBuilder(); // данный клас необходим для обновления значения строки.
                // Не знаю, зачем, пока, но сама IDEA подсказала))
                int count = 0;
                for (Integer i : listOfNumbers) {
                    stringOfNumbers.append(i.toString()).append(", ");
                    count++;
                }
                String newStringOfNumbers = stringOfNumbers.substring(0, stringOfNumbers.length() - 2);//удаляю два последних лишних символа (", ")
                if (newStringOfNumbers.length() == (1 | 2 | 3)) {
                    System.out.println("There is 1 match in this DataBase, and my id = " + newStringOfNumbers);
                }
                System.out.println("There are " + count + " matches in this DataBase: on " + newStringOfNumbers + " positions");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

      @After
    public void tearDown() {
        try { //проверка закрытия подключения
            connection.close();
        } catch (SQLException e) {
            System.out.println("Can't close connection");
            e.printStackTrace(); // отличается от toString() тем, что вызывает весь стек, что полезно для отладки.
        }
    }
}