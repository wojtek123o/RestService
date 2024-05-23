package org.example.restservice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;


public class PersonRepositoryImpl implements PersonRepository{

    private List<Person> personList;

    public PersonRepositoryImpl() {
        personList = new ArrayList<>();
        personList.add(new Person(1, "Mariusz",9));
        personList.add(new Person(2, "Tomek",8));
        personList.add(new Person(3, "Andrzej",7));
    }

    @Override
    public List<Person> getAllPersons() {
        return personList;
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @Override
    public Person getPerson(int id) throws PersonNotFoundEx {
        for(Person person: personList){
            if(person.getId() == id) {
                return person;
            }
        }
        throw new PersonNotFoundEx(id);
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @Override
    public Person updatePerson(Person updatedPerson) throws PersonNotFoundEx {
        for(int i = 0; i < personList.size(); i++){
            Person person = personList.get(i);
            if(person.getId() == updatedPerson.getId()) {
                updatedPerson.setPersonStatus(person.getPersonStatus());
                personList.set(i, updatedPerson);
                return updatedPerson;
            }
        }
        throw new PersonNotFoundEx(updatedPerson.getId());
    }

    @ResponseStatus(code = HttpStatus.CONFLICT)
    @Override
    public Person addPerson(Person personToAdd) throws PersonExistsEx {
        for(Person person: personList){
            if(person.getId() == personToAdd.getId()) {
                throw new PersonExistsEx(personToAdd.getId());
            }
        }
        Person person = new Person(personToAdd.getId(), personToAdd.getFirstName(), personToAdd.getAge());
        personList.add(person);
        return person;
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @Override
    public boolean deletePerson(int id) throws PersonNotFoundEx {
        for(Person person: personList){
            if(person.getId() == id) {
                personList.remove(person);
                return true;
            }
        }
        throw new PersonNotFoundEx(id);
    }
}
