package com.anand.capstone.repository;

import com.anand.capstone.entity.Employee;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("SELECT e FROM Employee e WHERE (:firstName IS NULL OR e.firstName LIKE :firstName) " +
            "AND (:lastName IS NULL OR e.lastName LIKE :lastName) " +
            "AND (:email IS NULL OR e.email LIKE :email) " +
            "AND (:grade IS NULL OR e.grade LIKE :grade)")
    List<Employee> findByParameters(@Param("firstName") String firstName,
                                    @Param("lastName") String lastName,
                                    @Param("email") String email,
                                    @Param("grade") String grade);

        Employee findByUsername(String username);

        Employee findByEmail(String email);
}
