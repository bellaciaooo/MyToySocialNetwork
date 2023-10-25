package com.example.lab6.domain.validators;

import com.example.lab6.domain.Prietenie;

public class PrietenieValidator implements Validator<Prietenie> {
    @Override
    public void validate(Prietenie entity) throws ValidationException {
        if(entity.getId1() == null){
            throw new ValidationException("ID-ul utilizatorului 1 nu poate sa fie nul.");
        }
        if(entity.getId2() == null){
            throw new ValidationException("ID-ul utilizatorului 2 nu poate sa fie nul.");
        }
        if(entity.getFriendsFrom() == null){
            throw new ValidationException("Data inceperii prieteniei nu poate sa fie nula.");
        }
    }
}
