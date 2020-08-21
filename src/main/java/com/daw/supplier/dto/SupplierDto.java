package com.daw.supplier.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SupplierDto {

	@NotNull(message = "Supplier Name cannot be null")
	@Size(min = 1, max = 50, message = "Minimum 1 characters")
	private String supplierName;

	@NotNull(message = "Phone No cannot be null")
	@Size(min = 1, max = 20, message = "Minimum 1 characters")
	private String phoneNo;
	
	@Email(message = "Email should be valid")
	@Size(max = 50, message = "Maximum 50 characters")
	private String email;

}
