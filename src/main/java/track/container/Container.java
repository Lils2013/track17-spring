package track.container;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import track.container.config.Bean;
import track.container.config.Property;

import static track.container.config.ValueType.REF;
import static track.container.config.ValueType.VAL;


/**
 * Основной класс контейнера
 * У него определено 2 публичных метода, можете дописывать свои методы и конструкторы
 */
public class Container {

    List<Bean> beans;
    Map<String, Object> objByName;
    Map<String, Object> objByClassName;

    // Реализуйте этот конструктор, используется в тестах!
    public Container(List<Bean> beans) {
        this.beans = beans;
        objByName = new HashMap<>();
        objByClassName = new HashMap<>();
    }

    /**
     * Вернуть объект по имени бина из конфига
     * Например, Car car = (Car) container.getById("carBean")
     */
    public Object getById(String id) throws Exception {
        for (Bean bean : beans) {
            String objId = bean.getId();
            if (objId.equals(id)) {
                Object object = objByName.get(objId);
                if (object == null) {
                    Class<?> cls = Class.forName(bean.getClassName());
                    object = (Object) cls.newInstance();
                    for (Map.Entry<String, Property> entry : bean.getProperties().entrySet()) {
                        if (entry.getValue().getType() == VAL) {
                            Class[] arg = new Class[1];
                            arg[0] = int.class;
                            Method method = cls.getDeclaredMethod("set" +
                                    entry.getValue().getName().substring(0, 1).toUpperCase() +
                                    entry.getValue().getName().substring(1), arg);
                            method.invoke(object, Integer.parseInt(entry.getValue().getValue()));
                        } else if (entry.getValue().getType() == REF) {
                            Class[] arg = new Class[1];
                            Class<?> cls1 = null;
                            Object object1 = null;
                            String id1 = entry.getValue().getValue();
                            for (Bean bean1 : beans) {
                                String objId1 = bean1.getId();
                                if (objId1.equals(id1)) {
                                    cls1 = Class.forName(bean1.getClassName());
                                    for (Map.Entry<String, Property> entry1 : bean.getProperties().entrySet()) {
                                        if (entry1.getValue().getType() == REF &&
                                                entry1.getValue().getValue().equals(id)) {
                                            throw new Exception();
                                        }
                                    }
                                    object1 = getById(objId1);
                                }
                            }
                            arg[0] = cls1;
                            Method method1 = cls.getDeclaredMethod("set" +
                                    entry.getValue().getName().substring(0, 1).toUpperCase() +
                                    entry.getValue().getName().substring(1), arg);
                            method1.invoke(object, object1);
                        }
                    }
                    objByName.put(id, object);
                }
                return object;
            }
        }
        return null;
    }

    /**
     * Вернуть объект по имени класса
     * Например, Car car = (Car) container.getByClass("track.container.beans.Car")
     */
    public Object getByClass(String className) throws Exception {
        for (Bean bean : beans) {
            String clsName = bean.getClassName();
            if (clsName.equals(className)) {
                Object object = objByName.get(clsName);
                if (object == null) {
                    Class<?> cls = Class.forName(bean.getClassName());
                    object = (Object) cls.newInstance();
                    for (Map.Entry<String, Property> entry : bean.getProperties().entrySet()) {
                        if (entry.getValue().getType() == VAL) {
                            Class[] arg = new Class[1];
                            arg[0] = int.class;
                            Method method = cls.getDeclaredMethod("set" +
                                    entry.getValue().getName().substring(0, 1).toUpperCase() +
                                    entry.getValue().getName().substring(1), arg);
                            method.invoke(object, Integer.parseInt(entry.getValue().getValue()));
                        } else if (entry.getValue().getType() == REF) {
                            Class[] arg = new Class[1];
                            Class<?> cls1 = null;
                            Object object1 = null;
                            String id1 = entry.getValue().getValue();
                            for (Bean bean1 : beans) {
                                String objId1 = bean1.getId();
                                if (objId1.equals(id1)) {
                                    cls1 = Class.forName(bean1.getClassName());
                                    for (Map.Entry<String, Property> entry1 : bean.getProperties().entrySet()) {
                                        if (entry1.getValue().getType() == REF &&
                                                entry1.getValue().getValue().equals(bean.getId())) {
                                            throw new Exception();
                                        }
                                    }
                                    object1 = getByClass(bean1.getClassName());
                                }
                            }
                            arg[0] = cls1;
                            Method method1 = cls.getDeclaredMethod("set" +
                                    entry.getValue().getName().substring(0, 1).toUpperCase() +
                                    entry.getValue().getName().substring(1), arg);
                            method1.invoke(object, object1);
                        }
                    }
                    objByName.put(clsName, object);
                }
                return object;
            }
        }
        return null;
    }
}
