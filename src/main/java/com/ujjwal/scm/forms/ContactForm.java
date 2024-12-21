package com.ujjwal.scm.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactForm {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Email(message = "Invalid email address")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(regexp = "^[0-9]{10}$", message = "Invalid phone number")
    private String phoneNumber;

    @NotBlank(message = "Address cannot be empty")
    private String address;


    private String description;
    private boolean favourite;
    private String websiteLink;
    private String linkedinLink;
    private MultipartFile contactImage;

}
