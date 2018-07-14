package me.philcali.config.env;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import me.philcali.config.api.IConfigProvider;

public class EnvConfigProviderTest {
    private IConfigProvider provider;

    @Before
    public void setUp() {
        provider = new EnvConfigProvider();
    }

    @Test
    public void testGetParameters() {
        assertEquals(Optional.empty(), provider.get("philbo").getParameter("name"));
        assertTrue(provider.get("session").getParameter("manager").isPresent());
    }
}
