package com.example.backend.repositories;

import com.example.backend.model.User;
import com.example.backend.model.UserImageFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFileRepository extends JpaRepository<UserImageFile, String> {

    @Query(value = """
      select f from UserImageFile f
      where f.user = :userId
      order by f.createDate desc
      limit 1
      """)
    UserImageFile findAllByUserOrderByCreateDateDesc(User userId);
}
