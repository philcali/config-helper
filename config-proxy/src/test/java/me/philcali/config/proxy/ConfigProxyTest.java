package me.philcali.config.proxy;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import me.philcali.config.annotations.Parameter;
import me.philcali.config.annotations.ParameterGroup;
import me.philcali.config.api.IConfigFactory;
import me.philcali.config.api.IConfigProvider;
import me.philcali.config.api.IParameter;
import me.philcali.config.api.IParameters;

public class ConfigProxyTest {
    private interface IConfigTest {
        String getName();
        int getAge();
    }

    @ParameterGroup("Person")
    private interface IConfigTest2 {
        @Parameter("name")
        String getName();

        @Parameter("age")
        int getAge();
    }

    private IConfigProvider provider;
    private IConfigFactory proxy;

    @Before
    public void setUp() {
        provider = mock(IConfigProvider.class);
        proxy = new ConfigProxyFactory(provider);
    }

    @Test
    public void testCreateNoAnnotations() {
        IConfigTest test = proxy.create(IConfigTest.class);
        IParameters parameters = mock(IParameters.class);
        IParameter nameParam = mock(IParameter.class);
        IParameter ageParam = mock(IParameter.class);
        doReturn(parameters).when(provider).get(eq("IConfigTest"));
        doReturn(Optional.of(nameParam)).when(parameters).getParameter(eq("Name"));
        doReturn(Optional.of(ageParam)).when(parameters).getParameter(eq("Age"));
        doReturn("Philip Cali").when(nameParam).getValue();
        doReturn(42).when(ageParam).getValue();
        assertEquals("Philip Cali", test.getName());
        assertEquals(42, test.getAge());
    }

    @Test
    public void testCreateWithAnnotations() {
        IConfigTest2 test = proxy.create(IConfigTest2.class);
        IParameters parameters = mock(IParameters.class);
        IParameter nameParam = mock(IParameter.class);
        IParameter ageParam = mock(IParameter.class);
        doReturn(parameters).when(provider).get(eq("Person"));
        doReturn(Optional.of(nameParam)).when(parameters).getParameter(eq("name"));
        doReturn(Optional.of(ageParam)).when(parameters).getParameter(eq("age"));
        doReturn("Philip Cali").when(nameParam).getValue();
        doReturn(42).when(ageParam).getValue();
        assertEquals("Philip Cali", test.getName());
        assertEquals(42, test.getAge());
    }
}
