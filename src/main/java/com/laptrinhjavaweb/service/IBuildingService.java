package com.laptrinhjavaweb.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.laptrinhjavaweb.builder.BuildingSearchBuilder;
import com.laptrinhjavaweb.dto.BuildingDTO;
import com.laptrinhjavaweb.entity.BuildingEntity;

public interface IBuildingService {
	List<BuildingDTO> findAll(BuildingSearchBuilder fieldSearch, Pageable pageable);
	List<BuildingDTO> findAll(BuildingSearchBuilder fieldSearch);
	BuildingEntity findById(Long id);
	int getCountAllItems(BuildingSearchBuilder fieldSearch);
	BuildingDTO save(BuildingDTO newBuilding);
	BuildingDTO update(BuildingDTO building);
	void delete(Long[] ids);
	Map<String, String> getDistricts();
	Map<String, String> getBuildingTypes();
	String generateTypes(BuildingEntity buildingEntity);
}
