package com.example.tmall_springboot.services.jpa;


import com.example.tmall_springboot.domains.User;
import com.example.tmall_springboot.repositories.UserRepository;
import com.example.tmall_springboot.services.UserService;
import com.example.tmall_springboot.utils.Page4Navigator;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static com.example.tmall_springboot.config.Const.NAVIGATE_PAGES;

@Service
public class UserJpaService implements UserService {

    private final UserRepository userRepository;

    public UserJpaService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page4Navigator<User> pageFromJpa(int start, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size,sort);
        return new Page4Navigator<>(userRepository.findAll(pageable), NAVIGATE_PAGES);
    }

    @Override
    public boolean isExist(String name) {
        return userRepository.findByName(name) != null;
    }

    @Override
    public User getByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public User add(User user) {
        return userRepository.save(user);
    }

    @Override
    public User get(String name, String password) {
        return userRepository.getByNameAndPassword(name, password);
    }
}
