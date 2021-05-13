package com.kpt.employee;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.data.map.repository.config.EnableMapRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
@EnableMapRepositories
public class EmployeeService {
    private final CrudRepository<Employee, Long> repository;
    
    public Iterable<Employee> findAll() {
        return repository.findAll();
    }

    public Optional<Employee> findById(final Long id) {
        return repository.findById(id);
    }

    public Employee create(final Employee employee) {
        final var copy = new Employee();
        return repository.save(copy.createOrUpdate(employee));
    }

    public Optional<Employee> update(final Long id, final Employee employee) {
        return repository.findById(id)
                         .map(old->old.createOrUpdate(employee))
                         .map(repository::save);
    }

    public void delete(final Long id) {
        repository.deleteById(id);
    }

    public void loadInitialEmployees() {
        final var mapper = new ObjectMapper();
		final var typeReference = new TypeReference<List<Employee>>(){};
		final var inputStream = EmployeeService.class.getResourceAsStream("/json/employees.json");
		try {
			final var employees = mapper.readValue(inputStream, typeReference);
			for (final var employee : employees) {
				create(employee);
			}
			log.info("Employees Saved!");
		} catch (IOException e){
			log.error("Unable to save Employees: ", e.getMessage());
		}
    }
}
