package dev.techdozo.graphql.controller;

import dev.techdozo.graphql.application.service.BookService;
import dev.techdozo.graphql.application.service.CurrencyConversionService;
import dev.techdozo.graphql.application.service.RatingService;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static dev.techdozo.graphql.domain.model.BookModel.*;
import static dev.techdozo.graphql.domain.model.RatingModel.Rating;
import static dev.techdozo.graphql.domain.model.RatingModel.RatingInput;

@Slf4j
@RequiredArgsConstructor
@Controller
public class BooksCatalogController {

  public static final String DEFAULT_CCY = "$";
  private final BookService bookService;
  private final RatingService ratingService;

  private final CurrencyConversionService currencyConversionService;

  @QueryMapping()
  public Collection<Book> books() {
    log.info("Fetching all books..");
    return bookService.getBooks();
  }

  @QueryMapping
  public Book bookById(@Argument Integer id) {
    log.info("Getting book by id, {}", id);
    return bookService.bookById(id);
  }

  @SchemaMapping(typeName = "Book")
  public Double price(Book book, DataFetchingEnvironment dataFetchingEnvironment) {
    double price = book.price();
    var maybeAppliedDirective =
        dataFetchingEnvironment.getQueryDirectives().getImmediateAppliedDirective("currency");

    if (!maybeAppliedDirective.isEmpty()) {
      String currency = maybeAppliedDirective.get(0).getArgument("currency").getValue();
      log.info("Getting price in currency {}", currency);
      var rate = currencyConversionService.convert(DEFAULT_CCY, currency);
      price = price * rate;
    }
    return price;
  }

  @MutationMapping
  public BookInfo addBook(@Argument BookInput book) {
    log.info("Saving book, name {}", book.name());
    return bookService.saveBook(book);
  }

  @MutationMapping
  public BookInfo updateBook(@Argument Integer id, @Argument BookInput book) {
    log.info("Updating book, id {}", id);
    return bookService.updateBook(id, book);
  }

  @MutationMapping
  public BookInfo deleteBook(@Argument Integer id) {
    log.info("Updating book, id {}", id);
    return bookService.deleteBook(id);
  }

  @MutationMapping
  public Rating addRating(@Argument Integer bookId, @Argument RatingInput rating) {
    log.info("Saving rating for book, id {}", bookId);
    return ratingService.addRating(bookId, rating);
  }

  @BatchMapping
  public Map<Book, List<Rating>> ratings(List<Book> books) {
    log.info("Fetching ratings for books {} ", books);
    return ratingService.ratingsForBooks(books);
  }
}
