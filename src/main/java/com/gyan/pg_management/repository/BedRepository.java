package com.gyan.pg_management.repository;

import com.gyan.pg_management.entity.Bed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BedRepository extends JpaRepository<Bed, Long> {

    @Query("""
        select count(b)
        from Bed b
        where b.room.id = ?1
    """)
    Long getTotalBedCountOfRoom(Long roomId);
}

