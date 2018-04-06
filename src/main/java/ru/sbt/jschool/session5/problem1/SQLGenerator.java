package ru.sbt.jschool.session5.problem1;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 */
public class SQLGenerator {

    public <T> String insert(Class<T> clazz) {
        String s = "INSERT INTO ";
        String tableName = clazz.getAnnotation(Table.class).name();
        s += tableName;
        s += "(";
        Field[] Fields = clazz.getDeclaredFields();
        ArrayList<String> into = getColumnsNames(Fields, true, true);
        s += makeStringFromColumns(into, ", ", "");
        s += ") VALUES (";
        for (int i = 0; i < into.size() - 1; i++) {
            s += "?, ";
        }
        s += "?)";
        return s;
    }

    public <T> String update(Class<T> clazz) {
        String s = "UPDATE ";
        String tableName = clazz.getAnnotation(Table.class).name();
        s += tableName;
        s += " SET ";
        Field[] Fields = clazz.getDeclaredFields();
        ArrayList<String> select = getColumnsNames(Fields, true, false);
        ArrayList<String> where = getColumnsNames(Fields, false, true);
        s += makeStringFromColumns(select, " = ?, ", " = ?");
        s += " WHERE ";
        s += makeStringFromColumns(where, " = ? AND ", " = ?");
        return s;
    }

    public <T> String delete(Class<T> clazz) {
        String s = "DELETE FROM ";
        String tableName = clazz.getAnnotation(Table.class).name();
        s += tableName;
        Field[] Fields = clazz.getDeclaredFields();
        ArrayList<String> where = getColumnsNames(Fields, false, true);
        s += " WHERE ";
        s += makeStringFromColumns(where, " = ? AND ", " = ?");
        return s;
    }

    public <T> String select(Class<T> clazz) {
        String s = "SELECT ";
        int count = 0;
        Field[] Fields = clazz.getDeclaredFields();
        ArrayList<String> select = getColumnsNames(Fields, true, false);
        ArrayList<String> where = getColumnsNames(Fields, false, true);
        s += makeStringFromColumns(select, ", ", "");
        s += " FROM ";
        String tableName = clazz.getAnnotation(Table.class).name();
        s += tableName;
        s += " WHERE ";
        s += makeStringFromColumns(where, " = ? AND ", " = ?");
        return s;
    }

    public <T> ArrayList<String> getColumnsNames(Field[] Fields, boolean getColumns, boolean getPrimaryKey) {
        ArrayList<String> columsNames = new ArrayList<>();
        for (int i = 0; i < Fields.length; i++) {
            if (getColumns) {
                try {
                    String str = Fields[i].getAnnotation(Column.class).name();
                    if (!str.equals("")) {
                        columsNames.add(str.toLowerCase());
                    } else {
                        columsNames.add(Fields[i].getName().toLowerCase());
                    }
                } catch (NullPointerException e) {
                    e.getMessage();
                }
            }
            if (getPrimaryKey) {
                try {
                    String str = Fields[i].getAnnotation(PrimaryKey.class).name();
                    if (!str.equals("")) {
                        columsNames.add(str.toLowerCase());
                    } else {
                        columsNames.add(Fields[i].getName().toLowerCase());
                    }
                } catch (NullPointerException e) {
                    e.getMessage();
                }
            }
        }
        return columsNames;
    }

    // t sep f sep g end
    public <T> String makeStringFromColumns(ArrayList<String> columnsNames, String sep, String end) {
        int size = columnsNames.size();
        String s = "";
        for (int i = 0; i < size - 1; i++) {
            s += columnsNames.get(i);
            s += sep;
        }
        s += columnsNames.get(size - 1);
        s += end;
        return s;
    }
}
