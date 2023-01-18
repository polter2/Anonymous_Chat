package com.example.AnonChat.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserReposetory extends CrudRepository<Users, Long> {
}
