package BookMyShow;

import java.util.List;

public class MovieController {

    List<Movie> movies;
    private MovieFactory movieFactory;

    public MovieController(List<Movie> movies){
        this.movies = movies;
    }

    public void addMovie(Movie movie){
        this.movies.add(movie);
    }

    public void removeMovie(int movieId){
        this.movies = this.movies.stream()
                     .filter(item -> item.movieId != movieId)
                     .toList();
    }

    public List<Movie> getMovies(){
        return this.movies;
    }

    public List<Movie> searchMovie(SearchType searchType, String query){

        return MovieFactory.searchMovie(searchType,query, this.movies);
    }
}
