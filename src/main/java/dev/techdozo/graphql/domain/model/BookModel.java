package dev.techdozo.graphql.domain.model;

public class BookModel {

  public record Book(
      Integer id, String name, String author, String publisher, Double price, Double revenue) {}

  public record BookInput(String name, String author, String publisher, Double price) {}

  public record BookInfo(Integer id, String name, String author, String publisher, Double price) {}
}
