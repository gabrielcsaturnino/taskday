package com.example.taskday.domain.model;
import java.util.List;
import com.example.taskday.domain.model.auxiliary.Address;
import com.example.taskday.domain.model.auxiliary.Cpf;
import com.example.taskday.domain.model.auxiliary.DateOfBirthday;
import com.example.taskday.domain.model.auxiliary.Email;
import com.example.taskday.domain.model.auxiliary.Phone;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table(name = "client")
@Entity
public class Client {
    
    @Id
    @Column(name = "id_client")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "first_name", nullable = false)
    private String first_name;
    @Column(name = "last_name", nullable = false)
    private String last_name;

    
    @Column(name = "phone", nullable = false)
    @Embedded
    private Phone phone;
    
    @Column(name = "email", nullable = false)
    @Embedded
    private Email email;
    
    @Column(name = "cpf_client", nullable = false, unique = true)
    @Embedded
    private Cpf cpf;
    
    @Column(name = "hash_password", nullable = false)
    private String password;

    @Column(name = "status_account", nullable = false)
    private boolean status_account;

    @Column(name = "rg_client", nullable = false, unique = true)
    private String rg_doc;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses;

    @Embedded
    private DateOfBirthday dateOfBirthday;
    
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Job> jobs;

    
    public Client(){}

    public Client(String first_name, String last_name, Phone phone, Email email, Cpf cpf, String password, boolean status_account, String rg_doc, List<Address> addresses, DateOfBirthday dateOfBirthday) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone = phone;
        this.email = email;
        this.cpf = cpf;
        this.password = password;
        this.status_account = status_account;
        this.rg_doc = rg_doc;
        this.addresses = addresses;
        this.dateOfBirthday = dateOfBirthday;
    }


    






}
