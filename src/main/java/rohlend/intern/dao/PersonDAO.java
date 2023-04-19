package rohlend.intern.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import rohlend.intern.models.Person;
import java.io.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Component
public class PersonDAO {


    private final ResourceLoader resourceLoader;

    private final HashMap<String, Integer> map;
    public PersonDAO(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        map = new HashMap<>();
    }

    public HashMap<String, Integer> getMap() {
        return map;
    }
    public String getMapJson(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String getPeopleByName(String name){
        map.put(name,map.getOrDefault(name,0)+1);
        List<Person> people = new ArrayList<>();
        Resource resource = resourceLoader.getResource("classpath:/static/names.txt");
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(resource.getFile()))){
            String[] line;
            while (bufferedReader.ready()){
                line = bufferedReader.readLine().split("_");
                if(name.equals(line[0])){
                    Person person = new Person(name,Integer.parseInt(line[1]));
                    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                    return ow.writeValueAsString(person);
                }
                people.add(new Person(line[0],Integer.parseInt(line[1])));
            }
        }
        catch (IOException e){
            System.out.println("File not found");
        }
        return String.format("{\n\"name\" : \"Not found\"\n\"age\" : \"%d\"\n}",new Random().nextInt());
    }
}