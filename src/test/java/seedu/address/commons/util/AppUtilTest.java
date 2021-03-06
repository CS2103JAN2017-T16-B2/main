package seedu.address.commons.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AppUtilTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getImage_exitingImage() {
        assertNotNull(AppUtil.getImage("/images/app_icon.png"));
    }

    @Test
    public void getImage_nullGiven_assertionError() {
        thrown.expect(AssertionError.class);
        AppUtil.getImage(null);
        fail("Should not reach here");
    }
}
