package com.cgi.poc.dw.rest.dto;


import com.cgi.poc.dw.auth.data.Role;

public class AccessTokenDto {

  private String authToken;
  private Role role = Role.RESIDENT;

  public AccessTokenDto() {
  }

  public AccessTokenDto(String authToken, Role role) {
    this.authToken = authToken;
    this.role = role;
  }

  public String getAuthToken() {
    return authToken;
  }

  public void setAuthToken(String authToken) {
    this.authToken = authToken;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }
}
