package com.example.assignment.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.assignment.dto.UsersDto;
import com.example.assignment.entity.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UsersMapperTest {

  private UsersMapper usersMapper;

  @BeforeEach
  void setUp() {
    usersMapper = new UsersMapper();
  }

  @Test
  void toDto_shouldMapUsersToUsersDto() {
    Users users =
        Users.builder()
            .id(1L)
            .name("John Doe")
            .email("johndoe@example.com")
            .password("password123")
            .build();

    UsersDto usersDto = usersMapper.toDto(users);

    assertThat(usersDto).isNotNull();
    assertThat(usersDto.getId()).isEqualTo(users.getId());
    assertThat(usersDto.getName()).isEqualTo(users.getName());
    assertThat(usersDto.getEmail()).isEqualTo(users.getEmail());
    assertThat(usersDto.getPassword()).isNull();
  }

  @Test
  void toEntity_shouldMapUsersDtoToUsers() {
    UsersDto usersDto =
        UsersDto.builder()
            .id(1L)
            .name("John Doe")
            .email("johndoe@example.com")
            .password("password123")
            .build();

    Users users = usersMapper.toEntity(usersDto);

    assertThat(users).isNotNull();
    assertThat(users.getId()).isEqualTo(usersDto.getId());
    assertThat(users.getName()).isEqualTo(usersDto.getName());
    assertThat(users.getEmail()).isEqualTo(usersDto.getEmail());
  }
}
