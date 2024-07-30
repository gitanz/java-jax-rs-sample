package org.sample.students.api.exception_mappers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BaseExceptionManager {

    public String getCurrentTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}
