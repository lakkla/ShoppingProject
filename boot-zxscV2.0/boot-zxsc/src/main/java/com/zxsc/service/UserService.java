package com.zxsc.service;

import com.zxsc.entity.User;
import com.zxsc.mapper.UserMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User login(String username, String password) {
        String md5Password = DigestUtils.md5Hex(password);
        return userMapper.login(username, md5Password);
    }

    public boolean register(User user) {
        User existUser = userMapper.findByUsername(user.getUsername());
        if (existUser != null) {
            return false;
        }
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));
        user.setRole(0);
        user.setStatus(1);
        return userMapper.insert(user) > 0;
    }

    public User findById(Long id) {
        return userMapper.findById(id);
    }

    public List<User> findAll() {
        return userMapper.findAll();
    }

    public boolean update(User user) {
        return userMapper.update(user) > 0;
    }

    public boolean updateProfile(User user) {
        return userMapper.updateProfile(user) > 0;
    }

    public boolean updatePassword(Long id, String oldPassword, String newPassword) {
        User user = userMapper.findById(id);
        if (user == null) {
            return false;
        }
        if (oldPassword != null && !oldPassword.isEmpty()) {
            String md5OldPassword = DigestUtils.md5Hex(oldPassword);
            if (!user.getPassword().equals(md5OldPassword)) {
                return false;
            }
        }
        String md5NewPassword = DigestUtils.md5Hex(newPassword);
        return userMapper.updatePassword(id, md5NewPassword) > 0;
    }

    public boolean deleteById(Long id) {
        return userMapper.deleteById(id) > 0;
    }

    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    public boolean updatePasswordByPhone(Long id, String newPassword) {
        String md5NewPassword = DigestUtils.md5Hex(newPassword);
        return userMapper.updatePassword(id, md5NewPassword) > 0;
    }
}
