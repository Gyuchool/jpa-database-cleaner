package com.gyucheol.jpadatabasecleaner.mysql;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;

@Component
public class DatabaseCleaner {
	private static final Logger log = LoggerFactory.getLogger(DatabaseCleaner.class);

	private static final String OFF_REFERENTIAL_INTEGRITY = "SET REFERENTIAL_INTEGRITY FALSE";
	private static final String ON_REFERENTIAL_INTEGRITY = "SET REFERENTIAL_INTEGRITY TRUE";
	private static final String TRUNCATE_EXEC = "TRUNCATE TABLE ";

	@PersistenceContext
	private EntityManager entityManager;

	private List<String> tableNames;

	@PostConstruct
	public void init() {
		tableNames = entityManager.getMetamodel().getEntities().stream()
			.map(entityType -> getTableName(this.entityManager, entityType.getJavaType()))
			.filter(Objects::nonNull)
			.collect(toList());
	}

	private String getTableName(EntityManager entityManager, Class<?> entityClass) {
		Metamodel metamodel = entityManager.getMetamodel();
		EntityType<?> entityType = metamodel.entity(entityClass);
		Table tableAnnotation = entityType.getJavaType().getAnnotation(Table.class);
		if (tableAnnotation == null) {
			log.warn("table annotation is not exists");
			return null;
		}
		return tableAnnotation.name();
	}

	@Transactional
	public void truncateTable() {
		entityManager.createNativeQuery(OFF_REFERENTIAL_INTEGRITY).executeUpdate();
		tableNames
			.forEach(
				tableName -> entityManager.createNativeQuery(TRUNCATE_EXEC + tableName).executeUpdate());
		entityManager.createNativeQuery(ON_REFERENTIAL_INTEGRITY).executeUpdate();
	}
}
