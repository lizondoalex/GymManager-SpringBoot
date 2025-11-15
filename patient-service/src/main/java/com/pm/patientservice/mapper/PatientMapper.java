package com.pm.patientservice.mapper;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.model.Patient;

import java.time.LocalDate;

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

    public static Patient toModel(PatientRequestDTO patientRequestDTO){
        Patient result = new Patient();

        result.setName(patientRequestDTO.getName());
        result.setEmail(patientRequestDTO.getEmail());
        result.setAddress(patientRequestDTO.getAddress());
        result.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        result.setRegisterDate(result.getDateOfBirth());

        return result;
    }
}
