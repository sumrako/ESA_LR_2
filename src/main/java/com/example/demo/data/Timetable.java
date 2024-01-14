package com.example.demo.data;

import com.example.demo.dto.TimetableDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Table(name = "timetable")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Timetable {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(targetEntity = Performance.class, fetch = FetchType.EAGER, optional = false, cascade={CascadeType.MERGE})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "performance_id")
    private Performance performance;

    @Column(name = "hall")
    private int hall;

    @Column(name = "date")
    private LocalDateTime date;

    public TimetableDto toDto() {
        return TimetableDto.builder()
                .id(this.id)
                .performance(this.performance.toDto())
                .hall(this.hall)
                .date(this.date)
                .build();
    }
}
