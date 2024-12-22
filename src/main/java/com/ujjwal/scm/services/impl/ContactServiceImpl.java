package com.ujjwal.scm.services.impl;

import com.ujjwal.scm.entities.Contact;
import com.ujjwal.scm.entities.User;
import com.ujjwal.scm.helpers.ResourceNotFoundException;
import com.ujjwal.scm.repo.ContactRepo;
import com.ujjwal.scm.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepo contactRepo;

    @Override
    public Contact save(Contact contact) {

        String contactId = UUID.randomUUID().toString();
        contact.setId(contactId);
        return contactRepo.save(contact);

    }

    @Override
    public Contact update(Contact contact) {
        var contactOld = contactRepo.findById(contact.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with given id "+ contact.getId()));

        contactOld.setName(contact.getName());
        contactOld.setFavorite(contact.isFavorite());
        contactOld.setPhoneNumber(contact.getPhoneNumber());
        contactOld.setEmail(contact.getEmail());
        contactOld.setDescription(contact.getDescription());
        contactOld.setAddress(contact.getAddress());
        contactOld.setWebsiteLink(contact.getWebsiteLink());
        contactOld.setLinkedinLink(contact.getLinkedinLink());

        if(contact.getPicture() != null &&
         !contact.getPicture().isEmpty()) {

            contactOld.setPicture(contact.getPicture());
            contactOld.setCloudinaryImagePublicId(contact.getCloudinaryImagePublicId());

        }
        return contactRepo.save(contactOld);

    }

    @Override
    public List<Contact> getAll() {
        return contactRepo.findAll();
    }

    @Override
    public Contact getById(String id) {
        return contactRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Contact not found with given id "+ id)
        );
    }

    @Override
    public void delete(String id) {

        var contact = getById(id);

        contactRepo.delete(contact);

    }

    @Override
    public Page<Contact> searchByName(String nameKeyword, int page, int size, String sortBy, String sortDir, User user) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        var pageable = PageRequest.of(page,size,sort);

        return contactRepo.findByUserAndNameContaining(user,nameKeyword,pageable);

    }

    @Override
    public Page<Contact> searchByEmail(String emailKeyword, int page, int size, String sortBy, String sortDir, User user) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        var pageable = PageRequest.of(page,size,sort);

        return contactRepo.findByUserAndEmailContaining(user,emailKeyword,pageable);
    }

    @Override
    public Page<Contact> searchByPhoneNumber(String phoneNumberKeyword, int page, int size, String sortBy, String sortDir, User user) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        var pageable = PageRequest.of(page,size,sort);

        return contactRepo.findByUserAndPhoneNumberContaining(user,phoneNumberKeyword,pageable);


    }


    @Override
    public List<Contact> getByUserId(String userId) {

        return contactRepo.findByUserId(userId);

    }

    @Override
    public Page<Contact> getByUser(User user, int page, int size,
                                   String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        var pageable = PageRequest.of(page,size,sort);

        return contactRepo.findByUser(user,pageable);
    }
}
