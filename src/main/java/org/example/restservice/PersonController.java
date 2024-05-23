package org.example.restservice;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class PersonController {

    private PersonRepository dataRepo = new PersonRepositoryImpl();

    @GetMapping("/persons/{id}")
    public EntityModel<Person> getPerson(@PathVariable int id) {
        System.out.println("...called GET person");
        Person person = dataRepo.getPerson(id);
//        return ResponseEntity
//                .status(HttpStatus.FOUND)
//                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .body(dataRepo.getPerson(id));
        EntityModel<Person> em = EntityModel.of(person);
        em.add(linkTo(methodOn(PersonController.class).getPerson(id)).withSelfRel());
        if(person.getPersonStatus() == PersonStatus.ACTIVE) {
            em.add(linkTo(methodOn(PersonController.class).deletePerson(id)).withRel("delete"),
                    linkTo(methodOn(PersonController.class).hirePerson(id)).withRel("hire"));
        } else if(person.getPersonStatus() == PersonStatus.HIRED){
            em.add(linkTo(methodOn(PersonController.class).vacatePerson(id)).withRel("vacate"));
        }
        em.add(linkTo(methodOn(PersonController.class).getAllPersons()).withRel("list all"));
        return em;
    }

    @GetMapping("/persons/")
    public CollectionModel<EntityModel<Person>> getAllPersons() {
        System.out.println("...called GET personsList");
//        return ResponseEntity
//                .status(HttpStatus.FOUND)
//                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .body(dataRepo.getAllPersons());
        List<EntityModel<Person>> persons =
                dataRepo.getAllPersons().stream().map( person ->
                        EntityModel.of(person,
                                linkTo(methodOn(PersonController.class).getPerson(person.getId())).withSelfRel(),
                                linkTo(methodOn(PersonController.class).deletePerson(person.getId())).withRel("delete"),
                                linkTo(methodOn(PersonController.class).getAllPersons()).withRel("list all")
                        )
                ).collect(Collectors.toList());

        return CollectionModel.of(persons,
                linkTo(methodOn(PersonController.class).getAllPersons()).withSelfRel());
    }

    @PostMapping("/persons/")
    public ResponseEntity<Person> addPerson(@RequestBody Person person){
        System.out.println("...called POST addPerson");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(dataRepo.addPerson(person));
    }

    @DeleteMapping("/persons/{id}")
    public ResponseEntity<Boolean> deletePerson(@PathVariable int id) {
        System.out.println("...called DELETE deletePerson");
        dataRepo.deletePerson(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/persons/")
    public ResponseEntity<Person> updatePerson(@RequestBody Person person) {
        System.out.println("...called PUT updatePerson");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(dataRepo.updatePerson(person));
    }

    @PatchMapping("/persons/{id}/deactivate")
    public ResponseEntity<?> deactivatePerson(@PathVariable int id) {
        Person person = dataRepo.getPerson(id);
        PersonStatus personStatus = person.getPersonStatus();
        if(personStatus == PersonStatus.ACTIVE) {
            person.setPersonStatus(PersonStatus.NOT_ACTIVE);

            EntityModel<Person> resource = EntityModel.of(person,
                    linkTo(methodOn(PersonController.class).getPerson(id)).withSelfRel(),
                    linkTo(methodOn(PersonController.class).activatePerson(id)).withRel("activate"),
                    linkTo(methodOn(PersonController.class).getAllPersons()).withRel("list all")
            );

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(resource);
        } else {
            throw new ConflictEx("You can't deactivate a person with status " + personStatus);
        }
    }

    @PatchMapping("/persons/{id}/activate")
    public ResponseEntity<?> activatePerson(@PathVariable int id) {
        Person person = dataRepo.getPerson(id);
        PersonStatus personStatus = person.getPersonStatus();
        if(personStatus == PersonStatus.NOT_ACTIVE) {
            person.setPersonStatus(PersonStatus.ACTIVE);

            EntityModel<Person> resource = EntityModel.of(person,
                    linkTo(methodOn(PersonController.class).getPerson(id)).withSelfRel(),
                    linkTo(methodOn(PersonController.class).deactivatePerson(id)).withRel("deactivate"),
                    linkTo(methodOn(PersonController.class).getAllPersons()).withRel("list all")
            );

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(resource);
        } else {
            throw new ConflictEx("You can't activate a person with status " + personStatus);
        }
    }

    @PatchMapping("/persons/{id}/hire")
    public ResponseEntity<?> hirePerson(@PathVariable int id) {
        Person person = dataRepo.getPerson(id);
        PersonStatus personStatus = person.getPersonStatus();
        if(personStatus == PersonStatus.ACTIVE) {
            person.setPersonStatus(PersonStatus.HIRED);

            EntityModel<Person> resource = EntityModel.of(person,
                    linkTo(methodOn(PersonController.class).getPerson(id)).withSelfRel(),
                    linkTo(methodOn(PersonController.class).vacatePerson(id)).withRel("vacate"),
                    linkTo(methodOn(PersonController.class).getAllPersons()).withRel("list all")
            );

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(resource);
        } else {
            throw new ConflictEx("You can't hire a person with status " + personStatus);
        }
    }

    @PatchMapping("/persons/{id}/vacate")
    public ResponseEntity<?> vacatePerson(@PathVariable int id) {
        Person person = dataRepo.getPerson(id);
        PersonStatus personStatus = person.getPersonStatus();
        if(personStatus == PersonStatus.HIRED) {
            person.setPersonStatus(PersonStatus.ACTIVE);

            EntityModel<Person> resource = EntityModel.of(person,
                    linkTo(methodOn(PersonController.class).getPerson(id)).withSelfRel(),
                    linkTo(methodOn(PersonController.class).hirePerson(id)).withRel("hire"),
                    linkTo(methodOn(PersonController.class).deletePerson(id)).withRel("delete"),
                    linkTo(methodOn(PersonController.class).getAllPersons()).withRel("list all")
            );

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(resource);
        } else {
            throw new ConflictEx("You can't hire a person with status " + personStatus);
        }
    }
}
