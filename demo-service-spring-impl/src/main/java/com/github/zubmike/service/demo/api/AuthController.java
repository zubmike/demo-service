package com.github.zubmike.service.demo.api;

import com.github.zubmike.service.demo.api.types.AuthEntry;
import com.github.zubmike.service.demo.api.types.AuthUserInfo;
import com.github.zubmike.service.demo.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

	private final AuthService authService;

	@Autowired
	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping
	public AuthUserInfo authenticate(@RequestBody AuthEntry authEntry) {
		return authService.authenticate(Locale.getDefault(), authEntry);
	}

}
