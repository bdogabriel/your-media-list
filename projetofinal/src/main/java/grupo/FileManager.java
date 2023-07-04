
package grupo;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.BufferedReader;

import java.util.Vector;

import grupo.ClassesFilm.ListaFilm;
import grupo.ClassesFilm.Review;

public class FileManager {

    /**
     * @breif Salvar uma string de ids no HD
     * @param ids_str string de ids com um id em cada linha
     */
    public void saveIdsList(ListaFilm list) {
        // Instancia e cria um arquivo se nescessário
        File list_ids = new File(list.file_path);

        try {
            FileWriter fw;
            BufferedWriter bw;

            if (!list_ids.exists()) {
                new File("./lists_saved").mkdir();
                list_ids.createNewFile();
                // Instanciar um objeto para escrita
                fw = new FileWriter(list_ids, true);
                bw = new BufferedWriter(fw);

                // Escrever nome no arquivo
                bw.write(list.list_name);
            } else {
                // Instanciar um objeto para escrita
                fw = new FileWriter(list_ids, true);
                bw = new BufferedWriter(fw);
            }

            // escrever dados
            for (Integer i = 0; i < list.ids_midia_str.size(); i++) {

                bw.write(list.filme_ou_serie.get(i));
                bw.write(list.ids_midia_str.get(i));
                bw.write("-1\n");

            }
            // Fechar os recursos
            bw.close();
            fw.close();

        } catch (Exception e) {
            // System.out.println("Aquivo");
            return;
        }

    }

