package components.entities;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import java.util.List;


@Getter
@Setter

@Entity
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "room_number", nullable = false, unique = true)
    private String roomNumber;


    @Column(name = "class_type", nullable = false)
    private String classType;


    @DecimalMin(value = "0.0", inclusive = false, message = "Should be a positive decimal number")
    @Column(name = "price", nullable = false)
    private Double price;


    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @OneToMany(mappedBy = "roomId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookingTable> bookings;

}
