package com.riverinnovations.saltui.model.user;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class UserTest {

    @Test
    public void testSimpleEqualsAndHashcode() throws Exception {

        User one = new User("one");
        User two = new User("two");
        User oneA = new User("one");

        assertEquals(one, oneA);
        assertEquals(one.hashCode(), oneA.hashCode());
        assertNotEquals(one, two);
        assertNotEquals(one.hashCode(), two.hashCode());
    }

}
