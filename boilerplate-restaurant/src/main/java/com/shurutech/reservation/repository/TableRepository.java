package com.shurutech.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shurutech.reservation.entity.RestaurantTable;

@Repository
public interface TableRepository
        extends JpaRepository<RestaurantTable, Long> {
}
