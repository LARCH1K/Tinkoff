package edu.hw10;

import edu.hw10.task1.RandomObjectGenerator;
import edu.hw10.task1.classes.MyClass;
import edu.hw10.task1.classes.MyRecord;
import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Task1Test {

    @Test
    void RandomObjectGeneratorWithRecordTest()
        throws InvocationTargetException, IllegalAccessException, InstantiationException {
        RandomObjectGenerator rog = new RandomObjectGenerator();
        MyRecord myRecord = rog.nextObject(MyRecord.class);
        assertTrue(myRecord.intValue() > 0);
        assertTrue(myRecord.intValue() <= 100);
        assertTrue(myRecord.doubleValue() >= 100);
        assertTrue(myRecord.doubleValue() <= 200);
        assertNotNull(myRecord.stringValue());
    }

    @Test
    void RandomObjectGeneratorWithPOJOClassConstructorTest()
        throws InvocationTargetException, IllegalAccessException, InstantiationException {
        RandomObjectGenerator rog = new RandomObjectGenerator();
        MyClass myClass = rog.nextObject(MyClass.class);
        assertTrue(myClass.getIntValue() > 0);
        assertTrue(myClass.getIntValue() <= 100);
    }

    @Test
    void RandomObjectGeneratorWithPOJOClassMethodCreateTest()
        throws InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        RandomObjectGenerator rog = new RandomObjectGenerator();
        MyClass myClass = rog.nextObject(MyClass.class, "create");
        assertTrue(myClass.getIntValue() >= 100);
        assertTrue(myClass.getIntValue() <= 200);
    }
}
