package model;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.DateType;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * Type that persists a {@link LocalDate} object.
 */
public class LocalDateType extends DateType {

    private static final LocalDateType singleton = new LocalDateType();
    private static final DateStringFormatConfig sqlDateFormatConfig = new DateStringFormatConfig("yyyy-MM-dd");

    public static LocalDateType getSingleton() {
        return singleton;
    }

    private LocalDateType() {
        super(SqlType.DATE, new Class<?>[] { LocalDate.class });
    }

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
        LocalDate localDate = (LocalDate)javaObject;
        return Timestamp.valueOf(localDate.atStartOfDay());
    }

    @Override
    protected DateStringFormatConfig getDefaultDateFormatConfig() {
        return sqlDateFormatConfig;
    }

    @Override
    public boolean isValidForField(Field field) {
        return (field.getType() == LocalDate.class);
    }
}
