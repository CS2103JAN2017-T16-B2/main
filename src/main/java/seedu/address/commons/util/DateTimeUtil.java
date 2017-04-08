package seedu.address.commons.util;

import java.util.Date;

//@@author A0162877N
public class DateTimeUtil {

    public static final String DEFAULT_STARTTIME = "00:00:00";
    public static final String DEFAULT_ENDTIME = "23:59:59";
    public static final int DATE_INDEX = 0;
    public static final String DATE_DELIMITER = ",";

    @SuppressWarnings("deprecation")
    public static Date getEndDate() {
        Date endTime = new Date(2222, 1, 1);
        endTime.setHours(23);
        endTime.setMinutes(59);
        endTime.setSeconds(59);
        return endTime;
    }

    @SuppressWarnings("deprecation")
    public static Date getStartDate() {
        Date startDate = new Date();
        startDate.setHours(0);
        startDate.setMinutes(0);
        startDate.setSeconds(0);
        return startDate;
    }
}
