package ru.dexsys;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.NoSuchElementException;

import java.sql.*;

public class DBAddStudentTest {
    private static final String DRIVERNAME = "com.mysql.cj.jdbc.Driver";
    private static final String TIMEZONE = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String URL = "jdbc:mysql://db4free.net:3306/dexautomation" + TIMEZONE;
    private static final String USERNAME = "dexautomation";
    private static final String PASSWORD = "dexautomation";

    @Test
    void dbTest() { //в методе сразу описываю ВСЕ исключения для подключения) //String sqlCommand - будущий параметр
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
            String sqlCommandChangeStudent = "UPDATE Students SET firstName = 'Ivan' WHERE id = 19";
//            String sqlCommandAddStudent = "INSERT INTO Students(firstName, lastName, age, phone) VALUES ('Ivan', 'Matveev', 24, 89127684213);";
            statement.executeUpdate(sqlCommandChangeStudent); // выполнить команду
            System.out.println("DataBase is updated");

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
