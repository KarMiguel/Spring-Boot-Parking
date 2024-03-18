package io.github.KarMiguel.parkingapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "client_has_vacancy")
@EntityListeners(AuditingEntityListener.class)

public class ClientVacancy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "number_receipt",nullable = false,length = 15)
    private String receipt;
    @Column(nullable = false,length = 8)
    private String plate;
    @Column(nullable = false,length = 45)
    private String brand;
    @Column(nullable = false,length = 8)
    private String model;
    @Column(nullable = false,length = 8)
    private String color;
    @Column(name = "date_entry",nullable = false)
    private LocalDateTime dateEntry;
    @Column(name = "date_exit")
    private LocalDateTime dateExit;
    @Column(columnDefinition = "decimal(7,2)")
    private BigDecimal price;
    @Column(columnDefinition = "decimal(7,2)")
    private BigDecimal discount;
    @Column(name = "total",columnDefinition = "decimal(7,2)")
    private BigDecimal total;
    @ManyToOne
    @JoinColumn(name = "id_client",nullable = false)
    private Client client;
    @ManyToOne
    @JoinColumn(name = "id_vacancy",nullable = false)
    private Vacancy vacancy;

    @CreatedDate
    @Column(name = "date_created")
    private LocalDateTime dateCreated;
    @LastModifiedDate
    @Column(name = "date_modification")
    private  LocalDateTime dateModification;
    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;
    @LastModifiedBy
    @Column(name = "modified_by")
    private  String modifiedBy;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientVacancy that = (ClientVacancy) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
