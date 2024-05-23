package org.example.restservice;

import java.util.List;

public interface PersonRepository {
    List<Person> getAllPersons();
    Person getPerson(int id) throws PersonNotFoundEx;
    Person updatePerson(Person person) throws PersonNotFoundEx;
    Person addPerson(Person person) throws PersonExistsEx;
    boolean deletePerson(int id) throws PersonNotFoundEx;
}
