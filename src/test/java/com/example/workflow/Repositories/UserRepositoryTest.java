package com.example.workflow.Repositories;

import com.example.workflow.Dto.UserDtoResp;
import com.example.workflow.Entities.Pole;
import com.example.workflow.Entities.User.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserRepositoryTest {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PoleRepository poleRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getusers(){
        List<User> myUsers = userRepository.findAll();

        myUsers.forEach(one-> System.out.println(objectMapper.convertValue(one, UserDtoResp.class)));
//        Optional<Pole> p = poleRepository.findById(6L);
//        for(User user:myUsers){
//            user.setPole(p.get());
//            userRepository.save(user);
//        }

    }
}