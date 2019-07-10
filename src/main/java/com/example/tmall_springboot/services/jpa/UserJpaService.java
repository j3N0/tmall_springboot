package com.example.tmall_springboot.services.jpa;


import com.example.tmall_springboot.domains.User;
import com.example.tmall_springboot.repositories.UserRepository;
import com.example.tmall_springboot.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class UserJpaService implements UserService {

    private final UserRepository userRepository;

    public UserJpaService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<User> pageFromJpa(int start, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size,sort);
        return userRepository.findAll(pageable);
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
}
