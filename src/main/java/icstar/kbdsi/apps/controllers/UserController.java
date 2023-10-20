package icstar.kbdsi.apps.controllers;

import icstar.kbdsi.apps.dto.*;
import icstar.kbdsi.apps.models.User;
import icstar.kbdsi.apps.repository.UserRepository;
import icstar.kbdsi.apps.services.UserService;
import icstar.kbdsi.apps.util.UserExcelGenerator;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin(origins = {"http://localhost:4200", "http://https://kbdsi-icstar-fe.vercel.app/", "https://kbdsi-icstar-g0cg82eie-lh007lucky-gmailcom.vercel.app/" })

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserRepository userRepository;
    UserService userService;

    public UserController(UserService userService) {
        super();
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers (@RequestParam(required = false) String name){
        try{
            List<User> users = new ArrayList<>();

            if(name == null) {
                users.addAll(userService.getAllUsers());
            }
            if(users.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(users, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user){
        try{
            System.out.println(user);
            if(user.getPassword() != null){
                String pw = new BCryptPasswordEncoder().encode(user.getPassword());
                user.setPassword(pw);
                User newUser = new User(user.getName(), user.getEmail(), user.getPhone(), user.getPassword() , user.getRoles());
                userRepository.save(newUser);
                return new ResponseEntity<>(newUser, HttpStatus.CREATED);
            }
            else{
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

        }catch (Exception e){
            System.out.println(e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUserById(@PathVariable(required = true) Long id, @RequestBody User newUser) {
       return userService.updateUserById(newUser, id);
    }
    @PutMapping("/users/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable(required = true) Long id, @RequestBody DeleteDto deleteDto) {
        try {
            Optional<User> optUser = userRepository.findById(id);
            if(optUser.isPresent()) {
                User oldUser = optUser.get();
                oldUser.setDeleted(true);
                oldUser.setUpdatedBy(deleteDto.getUpdatedBy());
                userRepository.save(oldUser);
            return new ResponseEntity<>("User has been deleted",HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
            }
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/users/export-to-excel")
    public void exportIntoExcelFile(HttpServletResponse response) throws IOException{
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue= "attachment; filename=user_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey,headerValue);

//        List<User> listOfUsers = userRepository.findAll();
        List<User> listOfUsers = userService.getAllUsers();
        UserExcelGenerator generator = new UserExcelGenerator(listOfUsers);
        generator.generateExcelFile(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDetailsDto> loginUser(@RequestBody LoginDto user){
        User loginUser = userService.getUserByEmail(user.getEmail());
        UserDetailsDto res =  new UserDetailsDto();
        if(loginUser != null){
            boolean isMatch = new BCryptPasswordEncoder().matches(user.getPassword(), loginUser.getPassword());
            if(isMatch){
                res.setId(loginUser.getId());
                res.setName(loginUser.getName());
                res.setEmail(loginUser.getEmail());
                res.setPhone(loginUser.getPhone());
                res.setRoles(loginUser.getRoles());
                return new ResponseEntity<>(res, HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST
                );
            }
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }



    @GetMapping("/users/name")
    private Page<User> getUsersWithFilterName(PaginationUserDto paginationUserDto) {
        Page<User> users = userService.findUsersContainingName(paginationUserDto.getName(), false,  paginationUserDto.getPageNum(), paginationUserDto.getPageSize());

        return users;

    }



}
