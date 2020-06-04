package com.laptrinhjavaweb.service;

import com.laptrinhjavaweb.dto.BuildingDTO;
import com.laptrinhjavaweb.entity.BuildingEntity;
import com.laptrinhjavaweb.entity.RentAreaEntity;

public interface IRentAreaService {
	RentAreaEntity save(RentAreaEntity rentAreaEntity);
	void save(BuildingDTO buildingDTO, BuildingEntity buildingEntity);
}
