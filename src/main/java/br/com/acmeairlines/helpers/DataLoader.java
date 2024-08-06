package br.com.acmeairlines.helpers;

import br.com.acmeairlines.models.RoleModel;
import br.com.acmeairlines.models.StatusModel;
import br.com.acmeairlines.models.UserModel;
import br.com.acmeairlines.repositories.RoleRepository;
import br.com.acmeairlines.repositories.StatusRepository;
import br.com.acmeairlines.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private StatusRepository baggageStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            roleRepository.save(new RoleModel(1L,"ROLE_ADMIN"));
            roleRepository.save(new RoleModel(2L,"ROLE_USER"));
            roleRepository.save(new RoleModel(3L,"ROLE_MANAGER"));
            roleRepository.save(new RoleModel(4L,"ROLE_SUPPORT"));
        }

        if (baggageStatusRepository.count() == 0) {
            baggageStatusRepository.save(new StatusModel(1L,"DESPACHADA"));
            baggageStatusRepository.save(new StatusModel(2L,"EM_ANALISE_DE_SEGURANCA"));
            baggageStatusRepository.save(new StatusModel(3L,"REPROVADA_PELA_ANALISE_DE_SEGURANCA"));
            baggageStatusRepository.save(new StatusModel(4L,"APROVADA_PELA_ANALISE_DE_SEGURANCA"));
            baggageStatusRepository.save(new StatusModel(5L,"NA_AERONAVE"));
            baggageStatusRepository.save(new StatusModel(6L,"EM_VOO"));
            baggageStatusRepository.save(new StatusModel(7L,"DESTINO_INCERTO"));
            baggageStatusRepository.save(new StatusModel(8L,"EXTRAVIADA"));
            baggageStatusRepository.save(new StatusModel(9L,"AGUARDANDO_RECOLETA"));
            baggageStatusRepository.save(new StatusModel(10L,"COLETADA"));
        }

        if(userRepository.count() == 0){
            UserModel user = new UserModel();
            user.setEmail("admin@acme.com");
            user.setCpf("12345678910");
            user.setName("ADMIN");
            user.setPassword(passwordEncoder.encode("acme2024"));

            RoleModel defaultRole = roleRepository.findByRoleName("ROLE_ADMIN")
                    .orElseGet(() -> {
                        RoleModel newRole = new RoleModel();
                        newRole.setRoleName("ROLE_ADMIN");
                        return roleRepository.save(newRole);
                    });

            user.setRole(defaultRole);

            UserModel savedUser = userRepository.save(user);
        }
    }
}
