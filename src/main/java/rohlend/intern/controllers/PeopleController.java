package rohlend.intern.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rohlend.intern.dao.PersonDAO;

@RestController
@RequestMapping("/people")
public class PeopleController {
    private final PersonDAO personDAO;

    @Autowired
    public PeopleController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String getPeople(@RequestHeader(value = "name",required = false) String name){
        if(name!=null){
            return personDAO.getPeopleByName(name);
        }
        return "hello";
    }

    @GetMapping("/stat")
    public String getStats(){
        return personDAO.getMapJson();
    }

}