package ru.dexsys;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

class DBTest { //Вначале задаю драйвер для подключения к mySQL, URL, логин и пароль, чтоб не растягивать всё в большую "портянку")
    private static final String DRIVERNAME = "com.mysql.cj.jdbc.Driver";
    private static final String TIMEZONE = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    public static String getTIMEZONE() {
        return TIMEZONE;
    }

    void run() { //в методе run сразу описываю ВСЕ исключения для подключения)
        try { //проверка драйвера для mySQL
            Class.forName(DRIVERNAME);
        } catch (ClassNotFoundException e) {
            System.out.println("Can't get class. No driver found");
            e.printStackTrace();
            return;
        }
        Connection connection;
        try { //подключаюсь к БД
            connection = DriverManager.getConnection(DBStartButton.URL, DBStartButton.USERNAME, DBStartButton.PASSWORD);
        } catch (SQLException e) {
            System.out.println("Can't get connection. Incorrect URL");
            e.printStackTrace();
            return;
        }


        /*Вот сюда буду писать все запросы к БД!*/
        try { //создаю объект Statement'а для подключения к БД, прописываю данные
            Statement statement = connection.createStatement();
            String sqlCommandAddStudent = "INSERT INTO Students(firstName, lastName, age, phone) VALUES ('Ivan', 'Matveev', 24, 89127684213);";
            String sqlCommandDeletStudent = "DELETE from dexautomation where (firstName = 'Ivan' and lastName = 'Matveev')";
            String sqlCommandValidation = "SELECT * from Students where (firstName = 'Ivan' and lastName = 'Matveev' and age = 24 and phone = 89127684213)";
//            statement.execute(sqlCommandAddStudent);
//            statement.execute(sqlCommandDeletStudent);
            String validation = statement.executeQuery(sqlCommandValidation).toString();
            Assert.assertEquals(validation, "22222");
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

    @Test
    public static void main(String[] args) {
        DBTest app = new DBTest();
        app.run();
    }
}

