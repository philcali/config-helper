package me.philcali.config.system;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import me.philcali.config.api.IConfigProvider;

public class SystemPropertyConfigProviderTest {
    private IConfigProvider provider;

    @Before
    public void setUp() {
        provider = new SystemPropertyConfigProvider();
    }

    @Test
    public void testGet() {
        assertTrue(provider.get("user").getParameter("home").isPresent());
        assertFalse(provider.get("farts").getParameter("alot").isPresent());
        System.setProperty("farts.alot", "true");
        assertEquals("true", provider.get("farts").getParameter("alot").get().getValue());
    }
}
