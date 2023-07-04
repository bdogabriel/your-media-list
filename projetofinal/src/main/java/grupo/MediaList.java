package grupo;

import java.util.Collections;
import java.util.Vector;

import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.tv.TvSeries;

public class MediaList {

    private int length;
    private String name;
    private Vector<Object> medias;
    private Vector<Object> ids;

    MediaList(String n, MovieDb[] m, int[] mIds, TvSeries[] s, int[] sIds) {
        name = n;
        medias = new Vector<Object>();
        ids = new Vector<Object>();

        if (m != null)
            Collections.addAll(medias, m);
        if (s != null)
            Collections.addAll(medias, s);

        if (mIds != null)
            Collections.addAll(ids, mIds);
        if (sIds != null)
            Collections.addAll(ids, sIds);
    }

    /**
     * @breif Transformar uma lista de ids em string
     */
    public Vector<String> idListToString() {
        Vector<String> data = new Vector<String>();

        for (int i = 0; i < ids.size(); i++) {
            data.add(ids.get(i) + "\n");
        }

        return data;
    }

    public void addMovie(MovieDb movie, int id) {
        medias.add(movie);
        ids.add(id);
    }

    public void addSerie(TvSeries serie, int id) {
        medias.add(serie);
        ids.add(id);
    }

    public void removeMovie(MovieDb movie, int id) {
        medias.remove(movie);
        ids.remove(Integer.valueOf(id));
    }

    public void removeSerie(TvSeries serie, int id) {
        medias.remove(serie);
        ids.remove(Integer.valueOf(id));
    }

    public MovieDb[] getMovies() {
        Vector<MovieDb> movies = new Vector<MovieDb>();

        for (int i = 0; i < medias.size(); i++) {
            Object media = medias.get(i);

            if (media instanceof MovieDb) {
                movies.add((MovieDb) media);
            }
        }

        MovieDb[] m = (MovieDb[]) movies.toArray();

        return m;
    }

    public TvSeries[] getSeries() {
        Vector<TvSeries> series = new Vector<TvSeries>();

        for (int i = 0; i < medias.size(); i++) {
            Object media = medias.get(i);

            if (media instanceof TvSeries) {
                series.add((TvSeries) media);
            }
        }

        TvSeries[] s = (TvSeries[]) series.toArray();

        return s;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
