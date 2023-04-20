package rohlend.intern.dao;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import rohlend.intern.models.Person;
import java.io.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;


import java.util.HashMap;


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
        StringBuilder res = new StringBuilder("{ \"stats\":[");
        for(var pair : map.entrySet()){
            res.append(String.format("{ \"name\" : \"%s\",",pair.getKey()))
                    .append(String.format("\"age\":\"%d\"},",pair.getValue()));
        }
        res.deleteCharAt(res.length());
        res.append("]}");
        return res.toString();
    }

    public String getPeopleByName(String name){
        if(name.equals("")) return String.format("{\"name\" : \"Not found\",\"age\" : \"%d\"}",(int)(Math.random()*101));
        map.put(name,map.getOrDefault(name,0)+1);
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
            }
        }
        catch (IOException e){
            System.out.println("File not found");
        }
        return String.format("{\"name\" : \"Not found\",\"age\" : \"%d\"}",(int)(Math.random()*101));
    }
}