package com.example.tmall_springboot.services.jpa;


import com.example.tmall_springboot.domains.User;
import com.example.tmall_springboot.repositories.UserRepository;
import com.example.tmall_springboot.services.UserService;
import com.example.tmall_springboot.utils.Page4Navigator;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static com.example.tmall_springboot.config.Const.NAVIGATE_PAGES;

@Service
@CacheConfig(cacheNames = "users")
public class UserJpaService implements UserService {

    private final UserRepository userRepository;

    public UserJpaService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Cacheable(key = "'users-page-' + #p0+ '-' + #p1")
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
    @Cacheable(key = "'users-one-name-' + #p0")
    public User getByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    @CacheEvict(allEntries = true)
    public User add(User user) {
        return userRepository.save(user);
    }

    @Override
    @Cacheable(key = "'users-one-name-' + #p0 +'-password-' + #p1")
    public User get(String name, String password) {
        return userRepository.getByNameAndPassword(name, password);
    }
}
