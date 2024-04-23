package com.relevans.repo;

import com.relevans.repo.model.PlayListModel;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IPlayList extends JpaRepository<PlayListModel,Integer> {
}
