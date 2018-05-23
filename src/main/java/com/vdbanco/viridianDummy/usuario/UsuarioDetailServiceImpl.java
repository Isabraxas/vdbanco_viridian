package com.vdbanco.viridianDummy.usuario;

import com.vdbanco.viridianDummy.domain.UserModel;
import com.vdbanco.viridianDummy.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

@Service
public class UsuarioDetailServiceImpl implements UserDetailsService {

	private UserRepository usuarioRepository;

	public UsuarioDetailServiceImpl(UserRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserModel usuario = usuarioRepository.findByUserName(username);
		if (usuario == null) {
			throw new UsernameNotFoundException(username);
		}
		//return new User(usuario.getUserName(), usuario.getUserPassword(), emptyList());
		//TODO el if deberia ser algo asi : if(usuario.getRol == talCosa) Then asignar estos roles
		else if(usuario.getUserName().equals("adm")) {
			return userBuilder(usuario.getUserName(), usuario.getUserPassword(), "ADMIN", "USER");
		}
		else {
			return userBuilder(usuario.getUserName(), usuario.getUserPassword(), "USER");
		}

	}

	private User userBuilder(String username, String password, String... roles) {
		boolean enabled = true;
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
		}
		return new User(username, password, enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, authorities);
	}

}
