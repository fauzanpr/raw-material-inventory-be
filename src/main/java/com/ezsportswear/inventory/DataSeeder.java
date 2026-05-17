// package com.ezsportswear.inventory;

// import com.ezsportswear.inventory.entity.Category;
// import com.ezsportswear.inventory.entity.User;
// import com.ezsportswear.inventory.repository.CategoryRepository;
// import com.ezsportswear.inventory.repository.UserRepository;
// import lombok.RequiredArgsConstructor;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.stereotype.Component;

// @Component
// @RequiredArgsConstructor
// public class DataSeeder implements CommandLineRunner {

//     private final CategoryRepository categoryRepository;
//     private final UserRepository userRepository;

//     @Override
//     public void run(String... args) {
//         if (categoryRepository.count() == 0) {
//             categoryRepository.save(Category.builder().name("Fabric").build());
//             categoryRepository.save(Category.builder().name("Accessories").build());
//             categoryRepository.save(Category.builder().name("Printing Material").build());
//         }

//         if (userRepository.count() == 0) {
//             userRepository.save(User.builder()
//                     .name("Admin Gudang")
//                     .email("admin@ezsportswear.com")
//                     .password("password")
//                     .build());
//         }
//     }
// }

package com.ezsportswear.inventory;

import com.ezsportswear.inventory.entity.Category;
import com.ezsportswear.inventory.entity.User;
import com.ezsportswear.inventory.repository.CategoryRepository;
import com.ezsportswear.inventory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (categoryRepository.count() == 0) {
            categoryRepository.save(Category.builder().name("Fabric").build());
            categoryRepository.save(Category.builder().name("Accessories").build());
            categoryRepository.save(Category.builder().name("Printing Material").build());
        }

        if (userRepository.count() == 0) {
            userRepository.save(User.builder()
                    .name("Admin Gudang")
                    .email("admin@ezsportswear.com")
                    .password(passwordEncoder.encode("password"))
                    .build());
        }
    }
}