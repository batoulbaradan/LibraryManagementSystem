package com.example.Library.classes.mapper;

import com.example.Library.classes.dto.BorrowingRecordDTO;
import com.example.Library.model.table.BorrowingRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface BorrowingRecordMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "patronId", source = "patron.id")
    BorrowingRecordDTO toDTO(BorrowingRecord borrowingRecord);
    @Mapping(target = "id", source = "id")
    @Mapping(target = "book.id", source = "bookId")
    @Mapping(target = "patron.id", source ="patronId" )
    BorrowingRecord toEntity(BorrowingRecordDTO borrowingRecordDTO);
}
