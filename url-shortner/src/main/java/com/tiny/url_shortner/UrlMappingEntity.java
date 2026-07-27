package com.tiny.url_shortner;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "url_mapping", indexes = {
        @Index(name = "id_short_url", columnList = "shortUrl")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrlMappingEntity {

    @Id
    private long id;

    @Column(nullable = false, unique = true, length = 7)
    private String shortUrl;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String longUrl;

    @Column(nullable = false)
    private LocalDateTime createdAt;

}
