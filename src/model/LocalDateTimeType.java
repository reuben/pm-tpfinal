package model;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.DateType;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Type that persists a {@link java.time.LocalDate} object.
 */
public class LocalDateTimeType extends DateType {

    private static final LocalDateTimeType singleton = new LocalDateTimeType();
    private static final DateStringFormatConfig sqlDateFormatConfig = new DateStringFormatConfig("yyyy-MM-dd");

    public static LocalDateTimeType getSingleton() {
        return singleton;
    }

    private LocalDateTimeType() {
        super(SqlType.DATE, new Class<?>[] { java.time.LocalDate.class });
    }

    protected LocalDateTimeType(SqlType sqlType, Class<?>[] classes) {
        super(sqlType, classes);
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        return ((Timestamp)sqlArg).toLocalDateTime();
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        return Timestamp.valueOf((LocalDateTime)javaObject);
    }

    @Override
    protected DateStringFormatConfig getDefaultDateFormatConfig() {
        return sqlDateFormatConfig;
    }

    @Override
    public boolean isValidForField(Field field) {
        return (field.getType() == LocalDateTime.class);
    }
}
