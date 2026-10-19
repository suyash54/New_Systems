package BookMyShow;

import java.util.List;

public class Movie {

    int movieId;
    String movieName;
    String movieGenre;
    String movieLanguage;
    int duration;
    List<String> availableInCities;


    public Movie(int movieId, String movieName, String movieGenre, String movieLanguage, int duration, List<String> availableInCities){
        this.movieId = movieId;
        this.movieName = movieName;
        this.movieGenre = movieGenre;
        this.movieLanguage = movieLanguage;
        this.duration = duration;
        this.availableInCities = availableInCities;
    }

    public String getMovieName(){
        return this.movieName;
    }
    public String getMovieGenre(){
        return this.movieGenre;
    }

    public String getMovieLanguage(){
        return this.movieLanguage;
    }

    public int getDuration(){
        return this.duration;
    }

    public List<String> getAvailableInCities(){
        return this.availableInCities;
    }

}
