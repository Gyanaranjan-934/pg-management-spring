package com.gyan.pg_management.repository;

import com.gyan.pg_management.entity.Bed;
import com.gyan.pg_management.enums.BedStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface BedRepository extends JpaRepository<Bed, Long> {

    @Query("""
        select count(b)
        from Bed b
        where b.room.id = ?1
    """)
    Long getTotalBedCountOfRoom(Long roomId);

    Long countByIdAndStatusIn(Long id, List<BedStatus> statuses);


}

