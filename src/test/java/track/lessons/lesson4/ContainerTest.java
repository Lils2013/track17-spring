package track.lessons.lesson4;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import track.container.Container;
import track.container.JsonConfigReader;
import track.container.beans.Car;
import track.container.beans.Engine;
import track.container.beans.Gear;
import track.container.config.Bean;
import track.container.config.ConfigReader;
import track.container.config.InvalidConfigurationException;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class ContainerTest {

    private static Container container;
    @BeforeClass
    public static void init() throws InvalidConfigurationException {
        ConfigReader reader = new JsonConfigReader();
        List<Bean> beans = reader.parseBeans(new File("src/main/resources/config.json"));
        container = new Container(beans);
    }

    @Test
    public void getByIdTest() throws Exception {
        Gear gear = (Gear) container.getById("gearBean");
        Assert.assertTrue("Gear{count=6}".equals(gear.toString()));
        Engine engine = (Engine) container.getById("engineBean");
        Assert.assertTrue("Engine{power=200}".equals(engine.toString()));
        Car car = (Car) container.getById("carBean");
        Assert.assertTrue("Car{gear=Gear{count=6}, engine=Engine{power=200}}".equals(car.toString()));
    }

    @Test
    public void getByClassTest() throws Exception {
        Gear gear = (Gear) container.getByClass("track.container.beans.Gear");
        Assert.assertTrue("Gear{count=6}".equals(gear.toString()));
        Engine engine = (Engine) container.getByClass("track.container.beans.Engine");
        Assert.assertTrue("Engine{power=200}".equals(engine.toString()));
        Car car = (Car) container.getByClass("track.container.beans.Car");
        Assert.assertTrue("Car{gear=Gear{count=6}, engine=Engine{power=200}}".equals(car.toString()));
    }
}
