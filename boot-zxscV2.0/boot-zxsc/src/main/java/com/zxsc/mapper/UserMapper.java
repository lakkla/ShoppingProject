package com.zxsc.mapper;

import com.zxsc.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface UserMapper {
    User findByUsername(String username);

    User findById(Long id);

    List<User> findAll();

    int insert(User user);

    int update(User user);

    int updateProfile(User user);

    int deleteById(Long id);

    User login(@Param("username") String username, @Param("password") String password);

    int updatePassword(@Param("id") Long id, @Param("password") String password);
}
