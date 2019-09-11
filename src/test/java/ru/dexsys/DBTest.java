package ru.dexsys;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;

import java.sql.*;

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
        public void dbTest2() { //проверяю, в единственном ли количестве моя строка
            try {
                Statement statement = connection.createStatement(); //создаю объект Statement'а для подключения к БД, прописываю данные
                String sqlCommandValidation2 = "SELECT COUNT(id) from Students where (firstName = 'Ivan' and lastName = 'Matveev' and age = 24 and phone = 89127684213)";
//                String sqlCommandValidation2 = "SELECT COUNT(id) from Students where firstName = 'Иван'";
                ResultSet resultSet = statement.executeQuery(sqlCommandValidation2);
                resultSet.first();
                int count=resultSet.getInt(1);
                String failMassage;

                if (count == 0) {
                    failMassage = "There is no match in this DataBase";
                } else {
                    failMassage = "There are " + count + " matches in this DataBase";
                }
                Assert.assertEquals(failMassage, 1, count);
                System.out.println("There is only " + count + " string with my data in this DataBase");
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