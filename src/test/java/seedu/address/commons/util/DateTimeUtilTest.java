package seedu.address.commons.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

//@@author A0162877N
/**
 * Date Time Util jUnit test
 */
public class DateTimeUtilTest {

    public static final String DEFAULT_STARTTIME = "00:00:00";
    public static final String DEFAULT_ENDTIME = "23:59:59";
    public static final String DATE_DELIMITER = ",";

    @SuppressWarnings("deprecation")
    @Test
    public void isEndDateSame_ReturnTrue() {
        Date endTime = new Date(2222, 1, 1);
        endTime.setHours(23);
        endTime.setMinutes(59);
        endTime.setSeconds(59);
        assertTrue(DateTimeUtil.getEndDate().equals(endTime));
    }

    @SuppressWarnings("deprecation")
    @Test
    public void isEndDateSame_ReturnFalse() {
        Date endTime = new Date(2222, 1, 1);
        endTime.setHours(21);
        endTime.setMinutes(59);
        endTime.setSeconds(59);
        assertFalse(DateTimeUtil.getEndDate().equals(endTime));
    }

    @Test
    public void isStartDateSame_ReturnTrue() {
        Date dateToCompare = DateTimeUtil.getStartDate();
        Date startDate = new Date(dateToCompare.getTime());
        assertTrue(dateToCompare.equals(startDate));
    }

    @SuppressWarnings("deprecation")
    @Test
    public void isStartDateSame_ReturnFalse() {
        Date startDate = new Date();
        startDate.setHours(0);
        startDate.setMinutes(0);
        startDate.setSeconds(1);
        assertFalse(DateTimeUtil.getStartDate().equals(startDate));
    }

    @Test
    public void isSameString_ReturnTrue() {
        assertTrue(DateTimeUtil.DEFAULT_STARTTIME.equals(DEFAULT_STARTTIME));
        assertTrue(DateTimeUtil.DEFAULT_ENDTIME.equals(DEFAULT_ENDTIME));
        assertTrue(DateTimeUtil.DATE_INDEX == 0);
        assertTrue(DateTimeUtil.DATE_DELIMITER.equals(DATE_DELIMITER));
    }
}
