package demoqa.models;

import lombok.Data;
import java.util.List;

@Data
public class GetBookResponseModel {

  private String userId;
  private String username;
  private List<ResponseGetBookBooksModel> books;
}
