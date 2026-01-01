package com.function;

import com.model.Employee;
import com.model.ErrorResponse;
import com.util.DatabaseUtil;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

public class ProcessEmployeeFunction {

    @FunctionName("processEmployee")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {
                    HttpMethod.POST }, authLevel = AuthorizationLevel.ANONYMOUS, route = "employees/process") HttpRequestMessage<Employee> request,
            ExecutionContext context) {

        Employee emp = request.getBody();

        if (emp == null || emp.getName() == null || emp.getEmail() == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .header("Content-Type", "application/json")
                    .body(new ErrorResponse("Invalid employee data"))
                    .build();
        }

        try (Connection conn = DatabaseUtil.getConnection()) {

            String sql = """
                        INSERT INTO employees (name, email, department, salary)
                        VALUES (?, ?, ?, ?)
                    """;

            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, emp.getName());
            stmt.setString(2, emp.getEmail());
            stmt.setString(3, emp.getDepartment());
            stmt.setDouble(4, emp.getSalary());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                emp.setId(rs.getLong(1));
            }

            context.getLogger().info("Employee saved with id: " + emp.getId());

            return request.createResponseBuilder(HttpStatus.CREATED)
                    .header("Content-Type", "application/json")
                    .body(emp)
                    .build();

        } catch (Exception e) {
            context.getLogger().severe("DB error: " + e.getMessage());
            context.getLogger().severe(e.toString());

            for (StackTraceElement ste : e.getStackTrace()) {
                context.getLogger().severe(ste.toString());
            }
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("Content-Type", "application/json")
                    .body(new ErrorResponse("Failed to save employee"))
                    .build();
        }
    }
}
