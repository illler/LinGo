package com.example.backend.repositories;

import com.example.backend.model.User;
import com.example.backend.model.UserFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<UserFile, String> {

    @Query(value = """
      select f from UserFile f
      where f.user = :userId
      order by f.createDate desc
      limit 1
      """)
    UserFile findAllByUserOrderByCreateDateDesc(User userId);
}
