package dev.techdozo.graphql.domain.model;

public class RatingModel {
  public record Rating(Integer id, Integer bookId, Integer rating, String comment, String user) {}

  public record RatingInput(Integer rating, String comment, String user) {}
}