    /**
     * @breif Ler e jogar na Ram uma lista de ids de filmes
     * @param ids_file arquivo de onde será feita a leitura
     * @return vetor de inteiros contendo a listade ids
     */
    public ClassesFilm.ListaFilm getSaveIdsList(ListaFilm l) {
        File ids_file = new File(l.file_path);
        ClassesFilm cf = new ClassesFilm();

        try {
            ClassesFilm.ListaFilm list = cf.new ListaFilm(l.list_name);
            list.filme_ou_serie = new Vector<String>();
            list.ids_midia_int = new Vector<Integer>();
            list.ids_midia_str = new Vector<String>();

            // Verificar se o arquivo existe
            if (!ids_file.exists()) {
                return null;
            }

            // Instanciar buffer de leitura
            FileReader fr = new FileReader(ids_file);
            BufferedReader br = new BufferedReader(fr);

            // Loop para a execução da leitura no arquivo
            list.list_name = br.readLine();
            int i = 0;
            while (br.ready()) {
                i++;
                // Leitura das informações
                String filme_ou_serie = br.readLine();
                String ids_midia_str = br.readLine();

                // Pular caractere terminador
                br.readLine();

                // Adicionar informações
                list.filme_ou_serie.add(filme_ou_serie);
                list.ids_midia_str.add(ids_midia_str);
                list.ids_midia_int.add(Integer.parseInt(ids_midia_str));

            }
            if (i == 0) {
                fr.close();
                br.close();
                return null;
            }

            // Fechar os recursos
            fr.close();
            br.close();

            return list;

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

    }

    /**
     * @breif Salvar uma ou mais reviews no HD
     * @param ids_films    lista de inteiros contendo os ids de cada filme a ser
     *                     salvo
     * @param list_reviews lista de strings contendo
     */
    public void saveReviews(Vector<ClassesFilm.Review> reviews) {

        File reviews_file = new File("./reviews_saved/reviews_file");

        try {

            if (!reviews_file.exists()) {
                new File("./reviews_saved").mkdir();
                reviews_file.createNewFile();
            }

            // Instanciar um objeto para escrita e defini-lo
            FileWriter fw = new FileWriter(reviews_file, true);
            // Instanciar o buffer
            BufferedWriter bw = new BufferedWriter(fw);

            for (int i = 0; i < reviews.size(); i++) {

                // Escrever id e review
                Review r = reviews.get(i);
                bw.write(r.filme_ou_serie);
                bw.write(r.id_filme_str + "\n");
                bw.write(r.name_film + "\n");
                bw.write(r.review);
                bw.write("\n");
                bw.write("-1\n");

            }

            // Fechar os recursos
            bw.close();
            fw.close();

        } catch (Exception e) {
            // System.out.println("Aquivo");
            return;
        }

    }

    /**
     * @breif Função para pesquisar e recuperar uma review de um arquivo
     * @param reviews_file   arquivo de onde será feita a leitura
     * @param id_film_to_get id do filme da review
     * @return a review
     */
    public Review searchReview(File reviews_file, String id_film_to_get) {
        ClassesFilm cf = new ClassesFilm();

        try {
            // Instanciar um objeto para escrita e defini-lo
            FileReader fr = new FileReader(reviews_file);
            // Instanciar o buffer
            BufferedReader br = new BufferedReader(fr);

            // Loop para a execução da leitura no arquivo
            String id = "";
            String film_serie = br.readLine();
            while ((id = br.readLine()) != null) {
                // ler id
                if (id.equals(id_film_to_get)) {
                    // Ler revirew
                    String name_film = br.readLine();
                    String review_disc = "";
                    String read_out;
                    while (!(read_out = br.readLine()).equals("-1")) {
                        review_disc += read_out;
                    }

                    ClassesFilm.Review review_find = cf.new Review(film_serie, review_disc, name_film,
                            Integer.parseInt(id));
                    br.close();
                    fr.close();
                    return review_find;

                }
                // Iterar até a próxima review
                else
                    while (br.readLine() != "-1\n")
                        ;

            }

            br.close();
        } catch (Exception e) {
            // TODO: handle exception
        }

        System.out.println("Review não encontrada\n");
        return (cf.new Review("-1", "-1", "-1", -1));
    }

    /**
     * @breif Função para pegar todas as reviews de um arquivo
     * @return
     */
    public Vector<Review> getReviews() {
        Vector<Review> reviews = new Vector<Review>();
        ClassesFilm cf = new ClassesFilm();
        File reviews_file = new File("./reviews_saved/reviews_file");

        try {
            // Instanciar um objeto para escrita e defini-lo
            FileReader fr = new FileReader(reviews_file);
            // Instanciar o buffer
            BufferedReader br = new BufferedReader(fr);

            String id = "";
            while ((id = br.readLine()) != null) {
                // Ler revirew
                String filme_serie = br.readLine();
                String name_film = br.readLine();
                String review_disc = "";
                String read_out;

                while (!(read_out = br.readLine()).equals("-1")) {
                    review_disc += read_out;
                }
                // Adicionar a review lida no vetor
                ClassesFilm.Review review_find = cf.new Review(filme_serie, review_disc, name_film,
                        Integer.parseInt(id));
                reviews.add(review_find);

                // Iterar até a próxima review
                while (br.readLine() != "-1\n")
                    ;

            }
            br.close();

        } catch (Exception e) {
            // TODO: handle exception
        }

        return reviews;
    }

    public void removeFromList(Integer id, ListaFilm l) {
        // pegar todos os ids
        ListaFilm list = getSaveIdsList(l);
        for (int i = 0; i < list.ids_midia_str.size(); i++) {
            if (list.ids_midia_int.get(i).equals(id)) {
                list.ids_midia_int.remove(i);
                list.ids_midia_str.remove(i);
                list.filme_ou_serie.remove(i);
            }
        }

        for (int i = 0; i < list.ids_midia_str.size(); i++) {
            System.out.println(list.ids_midia_int.get(i));
        }

        // abre o arquivo
        File list_ids = new File(l.file_path);
        try {
            // Instanciar um objeto para escrita para sobreescrever o arquivo
            FileWriter fw = new FileWriter(list_ids, false);
            BufferedWriter bw = new BufferedWriter(fw);

            // Escrever nome no arquivo
            bw.write(list.list_name + "\n");

            // escrever dados
            for (Integer i = 0; i < list.ids_midia_str.size(); i++) {
                bw.write(list.filme_ou_serie.get(i) + "\n");
                bw.write(list.ids_midia_str.get(i) + "\n");
                bw.write("-1\n");
            }
            // Fechar os recursos
            bw.close();
            fw.close();

        } catch (Exception e) {
            return;
        }
    }

    public static void main(String[] args) {

        String review_disc = "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum";

        ClassesFilm cf = new ClassesFilm();
        ClassesFilm.Review r = cf.new Review("-1", review_disc, "a", 1);

        Vector<ClassesFilm.Review> list_r = new Vector<ClassesFilm.Review>();
        list_r.add(r);

        FileManager fm = new FileManager();
        fm.saveReviews(list_r);
        File f = new File("reviews_saved/reviews_file");
        System.out.println("antes");
        ClassesFilm.Review g_r = fm.searchReview(f, "1");
        System.out.println("depois");

        System.out.println(g_r.name_film);
        System.out.println(g_r.review);

    }

}
