package BookMyShow;

import java.util.List;

import static BookMyShow.SearchType.MOVIE_NAME;

public class MovieFactory {

    public static List<Movie> searchMovie(SearchType searchType, String query, List<Movie> movies) {

        return switch (searchType) {
            case MOVIE_NAME -> movies.stream()
                    .filter(movie -> movie.getMovieName().equals(query))
                    .toList();
            case MOVIE_GENRE -> movies.stream()
                    .filter(movie -> movie.getMovieGenre().equals(query))
                    .toList();
            case MOVIE_LANGUAGE -> movies.stream()
                    .filter(movie -> movie.getMovieLanguage().equals(query))
                    .toList();
            case MOVIE_DURATION -> movies.stream()
                    .filter(movie -> movie.getDuration() == Integer.parseInt(query))
                    .toList();
            case MOVIE_CITY -> movies.stream()
                    .filter(movie -> movie.getAvailableInCities().contains(query))
                    .toList();
            default -> null;
        };

    }
}
