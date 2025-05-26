package demoqa.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AddBookBodyModel {

  private String userId;
  private List<CollectionOfIsbnsModel> collectionOfIsbns;
}
