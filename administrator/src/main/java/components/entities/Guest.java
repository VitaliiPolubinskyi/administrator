package components.entities;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "guest")
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "passport", unique = true)
    private String passport;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "notes")
    private String notes;

    @OneToMany(mappedBy = "guestId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookingTable> bookings;



}
