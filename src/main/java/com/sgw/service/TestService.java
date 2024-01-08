package com.sgw.service;

import com.spring.*;

/**
 * @author sgw
 * @date 2024/01/07 12:51
 **/
@Component
@Lazy
public class TestService implements BeanInterface {
    @Autowired
    private AnimalService animalService;

    private PersonService personService;

    public void test(){
        System.out.println(animalService);
        System.out.println(personService);
    }
}
