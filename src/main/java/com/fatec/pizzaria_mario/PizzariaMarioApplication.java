package com.fatec.pizzaria_mario;

import com.fatec.pizzaria_mario.domain.Usuario;
import com.fatec.pizzaria_mario.repository.UsuarioRepository;
import com.fatec.pizzaria_mario.service.UsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class PizzariaMarioApplication {

    public static void main(String[] args) {
        SpringApplication.run(PizzariaMarioApplication.class, args);
    }

    // ESTA É A VERSÃO CORRIGIDA QUE ATUALIZA A SENHA, NÃO IMPORTA O QUE ACONTEÇA
    @Bean
    CommandLineRunner run(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Garante que o usuário ADMIN exista com a senha correta.
            String adminLogin = "admin";
            Usuario admin = (Usuario) usuarioRepository.findByLogin(adminLogin);
            if (admin == null) {
                admin = new Usuario();
                admin.setLogin(adminLogin);
                admin.addRole("ADMIN");
                System.out.println(">>> Usuário ADMIN não encontrado, criando um novo. <<<");
            }
            // ESTA É A LINHA MAIS IMPORTANTE:
            // Ela executa TODA VEZ, garantindo que a senha está sempre criptografada corretamente.
            admin.setSenha(passwordEncoder.encode("admin123"));
            usuarioRepository.save(admin);
            System.out.println(">>> Senha do usuário ADMIN verificada e salva com sucesso! <<<");
        };
    }
}