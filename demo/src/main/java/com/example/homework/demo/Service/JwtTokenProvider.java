package com.example.homework.demo.Service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider {
	
	@Value("${spring.jwt.secret}")
	private String secretKey;
	
	private long tokenValidMilisecond = 1000L * 60 * 60; // 토근 유효는 1시간동안만
	
	
	//이름으로 토큰 생성
	public String generateToken(String name) {
		Date now = new Date();
		return Jwts.builder()
				.setId(name)
				//토큰 발행 일자
				.setIssuedAt(now)
				//유효시간 설정
				.setExpiration(new Date(now.getTime() + tokenValidMilisecond))
				//암호화 알고리즘, 시크릿키 설정
				.signWith(SignatureAlgorithm.HS256, secretKey)
				.compact();
	}
	
	//토큰을 복호화 해서 이름을 찾는다.
	public String getUserNameFormJwt(String jwt) {
		return getClaims(jwt).getBody().getId();
	}
	
	//유효성을 체크
	public boolean validateToken(String jwt) {
		return this.getClaims(jwt) != null;
	}
	
	private Jws<Claims> getClaims(String jwt){
		try {
			return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt);
			
		}catch(SignatureException e) {
			log.error("Invalid JWT signature");
			throw e;
		}catch(MalformedJwtException e) {
			log.error("Invalid JWT token");
			throw e;
		}catch(ExpiredJwtException e) {
			log.error("Expired JWT token");
			throw e;
		}catch(UnsupportedJwtException e) {
			log.error("Unsupported JWT token");
			throw e;
		}catch(IllegalArgumentException e) {
			log.error("JWT claims string is empty");
			throw e;
		}
	}
	
	
}
