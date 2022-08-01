package com.alwertus.spassistent.parts.feeding.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "feeding_props")
public class FeedingProperties {
    @Id
    String id;

    @Column(name = "interval_hour", nullable = false, columnDefinition = "integer default 2")
    int intervalHour = 2;

    @Column(name = "interval_min", nullable = false, columnDefinition = "integer default 0")
    int intervalMin = 0;
}
