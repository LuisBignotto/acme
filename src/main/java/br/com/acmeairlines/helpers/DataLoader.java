package br.com.acmeairlines.helpers;

import br.com.acmeairlines.models.*;
import br.com.acmeairlines.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private StatusRepository baggageStatusRepository;

    @Autowired
    private BaggageRepository baggageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            roleRepository.save(new RoleModel(1L,"ROLE_ADMIN"));
            roleRepository.save(new RoleModel(2L,"ROLE_USER"));
            roleRepository.save(new RoleModel(3L,"ROLE_BAGGAGE_MANAGER"));
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

        if (flightRepository.count() == 0) {
            flightRepository.save(new FlightModel(1L, "GRU123VCP", LocalDateTime.of(2024, 9, 21, 8, 0),
                    LocalDateTime.of(2024, 9, 21, 9, 0), "Aeroporto Internacional de São Paulo - Guarulhos",
                    "Aeroporto Internacional de Campinas - Viracopos", "ON_TIME", "Boeing 737"));

            flightRepository.save(new FlightModel(2L, "PMW456AUX", LocalDateTime.of(2024, 9, 22, 10, 30),
                    LocalDateTime.of(2024, 9, 22, 11, 30), "Aeroporto de Palmas",
                    "Aeroporto de Araguaína", "ON_TIME", "Airbus A320"));

            flightRepository.save(new FlightModel(3L, "SBAM789SNCC", LocalDateTime.of(2024, 9, 23, 12, 45),
                    LocalDateTime.of(2024, 9, 23, 13, 45), "Aeroporto de Amapá",
                    "Aeroporto de Calçoene", "ON_TIME", "Embraer E195"));

            flightRepository.save(new FlightModel(4L, "SSA321IOS", LocalDateTime.of(2024, 9, 24, 14, 0),
                    LocalDateTime.of(2024, 9, 24, 15, 30), "Aeroporto Internacional de Salvador - Deputado Luís Eduardo Magalhães",
                    "Aeroporto de Ilhéus - Jorge Amado", "ON_TIME", "Boeing 787"));

            flightRepository.save(new FlightModel(5L, "BPS654GIG", LocalDateTime.of(2024, 9, 25, 16, 15),
                    LocalDateTime.of(2024, 9, 25, 17, 45), "Aeroporto de Porto Seguro",
                    "Aeroporto Internacional do Rio de Janeiro - Galeão", "ON_TIME", "Airbus A330"));

            flightRepository.save(new FlightModel(6L, "CFB987POA", LocalDateTime.of(2024, 9, 26, 18, 0),
                    LocalDateTime.of(2024, 9, 26, 19, 30), "Aeroporto Internacional de Cabo Frio",
                    "Aeroporto Internacional de Porto Alegre - Salgado Filho", "ON_TIME", "Boeing 777"));

            flightRepository.save(new FlightModel(7L, "BGX741PET", LocalDateTime.of(2024, 9, 27, 20, 0),
                    LocalDateTime.of(2024, 9, 27, 21, 30), "Aeroporto Internacional Comandante Gustavo Kraemer",
                    "Aeroporto Internacional de Pelotas", "ON_TIME", "Embraer E190"));

            flightRepository.save(new FlightModel(8L, "URG852CNF", LocalDateTime.of(2024, 9, 28, 9, 0),
                    LocalDateTime.of(2024, 9, 28, 10, 30), "Aeroporto Internacional Rubem Berta",
                    "Aeroporto Internacional de Belo Horizonte-Confins", "ON_TIME", "Boeing 737"));

            flightRepository.save(new FlightModel(9L, "IZA963DIQ", LocalDateTime.of(2024, 9, 29, 11, 15),
                    LocalDateTime.of(2024, 9, 29, 12, 45), "Aeroporto Presidente Itamar Franco",
                    "Aeroporto de Divinópolis", "ON_TIME", "Airbus A320"));

            flightRepository.save(new FlightModel(10L, "IPN147SNCC", LocalDateTime.of(2024, 9, 30, 13, 30),
                    LocalDateTime.of(2024, 9, 30, 15, 0), "Aeroporto de Ipatinga",
                    "Aeroporto de Calçoene", "ON_TIME", "Embraer E195"));
        }

        if(userRepository.count() == 0){
            UserModel admin = new UserModel();
            admin.setEmail("admin@acme.com");
            admin.setCpf("0000000000");
            admin.setName("ADMIN");
            admin.setPassword(passwordEncoder.encode("123"));

            RoleModel adminRole = roleRepository.findByRoleName("ROLE_ADMIN")
                    .orElseGet(() -> {
                        RoleModel newRole = new RoleModel();
                        newRole.setRoleName("ROLE_ADMIN");
                        return roleRepository.save(newRole);
                    });

            admin.setRole(adminRole);
            userRepository.save(admin);

            UserModel luisFelipe = new UserModel();
            luisFelipe.setEmail("luis@acme.com");
            luisFelipe.setCpf("98765432100");
            luisFelipe.setName("Luis Felipe");
            luisFelipe.setPassword(passwordEncoder.encode("123"));

            RoleModel supportRole = roleRepository.findByRoleName("ROLE_SUPPORT")
                    .orElseGet(() -> {
                        RoleModel newRole = new RoleModel();
                        newRole.setRoleName("ROLE_SUPPORT");
                        return roleRepository.save(newRole);
                    });

            luisFelipe.setRole(supportRole);
            userRepository.save(luisFelipe);

            UserModel lucas = new UserModel();
            lucas.setEmail("lucas@acme.com");
            lucas.setCpf("98765432101");
            lucas.setName("Lucas");
            lucas.setPassword(passwordEncoder.encode("123"));

            RoleModel baggageManagerRole = roleRepository.findByRoleName("ROLE_BAGGAGE_MANAGER")
                    .orElseGet(() -> {
                        RoleModel newRole = new RoleModel();
                        newRole.setRoleName("ROLE_BAGGAGE_MANAGER");
                        return roleRepository.save(newRole);
                    });

            lucas.setRole(baggageManagerRole);
            userRepository.save(lucas);

            UserModel diego = new UserModel();
            diego.setEmail("diego@gmail.com");
            diego.setCpf("98765432102");
            diego.setName("Diego");
            diego.setPassword(passwordEncoder.encode("123"));

            RoleModel userRole = roleRepository.findByRoleName("ROLE_USER")
                    .orElseGet(() -> {
                        RoleModel newRole = new RoleModel();
                        newRole.setRoleName("ROLE_USER");
                        return roleRepository.save(newRole);
                    });

            diego.setRole(userRole);
            userRepository.save(diego);

            UserModel enzo = new UserModel();
            enzo.setEmail("enzo@gmail.com");
            enzo.setCpf("98765432103");
            enzo.setName("Enzo");
            enzo.setPassword(passwordEncoder.encode("123"));

            enzo.setRole(userRole);
            userRepository.save(enzo);

            UserModel matheus = new UserModel();
            matheus.setEmail("matheus@acme.com");
            matheus.setCpf("98765432104");
            matheus.setName("Matheus");
            matheus.setPassword(passwordEncoder.encode("123"));

            matheus.setRole(supportRole);
            userRepository.save(matheus);

            UserModel gabriela = new UserModel();
            gabriela.setEmail("gabriela@acme.com");
            gabriela.setCpf("98765432105");
            gabriela.setName("Gabriela");
            gabriela.setPassword(passwordEncoder.encode("123"));

            gabriela.setRole(baggageManagerRole);
            userRepository.save(gabriela);

            String[] nomes = {"João Silva", "Maria Oliveira", "Pedro Souza", "Ana Lima", "Carlos Pereira",
                    "Fernanda Costa", "Rafael Alves", "Juliana Santos", "Lucas Batista", "Paula Moreira"};

            String[] cores = {"Azul", "Verde", "Preto", "Vermelho", "Amarelo", "Branco", "Cinza", "Rosa", "Laranja", "Roxo"};

            for (int i = 0; i < nomes.length; i++) {
                UserModel user = new UserModel();
                user.setEmail(nomes[i].toLowerCase().replace(" ", ".") + "@gmail.com");
                user.setCpf("123456789" + (i + 1));
                user.setName(nomes[i]);
                user.setPassword(passwordEncoder.encode("123"));
                user.setRole(userRole);
                userRepository.save(user);

                BaggageModel baggage = new BaggageModel();
                baggage.setUserId(user.getId());
                baggage.setTag(generateTag());
                baggage.setColor(cores[i]);
                baggage.setWeight(new BigDecimal("1.0").add(new BigDecimal(i)));
                baggage.setLastLocation("Localização " + (i + 1));

                StatusModel status = baggageStatusRepository.findById(1L)
                        .orElseThrow(() -> new RuntimeException("Status not found"));
                baggage.setStatus(status);

                baggage.setFlightId(1L);
                baggageRepository.save(baggage);
            }
        }

        if (ticketRepository.count() == 0) {
            for (int i = 1; i <= 10; i++) {
                TicketModel ticket = new TicketModel();
                ticket.setUserId((long) (i + 7));
                ticket.setTitle("Minha mala não aparece no APP");
                ticket.setDescription("Minha mala não aparece no APP");
                ticket.setStatus("ABERTO");
                ticket.setCreatedAt(LocalDateTime.now());
                ticket.setUpdatedAt(LocalDateTime.now());
                ticketRepository.save(ticket);

                if (i == 1) {
                    MessageModel message1 = new MessageModel();
                    message1.setTicketId(ticket.getId());
                    message1.setSenderId(ticket.getUserId());
                    message1.setMessage("Estou com dificuldade para localizar minha mala no aplicativo.");
                    message1.setTimestamp(LocalDateTime.now());
                    messageRepository.save(message1);

                    MessageModel message2 = new MessageModel();
                    message2.setTicketId(ticket.getId());
                    message2.setSenderId(1L);
                    message2.setMessage("Vamos verificar o status de sua mala, por favor aguarde.");
                    message2.setTimestamp(LocalDateTime.now());
                    messageRepository.save(message2);
                }
            }
        }

    }

    private String generateTag() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder tag = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(characters.length());
            tag.append(characters.charAt(index));
        }

        return tag.toString();
    }
}
