package rohlend.intern.dao;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import rohlend.intern.models.Person;
import java.io.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO {

    private static int id;

    public PersonDAO() {
    }
    public String getPeopleByName(String name){
        List<Person> people = new ArrayList<>();
        ClassPathResource classPathResource = new ClassPathResource("classpath:names.txt");
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(classPathResource.getFile()))){

            String[] line;
            while (bufferedReader.ready()){

                line = bufferedReader.readLine().split("_");
                if(name.equals(line[0])){
                    Person person = new Person(id++,name,Integer.parseInt(line[1]));
                    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                    return ow.writeValueAsString(person);
                }
                people.add(new Person(id++,line[0],Integer.parseInt(line[1])));
            }
        }
        catch (IOException e){
            System.out.println("File not found");
        }
        return "{\n\"message\" : \"Not found\"\n}";
    }
}