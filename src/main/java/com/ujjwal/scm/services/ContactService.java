package com.ujjwal.scm.services;

import com.ujjwal.scm.entities.Contact;
import com.ujjwal.scm.entities.User;

import java.util.List;

public interface ContactService {

    Contact save(Contact contact);

    Contact update(Contact contact);

    List<Contact> getAll();

    Contact getById(String id);

    void delete(String id);

    List<Contact> search(String name, String email, String phoneNumber);

    List<Contact> getByUserId(String userId);

    List<Contact> getByUser(User user);
}
