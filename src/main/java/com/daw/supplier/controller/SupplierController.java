package com.daw.supplier.controller;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.daw.supplier.dto.SupplierDto;
import com.daw.supplier.exception.ResourceNotFoundException;
import com.daw.supplier.model.DataToken;
import com.daw.supplier.model.Supplier;
import com.daw.supplier.repository.SupplierRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class SupplierController {

	@Autowired
	private SupplierRepository supplierRepository;

	private final String TEMPLATE_NOT_FOUND = "Supplier not found for id = ";
	private final String PATH = "/supplier";

	@GetMapping(PATH)
	public Page<Supplier> getAllSupplier(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Page<Supplier> findAll = supplierRepository.findAll(PageRequest.of(page, size));
		return (Page<Supplier>) findAll;
	}

	@GetMapping(PATH + "/{id}")
	public ResponseEntity<Supplier> get(@PathVariable Long id) throws ResourceNotFoundException {
		Supplier supplier = supplierRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(TEMPLATE_NOT_FOUND + id));
		return ResponseEntity.ok().body(supplier);
	}

	@PostMapping(PATH)
	public ResponseEntity<Supplier> createSupplier(@Valid @RequestBody SupplierDto supplierDto) {
		Supplier supplier = new Supplier();
		supplier.setSupplierName(supplierDto.getSupplierName());
		supplier.setPhoneNo(supplierDto.getPhoneNo());
		supplier.setEmail(supplierDto.getEmail());

		supplier.setCreatedOn(new Date());
		supplier.setCreatedBy(
				((DataToken) SecurityContextHolder.getContext().getAuthentication().getDetails()).getId());
		return ResponseEntity.ok(supplierRepository.save(supplier));
	}

	@PutMapping(PATH + "/{id}")
	public ResponseEntity<Supplier> updateSupplier(@PathVariable Long id, @Valid @RequestBody SupplierDto supplierDto)
			throws ResourceNotFoundException {
		Supplier supplier = supplierRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(TEMPLATE_NOT_FOUND + id));
		supplier.setSupplierName(supplierDto.getSupplierName());
		supplier.setPhoneNo(supplierDto.getPhoneNo());
		supplier.setEmail(supplierDto.getEmail());

		supplier.setUpdatedOn(new Date());
		supplier.setUpdatedBy(
				((DataToken) SecurityContextHolder.getContext().getAuthentication().getDetails()).getId());
		return ResponseEntity.ok(supplierRepository.save(supplier));
	}

	@DeleteMapping(PATH + "/{id}")
	public ResponseEntity<?> deleteSupplier(@PathVariable Long id) throws ResourceNotFoundException {
		Supplier supplier = supplierRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(TEMPLATE_NOT_FOUND + id));
		supplierRepository.delete(supplier);
		return ResponseEntity.noContent().build();
	}

}
