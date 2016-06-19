package model;

import java.lang.reflect.Field;
import java.sql.Timestamp;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.DateType;

/**
 * Type that persists a {@link java.time.LocalDate} object.
 */
public class LocalDateType extends DateType {

    private static final LocalDateType singleTon = new LocalDateType();
    private static final DateStringFormatConfig sqlDateFormatConfig = new DateStringFormatConfig("yyyy-MM-dd");

    public static LocalDateType getSingleton() {
        return singleTon;
    }

    private LocalDateType() {
        super(SqlType.DATE, new Class<?>[] { java.time.LocalDate.class });
    }

    /**
     * Here for others to subclass.
     */
    protected LocalDateType(SqlType sqlType, Class<?>[] classes) {
        super(sqlType, classes);
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        Timestamp value = (Timestamp)sqlArg;
        return value.toLocalDateTime().toLocalDate();
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        java.time.LocalDate localDate = (java.time.LocalDate)javaObject;
        return Timestamp.valueOf(localDate.atStartOfDay());
    }

    @Override
    protected DateStringFormatConfig getDefaultDateFormatConfig() {
        return sqlDateFormatConfig;
    }

    @Override
    public boolean isValidForField(Field field) {
        return (field.getType() == java.time.LocalDate.class);
    }
}
