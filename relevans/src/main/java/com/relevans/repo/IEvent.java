package com.relevans.repo;

import com.relevans.repo.model.EventModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IEvent extends JpaRepository<EventModel,Integer> {
    List<EventModel> findByEstado(String estado);
}
