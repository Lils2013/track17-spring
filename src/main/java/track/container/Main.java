package track.container;

import track.container.beans.Car;
import track.container.beans.Engine;
import track.container.beans.Gear;
import track.container.config.Bean;
import track.container.config.ConfigReader;
import track.container.config.InvalidConfigurationException;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 *
 */
public class Main {

    public static void main(String[] args) throws Exception {

        /*

        ПРИМЕР ИСПОЛЬЗОВАНИЯ

         */

//        // При чтении нужно обработать исключение
//        ConfigReader reader = new JsonReader();
//        List<Bean> beans = reader.parseBeans("config.json");
//        Container container = new Container(beans);
//
//        Car car = (Car) container.getByClass("track.container.beans.Car");
//        car = (Car) container.getById("carBean");
        ConfigReader reader = new JsonConfigReader();
        List<Bean> beans = reader.parseBeans(new File("src/main/resources/config.json"));
        for (Bean bean : beans) {
            System.out.println(bean);
        }
        Container container = new Container(beans);
        Gear gear = (Gear) container.getById("gearBean");
        System.out.println(gear);
        Engine engine = (Engine) container.getById("engineBean");
        System.out.println(engine);
        Car car = (Car) container.getById("carBean");
        System.out.println(car);
//        Gear gear1 = (Gear) container.getByClass("track.container.beans.Gear");
//        System.out.println(gear1);
//        Engine engine1 = (Engine) container.getByClass("track.container.beans.Engine");
//        System.out.println(engine1);
        Car car1 = (Car) container.getByClass("track.container.beans.Car");
        System.out.println(car1.getEngine());
    }
}
