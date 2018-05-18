package com.vdbanco.viridianDummy.usuario;

import com.vdbanco.viridianDummy.domain.UserModel;
import com.vdbanco.viridianDummy.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
		return new User(usuario.getUserName(), usuario.getUserPassword(), emptyList());
	}
}
