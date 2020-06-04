package com.laptrinhjavaweb.service.impl;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.laptrinhjavaweb.builder.BuildingSearchBuilder;
import com.laptrinhjavaweb.converter.BuildingConverter;
import com.laptrinhjavaweb.dto.BuildingDTO;
import com.laptrinhjavaweb.entity.BuildingEntity;
import com.laptrinhjavaweb.enums.BuildingTypesEnum;
import com.laptrinhjavaweb.enums.DistrictsEnum;
import com.laptrinhjavaweb.repository.BuildingRepository;
import com.laptrinhjavaweb.repository.RentAreaRepository;
import com.laptrinhjavaweb.service.IBuildingService;
import com.laptrinhjavaweb.service.IRentAreaService;

@Service
public class BuildingService implements IBuildingService {

	@Autowired
	private BuildingConverter buildingConverter;

	@Autowired
	private BuildingRepository buildingRepository;
	
	@Autowired
	private RentAreaRepository rentAreaRepository;

	@Autowired
	private IRentAreaService rentAreaService;

	@Override
	public List<BuildingDTO> findAll(BuildingSearchBuilder fieldSearch, Pageable pageable) {
		Map<String, Object> properties = convertToMapProperties(fieldSearch);
		List<BuildingEntity> buildingEntities = buildingRepository.findAll(properties, pageable, fieldSearch);
		return buildingEntities.stream().map(item -> buildingConverter.convertToDTO(item))
				.collect(Collectors.toList());
	}
	@Override
	public List<BuildingDTO> findAll(BuildingSearchBuilder fieldSearch) {
		Map<String, Object> properties = convertToMapProperties(fieldSearch);
		List<BuildingEntity> buildingEntities = buildingRepository.findAll(properties, fieldSearch);
		return buildingEntities.stream().map(item -> buildingConverter.convertToDTO(item))
				.collect(Collectors.toList());
	}
	
	@Override
	public BuildingEntity findById(Long id) {		
//		return buildingRepository.findOne(id);
		return null;
	}
	
	@Override
	public int getCountAllItems(BuildingSearchBuilder fieldSearch) {
		Map<String, Object> properties = convertToMapProperties(fieldSearch);
		return buildingRepository.getCountAllItems(properties, fieldSearch);
	}

	@Override
	@Transactional
	public BuildingDTO save(BuildingDTO newBuilding) {
		BuildingEntity buildingEntity = buildingConverter.convertToEntity(newBuilding);
		buildingEntity.setType(buildingConverter.convertBuildingTypeToString(newBuilding.getBuildingTypes()));
		buildingEntity = buildingRepository.save(buildingEntity);
		rentAreaService.save(newBuilding, buildingEntity);
		return buildingConverter.convertToDTO(buildingEntity);
	}

	@Override
	@Transactional
	public BuildingDTO update(BuildingDTO buildingDTO) {
//		BuildingEntity buildingEntity = buildingRepository.findOne(buildingDTO.getId());
//		rentAreaRepository.delete(buildingEntity.getRentAreas());
//		rentAreaService.save(buildingDTO, buildingEntity);	
//		
//		buildingEntity = buildingConverter.convertToEntity(buildingDTO);
//		buildingEntity.setType(buildingConverter.convertBuildingTypeToString(buildingDTO.getBuildingTypes()));
//		buildingRepository.save(buildingEntity);	
//		
//		return buildingConverter.convertToDTO(buildingRepository.findOne(buildingEntity.getId()));		
		return null;
	}

	@Override
	@Transactional
	public void delete(Long[] ids) {
//		for (Long id : ids) {
//			BuildingEntity buildingEntity = buildingRepository.findOne(id);
//			rentAreaRepository.delete(buildingEntity.getRentAreas());
//			buildingRepository.delete(buildingEntity);
//		}
	}

	
	private Map<String, Object> convertToMapProperties(BuildingSearchBuilder fieldSearch) {
		Map<String, Object> properties = new HashMap<>();
		try {
			Field[] fields = BuildingSearchBuilder.class.getDeclaredFields();
			for (Field field : fields) {
				if (!field.getName().equals("buildingTypes") && !field.getName().startsWith("costRent")
						&& !field.getName().startsWith("rentArea") && !field.getName().startsWith("staffId")) {
					field.setAccessible(true);
					if (field.get(fieldSearch) instanceof String) {
						if (field.getName().equals("buildingArea") || field.getName().equals("numberOfBasement")) {
							if (field.get(fieldSearch) != null
									&& StringUtils.isNotBlank((String) field.get(fieldSearch))) {
								properties.put(field.getName().toLowerCase(),
										Integer.parseInt((String) field.get(fieldSearch)));
							}

						} else {
							properties.put(field.getName().toLowerCase(), field.get(fieldSearch));
						}

					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return properties;
	}

	@Override
	public Map<String, String> getDistricts() {
		Map<String, String> districts = new HashMap<>();
		for (DistrictsEnum item : DistrictsEnum.values()) {
			districts.put(item.name(), item.getDistrictValue());
		}
		return districts;
	}

	@Override
	public Map<String, String> getBuildingTypes() {
		Map<String, String> buildingTypes = new HashMap<>();
		for (BuildingTypesEnum item : BuildingTypesEnum.values()) {
			buildingTypes.put(item.name(), item.getBuildingTypeValue());
		}
		return buildingTypes;
	}
	@Override
	public String generateTypes(BuildingEntity buildingEntity) {
		return buildingEntity.getRentAreas().stream().map(e -> e.getValue().toString()).collect(Collectors.joining(", "));
	}
	
}
