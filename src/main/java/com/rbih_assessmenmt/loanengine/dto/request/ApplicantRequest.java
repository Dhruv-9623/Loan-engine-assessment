package com.rbih_assessmenmt.loanengine.dto.request;
import com.rbih_assessmenmt.loanengine.domain.enums.EmploymentType;
import jakarta.validation.constraints.*;

public class ApplicantRequest {

    @NotBlank
    private String name;

    @Min(21) @Max(60)
    private int age;

    @DecimalMin(value = "0.01")
    private double monthlyIncome;

    @NotNull
    private EmploymentType employmentType;

    @Min(300) @Max(900)
    private int creditScore;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public double getMonthlyIncome() { return monthlyIncome; }
    public void setMonthlyIncome(double monthlyIncome) { this.monthlyIncome = monthlyIncome; }

    public EmploymentType getEmploymentType() { return employmentType; }
    public void setEmploymentType(EmploymentType employmentType) { this.employmentType = employmentType; }

    public int getCreditScore() { return creditScore; }
    public void setCreditScore(int creditScore) { this.creditScore = creditScore; }
}