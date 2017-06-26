package track.container;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import track.container.config.Bean;
import track.container.config.ConfigReader;
import track.container.config.InvalidConfigurationException;
import track.lections.lection4.Item;
import track.lections.lection4.Person;

/**
 * TODO: Реализовать
 */
public class JsonConfigReader implements ConfigReader {

    @Override
    public List<Bean> parseBeans(File configFile) throws InvalidConfigurationException {

        ObjectMapper mapper = new ObjectMapper();
        Beans beans = null;
        try {
            beans = mapper.readValue(configFile, Beans.class);
            return Arrays.asList(beans.getBeans());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }


}

class Beans {
    private Bean[] beans;

    public Beans() {
    }

    public Bean[] getBeans() {
        return beans;
    }
}
