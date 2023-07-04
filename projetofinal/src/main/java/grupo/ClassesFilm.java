package grupo;

import java.util.Vector;

public class ClassesFilm {

    public class Review {
        // 0 para filme
        String filme_ou_serie;

        String review;
        String name_film;
        String id_filme_str;
        Integer id_filme_int;

        public Review(String filme_ou_serie, String review, String name_film, Integer id_filme_int) {
            this.review = review;
            this.name_film = name_film;
            this.id_filme_int = id_filme_int;
            this.id_filme_str = String.valueOf(id_filme_int);
        }
    }

    public class ListaFilm {
        String list_name;
        String file_path;

        // 0 para filme
        Vector<String> filme_ou_serie;

        Vector<Integer> ids_midia_int;
        Vector<String> ids_midia_str;

        public ListaFilm(String name) {
            list_name = name;
            file_path = "./lists_saved/list_ids_" + list_name;
        }

    }
}
