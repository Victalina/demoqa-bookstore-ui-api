package demoqa.models;

import lombok.Data;

@Data
public class LoginResponseModel {

  private String userId;
  private String username;
  private String password;
  private String token;
  private String expires;
  private String created_date;
  private Boolean isActive;
}
