package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.exception.EmptyListException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilsService {

    protected void isEmptyCollection(List<?> list, String elementNotFound){
        if(list.isEmpty())
            throw new EmptyListException(elementNotFound);
    }


}
