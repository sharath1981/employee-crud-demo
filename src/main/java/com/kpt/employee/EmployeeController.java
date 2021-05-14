package com.kpt.employee;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@CrossOrigin("*")
@RestController
@RequestMapping("api/v1/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<Iterable<Employee>> findAll() {
        return ResponseEntity.ok()
                             .body(employeeService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Employee> findById(@PathVariable Long id) {
        return ResponseEntity.of(employeeService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Employee> create(@RequestBody Employee employee) {
        return createEmployee(employee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> update(@PathVariable("id") Long id, @RequestBody Employee employee) {
        return employeeService.update(id, employee)
                              .map(ResponseEntity.ok()::body)
                              .orElseGet(() -> {
                                  return createEmployee(employee);
                              });
    }

    private ResponseEntity<Employee> createEmployee(final Employee employee) {
        final var created = employeeService.create(employee);
        final var location = ServletUriComponentsBuilder.fromCurrentRequest()
                                                        .path("/{id}")
                                                        .buildAndExpand(created.getId())
                                                        .toUri();
        return ResponseEntity.created(location)
                             .body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Employee> delete(@PathVariable("id") Long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent()
                             .build();
    }
    
}
