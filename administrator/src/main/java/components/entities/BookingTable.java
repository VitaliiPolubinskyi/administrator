package components.entities;


import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDate;

@Getter
@Setter

@Entity
@Table(name = "bookingtable")
public class BookingTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room roomId;

    @ManyToOne
    @JoinColumn(name = "guest_id")
    private Guest guestId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "Use yyyy-MM-dd format! Should be not in the past!")
    @Column(name = "check_in", nullable = false)
    private LocalDate checkIn;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "Use yyyy-MM-dd format! Should be not in the past!")
    @Column(name = "check_out", nullable = false)
    private LocalDate checkOut;

}
