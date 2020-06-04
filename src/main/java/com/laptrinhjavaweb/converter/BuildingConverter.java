package com.laptrinhjavaweb.converter;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.laptrinhjavaweb.dto.BuildingDTO;
import com.laptrinhjavaweb.entity.BuildingEntity;
import com.laptrinhjavaweb.entity.RentAreaEntity;
import com.laptrinhjavaweb.enums.BuildingTypesEnum;
import com.laptrinhjavaweb.enums.DistrictsEnum;

@Component
public class BuildingConverter {

	@Autowired
	private ModelMapper modelMapper;
	
	public BuildingDTO convertToDTO(BuildingEntity entity) {						
		BuildingDTO dto = modelMapper.map(entity, BuildingDTO.class);
		dto.setAddress(dto.getStreet() + ", " + dto.getWard() + ", " + DistrictsEnum.getValueByName(dto.getDistrict()));
		dto.setType(generateBuildingType(dto.getType()));
		dto.setRentArea(generateRentArea(entity));
		return dto;		
	}	
	public BuildingEntity convertToEntity(BuildingDTO dto) {	
		BuildingEntity entity = modelMapper.map(dto, BuildingEntity.class);		
		return entity;		
	}
	
	private String generateBuildingType(String type) {
		String value = "";
		if(type != null && type != "") {
			String[] array = type.split("\\,", -1);
			for (int i = 0; i < array.length; i++) {
				for (BuildingTypesEnum item : BuildingTypesEnum.values()) {
					if(item.name().equals(array[i])) {
						if(i > 0) {
							value += "," +  item.getBuildingTypeValue();
						}else {
							value += item.getBuildingTypeValue();
						}
						
					}
				}
			}		
		}		
		return value;
	}
	private String generateRentArea(BuildingEntity entity) {
		String value = "";
		for (RentAreaEntity  item : entity.getRentAreas()) {
			if(StringUtils.isBlank(value)) {
				value += item.getValue().toString();
			}else {
				value += ", " + item.getValue().toString();
			}
		}
		return value;
	}
	public String convertBuildingTypeToString(String[] arrType) {
		StringBuilder type = new StringBuilder();
		for (String item : arrType) {
			if(StringUtils.isNotBlank(type.toString())){
				type.append(",");
			}
			type.append(item);
		}
		return type.toString();
	}
	public String[] convertBuildingTypeToArray(String type) {
		return type.split("\\,");		
	}
}
