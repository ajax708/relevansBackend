package com.relevans.repo;

import com.relevans.repo.model.EventModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEvent extends JpaRepository<EventModel,Integer> {
}
