package com.example.Library.classes.mapper;

import com.example.Library.classes.dto.BookDTO;
import com.example.Library.model.table.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookMapper {
    @Mapping(target = "id", source = "id")
    BookDTO toDTO(Book book);
    @Mapping(target = "id", source = "id")
    Book toEntity(BookDTO bookDTO);
}