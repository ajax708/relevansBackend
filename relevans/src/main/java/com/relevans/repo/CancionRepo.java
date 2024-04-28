package com.relevans.repo;

import com.relevans.repo.model.CancionModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CancionRepo extends JpaRepository<CancionModel,Integer> {

    public List<CancionModel> findCancionModelByIdPlayList(Integer idPlayList);
}
