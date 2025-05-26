package demoqa.models;

import lombok.Data;
import java.util.List;

@Data
public class AddBookResponseModel {

  private List<ResponseAddBookBooksModel> books;
}
