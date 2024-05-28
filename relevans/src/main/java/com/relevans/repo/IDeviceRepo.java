package com.relevans.repo;

import com.relevans.repo.model.DeviceModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDeviceRepo extends JpaRepository<DeviceModel, Integer> {
}
