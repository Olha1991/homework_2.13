package com.example.demo.service;


import com.example.demo.exception.IncorrectFirstNameException;
import com.example.demo.exception.IncorrectLastNameException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;



@Service
public class ValidatorService {

    public String validateFirstName(String firstName){
        if(!StringUtils.isAlpha(firstName)){
            throw new IncorrectFirstNameException();
        }
        return StringUtils.capitalize(firstName.toLowerCase());
    }

    public String validateLastName(String lastName) {
        String[] lastNames = lastName.split(" - ");
        for (int i = 0; i < lastNames.length; i++) {
            String isLastName = lastNames[i];
            if(!StringUtils.isAlpha(isLastName)){
                throw new IncorrectLastNameException();
            }
            lastNames[i] = StringUtils.capitalize(isLastName.toLowerCase());
        }
        return String.join(" - ",lastNames);
    }

}
