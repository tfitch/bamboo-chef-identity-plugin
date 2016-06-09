package ut.io.chef.bamboo;

import org.junit.Test;
import io.chef.bamboo.api.MyPluginComponent;
import io.chef.bamboo.impl.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}