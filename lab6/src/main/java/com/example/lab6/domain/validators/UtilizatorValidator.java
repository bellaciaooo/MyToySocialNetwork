package com.example.lab6.domain.validators;

import com.example.lab6.domain.Utilizator;


public class UtilizatorValidator implements Validator<Utilizator> {
    @Override
    public void validate(Utilizator entity) throws ValidationException {
        if(entity.getId() < 0){
            throw new ValidationException("ID-ul nu poate sa fie mai mic de 0.");
        }
        if(entity.getId() == null){
            throw new ValidationException("ID-ul nu poate sa fie nul.");
        }
        if(entity.getFirstName() == null){
            throw new ValidationException("Primul nume nu poate sa fie nul.");
        }
        if(entity.getLastName() == null){
            throw new ValidationException("Al doilea nume nu poate sa fie nul.");
        }
    }
}
