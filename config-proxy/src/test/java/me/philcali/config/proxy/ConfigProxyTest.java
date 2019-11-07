package me.philcali.config.proxy;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.List;
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

        List<String> getScopes();
    }

    @ParameterGroup("Person")
    private interface IConfigTest2 {
        @Parameter("name")
        String getName();

        @Parameter("age")
        int getAge();
    }

    private interface IConfigTest3 {
        String getName();

        IConfigTest getTest();
    }

    private IConfigProvider provider;
    private IConfigFactory proxy;

    @Before
    public void setUp() {
        provider = mock(IConfigProvider.class);
        proxy = new ConfigProxyFactory(ConfigProxyFactoryOptions.builder().withConfigProvider(provider).build());
    }

    @Test
    public void testNestedChildInterfaces() {
        IConfigTest3 test3 = proxy.create(IConfigTest3.class);
        IConfigTest test = test3.getTest();
        IParameters parameters = mock(IParameters.class);
        IParameter nameParam = mock(IParameter.class);
        IParameter ageParam = mock(IParameter.class);
        IParameter scopeParam = mock(IParameter.class);
        doReturn(parameters).when(provider).get(anyVararg());
        doReturn(Optional.of(nameParam)).when(parameters).getParameter(eq("Name"));
        doReturn(Optional.of(ageParam)).when(parameters).getParameter(eq("Age"));
        doReturn(Optional.of(scopeParam)).when(parameters).getParameter(eq("Scopes"));
        doReturn("Philip Cali").when(nameParam).getValue();
        doReturn(42).when(ageParam).getValue();
        doReturn("email,profile").when(scopeParam).getValue();
        assertEquals("Philip Cali", test.getName());
        assertEquals(42, test.getAge());
        assertEquals(Arrays.asList("email", "profile"), test.getScopes());
    }

    @Test
    public void testCreateNoAnnotations() {
        IConfigTest test = proxy.create(IConfigTest.class);
        IParameters parameters = mock(IParameters.class);
        IParameter nameParam = mock(IParameter.class);
        IParameter ageParam = mock(IParameter.class);
        IParameter scopeParam = mock(IParameter.class);
        doReturn(parameters).when(provider).get(anyVararg());
        doReturn(Optional.of(nameParam)).when(parameters).getParameter(eq("Name"));
        doReturn(Optional.of(ageParam)).when(parameters).getParameter(eq("Age"));
        doReturn(Optional.of(scopeParam)).when(parameters).getParameter(eq("Scopes"));
        doReturn("Philip Cali").when(nameParam).getValue();
        doReturn(42).when(ageParam).getValue();
        doReturn("email,profile").when(scopeParam).getValue();
        assertEquals("Philip Cali", test.getName());
        assertEquals(42, test.getAge());
        assertEquals(Arrays.asList("email", "profile"), test.getScopes());
    }

    @Test
    public void testCreateWithAnnotations() {
        IConfigTest2 test = proxy.create(IConfigTest2.class);
        IParameters parameters = mock(IParameters.class);
        IParameter nameParam = mock(IParameter.class);
        IParameter ageParam = mock(IParameter.class);
        doReturn(parameters).when(provider).get(anyVararg());
        doReturn(Optional.of(nameParam)).when(parameters).getParameter(eq("name"));
        doReturn(Optional.of(ageParam)).when(parameters).getParameter(eq("age"));
        doReturn("Philip Cali").when(nameParam).getValue();
        doReturn(42).when(ageParam).getValue();
        assertEquals("Philip Cali", test.getName());
        assertEquals(42, test.getAge());
    }
}
