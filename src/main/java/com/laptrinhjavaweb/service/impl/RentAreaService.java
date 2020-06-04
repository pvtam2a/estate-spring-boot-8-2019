package com.laptrinhjavaweb.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.laptrinhjavaweb.dto.BuildingDTO;
import com.laptrinhjavaweb.entity.BuildingEntity;
import com.laptrinhjavaweb.entity.RentAreaEntity;
import com.laptrinhjavaweb.repository.RentAreaRepository;
import com.laptrinhjavaweb.service.IRentAreaService;

@Service
public class RentAreaService implements IRentAreaService{

	@Autowired RentAreaRepository rentAreaRepository;
	@Override
	public RentAreaEntity save(RentAreaEntity rentAreaEntity) {		
		return rentAreaRepository.save(rentAreaEntity);
	}

	@Override
	@Transactional
	public void save(BuildingDTO buildingDTO, BuildingEntity buildingEntity) {
//		String[] arr = buildingDTO.getRentArea().split("\\,");	
//		List<RentAreaEntity> lists = new ArrayList<>();
//		for (String item : arr) {
//			if(StringUtils.isNotBlank(item)) {
//				RentAreaEntity rentAreaEntity = new RentAreaEntity();
//				rentAreaEntity.setValue(Integer.parseInt(item.trim()));
//				rentAreaEntity.setBuilding(buildingEntity);
//				lists.add(rentAreaEntity);
//			}			
//		}
//		rentAreaRepository.save(lists);
	}

}
