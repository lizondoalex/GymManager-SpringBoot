package com.pm.patientservice.mapper;

import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.model.Patient;

public class PatientMapper {
    public static PatientResponseDTO toDTO(Patient patient){

        PatientResponseDTO result = new PatientResponseDTO();

        result.setId(patient.getId().toString());
        result.setName(patient.getName());
        result.setEmail(patient.getEmail());
        result.setAddress(patient.getAddress());
        result.setDateOfBirth(patient.getDateOfBirth().toString());

        return result;
    }
}
