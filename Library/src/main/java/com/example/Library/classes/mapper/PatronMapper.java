package com.example.Library.classes.mapper;

import com.example.Library.classes.dto.PatronDTO;
import com.example.Library.model.table.Patron;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PatronMapper {
    @Mapping(target = "id", source = "id")
    PatronDTO toDTO(Patron patron);
    @Mapping(target = "id", source = "id")
    Patron toEntity(PatronDTO patronDTO);
}