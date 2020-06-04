package com.laptrinhjavaweb.repository.custom.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.laptrinhjavaweb.builder.BuildingSearchBuilder;
import com.laptrinhjavaweb.entity.BuildingEntity;
import com.laptrinhjavaweb.repository.custom.BuildingRepositoryCustom;

@Repository
public class BuildingRepositoryImpl implements BuildingRepositoryCustom{

	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BuildingEntity> findAll(Map<String, Object> params, Pageable pageable,
			BuildingSearchBuilder fieldSearch) {

		StringBuilder sqlSearch = new StringBuilder(" SELECT * from building A ");
		if(StringUtils.isNotBlank(fieldSearch.getStaffId())){
			sqlSearch.append(" INNER JOIN assignmentstaff assignmentstaff ON A.id = assignmentstaff.buildingid ");
		}
		sqlSearch.append(" WHERE 1=1 ");
		sqlSearch = this.createSQLfindAll(sqlSearch, params);
		String sqlSpecial = buildSqlSpecial(fieldSearch);	
		sqlSearch.append(sqlSpecial);
		Query query = entityManager.createNativeQuery(sqlSearch.toString(), BuildingEntity.class);
		query.setFirstResult((int) pageable.getOffset());
		query.setMaxResults(pageable.getPageSize());
		return query.getResultList();
	}
	private String buildSqlSpecial(BuildingSearchBuilder fieldSearch) {
		StringBuilder result = new StringBuilder();
		if(StringUtils.isNotBlank(fieldSearch.getCostRentFrom())){
			result.append(" AND A.costrent >= "+fieldSearch.getCostRentFrom()+"");
		}
		if(StringUtils.isNotBlank(fieldSearch.getCostRentTo())){
			result.append(" AND A.costrent <= "+fieldSearch.getCostRentTo()+"");
		}
		if(fieldSearch.getBuildingTypes().length > 0){
			result.append(" AND ( ");
			
			String buildingTypeSql = Arrays.stream(fieldSearch.getBuildingTypes()).map(item -> " A.type like '%"+item+"%' ").collect(Collectors.joining(" OR "));
						
			result.append(buildingTypeSql + ")");
		}
		if(StringUtils.isNotBlank(fieldSearch.getRentAreaFrom()) || StringUtils.isNotBlank(fieldSearch.getRentAreaTo())){
			result.append(" AND EXISTS (SELECT * FROM rentarea ra WHERE (ra.buildingid = A.id ");
			
			if(StringUtils.isNotBlank(fieldSearch.getRentAreaFrom())) {
				result.append(" AND ra.value >= "+fieldSearch.getRentAreaFrom()+" ");
			}
			if(StringUtils.isNotBlank(fieldSearch.getRentAreaTo())) {
				result.append(" AND ra.value <= "+fieldSearch.getRentAreaTo()+" ");
			}
			result.append("))");			
		}
		if(StringUtils.isNotBlank(fieldSearch.getStaffId())){
			result.append(" AND assignmentstaff.staffid = "+fieldSearch.getStaffId()+" ");
		}
		return result.toString();
	}
	protected StringBuilder createSQLfindAll(StringBuilder where, Map<String, Object> params) {		
		if (params != null && params.size() > 0) {
			String[] keys = new String[params.size()];
			Object[] values = new Object[params.size()];
			int i = 0;
			for (Map.Entry<String, Object> item : params.entrySet()) {
				keys[i] = item.getKey();
				values[i] = item.getValue();
				i++;
			}
			for (int i1 = 0; i1 < keys.length; i1++) {
				if (values[i1] instanceof String && StringUtils.isNotBlank(values[i1].toString())) {
					where.append(" AND LOWER(A." + keys[i1] + ") LIKE '%" + values[i1].toString() + "%' ");
				} else if (values[i1] instanceof Integer && (values[i1] != null)) {
					where.append(" AND LOWER(A." + keys[i1] + ") = " + values[i1] + " ");
				} else if (values[i1] instanceof Long && (values[i1] != null)) {
					where.append(" AND LOWER(A." + keys[i1] + ") = " + values[i1] + " ");
				}
			}
		}
		return where;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<BuildingEntity> findAll(Map<String, Object> params, BuildingSearchBuilder fieldSearch) {
		StringBuilder sqlSearch = new StringBuilder(" SELECT * from building A ");
		if(StringUtils.isNotBlank(fieldSearch.getStaffId())){
			sqlSearch.append(" INNER JOIN assignmentstaff assignmentstaff ON A.id = assignmentstaff.buildingid ");
		}
		sqlSearch.append(" WHERE 1=1 ");
		sqlSearch = this.createSQLfindAll(sqlSearch, params);
		String sqlSpecial = buildSqlSpecial(fieldSearch);	
		sqlSearch.append(sqlSpecial);
		Query query = entityManager.createNativeQuery(sqlSearch.toString(), BuildingEntity.class);
		return query.getResultList();
	}
	@Override
	public int getCountAllItems(Map<String, Object> params, BuildingSearchBuilder fieldSearch) {
		StringBuilder sqlSearch = new StringBuilder(" SELECT count(*) from building A ");
		if(StringUtils.isNotBlank(fieldSearch.getStaffId())){
			sqlSearch.append(" INNER JOIN assignmentstaff assignmentstaff ON A.id = assignmentstaff.buildingid ");
		}
		sqlSearch.append(" WHERE 1=1 ");
		sqlSearch = this.createSQLfindAll(sqlSearch, params);
		String sqlSpecial = buildSqlSpecial(fieldSearch);	
		sqlSearch.append(sqlSpecial);
		Query query = entityManager.createNativeQuery(sqlSearch.toString());
		return ((Number) query.getSingleResult()).intValue();
	}
}
