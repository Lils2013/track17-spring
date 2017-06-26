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

    private List<Bean> beans;
    private Map<String, Object> objByName;
    private Map<String, Object> objByClassName;

    // Реализуйте этот конструктор, используется в тестах!
    public Container(List<Bean> beans) {
        this.beans = beans;
        objByName = new HashMap<>();
        objByClassName = new HashMap<>();
    }

    public Map<String, Object> getMapByName() {
        return objByName;
    }

    public Map<String, Object> getMapByClass() {
        return objByClassName;
    }

    /**
     * Вернуть объект по имени бина из конфига
     * Например, Car car = (Car) container.getById("carBean")
     */
    public Object getById(String id) throws ClassNotFoundException, IllegalAccessException,
            InstantiationException, NoSuchMethodException, CyclicReferenceException, InvocationTargetException {
        for (Bean bean : beans) {
            String objId = bean.getId();
            if (objId.equals(id)) {
                Object object = objByName.get(objId);
                if (object == null) {
                    Class<?> cls = Class.forName(bean.getClassName());
                    object = (Object) cls.newInstance();
                    if (bean.getProperties() != null) {
                        for (Map.Entry<String, Property> entry : bean.getProperties().entrySet()) {
                            if (entry.getValue().getType() == VAL) {
                                parseValue(entry, cls, object);
                            } else if (entry.getValue().getType() == REF) {
                                parseRef(bean, entry, cls, object);
                            }
                        }
                    }
                    objByName.put(id, object);
                    objByClassName.put(bean.getClassName(), object);
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
    public Object getByClass(String className) throws ClassNotFoundException, IllegalAccessException,
            InstantiationException, NoSuchMethodException, CyclicReferenceException, InvocationTargetException {
        for (Bean bean : beans) {
            String clsName = bean.getClassName();
            if (clsName.equals(className)) {
                Object object = objByClassName.get(clsName);
                if (object == null) {
                    Class<?> cls = Class.forName(bean.getClassName());
                    object = (Object) cls.newInstance();
                    if (bean.getProperties() != null) {
                        for (Map.Entry<String, Property> entry : bean.getProperties().entrySet()) {
                            if (entry.getValue().getType() == VAL) {
                                parseValue(entry, cls, object);
                            } else if (entry.getValue().getType() == REF) {
                                parseRef(bean, entry, cls, object);
                            }
                        }
                    }
                    objByClassName.put(clsName, object);
                    objByName.put(bean.getId(), object);
                }
                return object;
            }
        }
        return null;
    }

    private void parseRef(Bean bean, Map.Entry<String, Property> entry, Class<?> cls, Object object) throws ClassNotFoundException, CyclicReferenceException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Class[] arg = new Class[1];
        Class<?> cls1 = null;
        Object object1 = null;
        String id1 = entry.getValue().getValue();
        for (Bean bean1 : beans) {
            String objId1 = bean1.getId();
            if (objId1.equals(id1)) {
                cls1 = Class.forName(bean1.getClassName());
                if (bean.getProperties() != null) {
                    for (Map.Entry<String, Property> entry1 : bean.getProperties().entrySet()) {
                        if (entry1.getValue().getType() == REF &&
                                entry1.getValue().getValue().equals(bean.getId())) {
                            throw new CyclicReferenceException();
                        }
                    }
                }
                object1 = getById(objId1);
            }
        }
        arg[0] = cls1;
        Method[] methods = cls.getMethods();
        Method method = null;
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals("set" +
                    entry.getValue().getName().substring(0, 1).toUpperCase() +
                    entry.getValue().getName().substring(1))) {
                method = methods[i];
            }
        }
        method.invoke(object, object1);
    }

    private void parseValue(Map.Entry<String, Property> entry, Class<?> cls, Object object) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class[] arg = new Class[1];
        Object value;
        try {
            value = Integer.parseInt(entry.getValue().getValue());
            arg[0] = int.class;
        } catch (NumberFormatException e) {
            try {
                value = Double.parseDouble(entry.getValue().getValue());
                arg[0] = double.class;
            } catch (NumberFormatException ex) {
                value = entry.getValue().getValue();
                arg[0] = String.class;
            }
        }
        Method[] methods = cls.getMethods();
        Method method = null;
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals("set" +
                    entry.getValue().getName().substring(0, 1).toUpperCase() +
                    entry.getValue().getName().substring(1))) {
                method = methods[i];
            }
        }
        method.invoke(object, value);
    }
}

class CyclicReferenceException extends Exception {

    public CyclicReferenceException() {
    }

    public CyclicReferenceException(String message) {
        super(message);
    }
}
