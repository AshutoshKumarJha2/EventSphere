package com.cts.eventsphere.repository;

import com.cts.eventsphere.model.ResourceAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceAllocationRepository extends JpaRepository<ResourceAllocation,String> {

    List<ResourceAllocation> findByEvent_EventId(String eventId);
}
