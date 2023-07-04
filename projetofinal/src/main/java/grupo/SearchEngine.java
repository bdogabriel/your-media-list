package grupo;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.TmdbTV;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.tv.TvSeries;
import info.movito.themoviedbapi.TvResultsPage;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;

public class SearchEngine {
    // variáveis privadas
    private TmdbSearch engine;
    private TmdbMovies m;
    private TmdbTV t;

    /**
     * Initializes a Search Engine (for movies and series) using the TMDb API
     * database
     * 
     * @param api class that will communicate with the API
     */
    public SearchEngine(TmdbApi api) {
        engine = api.getSearch();
        m = api.getMovies();
        t = api.getTvSeries();
    }

    public MovieDb FindMovieByID(int id) {
        return m.getMovie(id, "pt-BR");
    }

    public TvSeries FindSeriesByID(int id) {
        return t.getSeries(id, "pt-BR");
    }

    /**
     * Searches for movies by name using the TMDb API
     * 
     * @param name name that will be searched
     * @return array of movies that were found in the search
     */
    public MovieDb[] searchMovies(String name) {
        // searching movies by name
        MovieResultsPage pages;
        try {
            pages = engine.searchMovie(name, 0, "pt-BR", false, 0);
        } catch (Exception e) {
            return null;
        }

        // saving all movies found in a array
        List<MovieDb> movies = pages.getResults();
        MovieDb[] moviesFound = new MovieDb[movies.size()];
        movies.toArray(moviesFound);

        return moviesFound;
    }

    /**
     * Gets covers for series in a TvSeries array
     * 
     * @param series array
     * @param size   of the cover. Not all sizes are accepted
     * @return BufferedImage array with the covers
     */
    public BufferedImage[] getCovers(TvSeries series[], String size) {
        BufferedImage covers[] = new BufferedImage[series.length];

        for (int i = 0; i < series.length; i++) {
            String posterPath = series[i].getPosterPath();
            String baseURL = "https://image.tmdb.org/t/p/";
            String s = "w" + size;
            String fullImageURL = baseURL + s + posterPath;

            System.out.println(fullImageURL);

            try {
                URL url = new URL(fullImageURL);
                covers[i] = ImageIO.read(url);
            }

            catch (IOException ex) {
                System.out.println("Invalid URL");
            }
        }

        return covers;
    }

    /**
     * Gets covers for movies in a MovieDb array
     * 
     * @param movies array
     * @param size   of the cover. Not all sizes are accepted
     * @return BufferedImage array with the covers
     */
    public BufferedImage[] getCovers(MovieDb movies[], String size) {
        BufferedImage covers[] = new BufferedImage[movies.length];

        for (int i = 0; i < movies.length; i++) {
            String posterPath = movies[i].getPosterPath();
            String baseURL = "https://image.tmdb.org/t/p/";
            String s = "w" + size;
            String fullImageURL = baseURL + s + posterPath;

            System.out.println(fullImageURL);

            try {
                URL url = new URL(fullImageURL);
                covers[i] = ImageIO.read(url);
            }

            catch (IOException ex) {
                System.out.println("Invalid URL");
            }
        }

        return covers;
    }

    /**
     * Searches for series by name using the TMDb API
     * 
     * @param name name that will be searched
     * @return array of series that were found in the search
     */
    public TvSeries[] searchSeries(String name) {
        // searching series by name
        TvResultsPage pages;

        try {
            pages = engine.searchTv(name, "pt-BR", 0);
        } catch (Exception e) {
            return null;
        }

        // saving all series found in a array
        List<TvSeries> series = pages.getResults();
        TvSeries[] seriesFound = new TvSeries[series.size()];
        series.toArray(seriesFound);

        return seriesFound;
    }

    public static void main(String[] args) {
        // creating a TmdbApi class using a apiKey
        TmdbApi api = new TmdbApi("813da1ea51be04e7c3cecf6e417f67dc");
        SearchEngine SE = new SearchEngine(api);

        // Searching for movies example
        MovieDb[] movies = SE.searchMovies("O Hobbit");
        System.out.println("Filmes achados:");
        for (MovieDb movie : movies) {
            System.out.println(movie.getTitle());
        }
        System.out.println();

        // Searching for series example
        TvSeries[] series = SE.searchSeries("Moon Knight");
        System.out.println("Séries achadas:");
        for (TvSeries serie : series) {
            System.out.println(serie.getName());
        }
    }
}
