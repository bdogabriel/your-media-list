/*******************************************************************************
*                       USP - Universidade de São Paulo                        *
*            ICMC - Instituto de Ciências Matemáticas e de Computação          *
********************************************************************************
*--------------------Bacharelado em Ciências de Computação 2021----------------*
*   Eric Rodrigues das Chagas            NºUSP: 12623971  (contribuição: 25%)  *
*   Murillo Moraes Martins               NºUSP: 12701599  (contribuição: 25%)  *
*   Fernando César Lopes Barbosa Filho   NºUSP: 10260559  (contribuição: 25%)  *
*   Gabriel Barbosa de Oliveira          NºUSP: 12543415  (contribuição: 25%)  *
********************************************************************************
*                           Your Media List                                    *
*                  Programação Orientada a Objetos - SSC0103                   *
*                                  2022.2                                      *
********************************************************************************/

package grupo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

import org.apache.commons.lang3.ArrayUtils;

import grupo.ClassesFilm.ListaFilm;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.tv.TvSeries;
import java.util.Vector;

public class App extends JFrame {
    private JFrame frame;

    // fields to be placed in search menu
    private JTextField searchText = new JTextField();

    private JPanel resultsPanel = new JPanel();
    private JPanel searchPanel = new JPanel();
    private JPanel bottomMenu = new JPanel();

    private JButton searchButton = new JButton("Pesquisar");
    private JButton searchMenuButton = new JButton("Pesquisa");
    private JButton listWatchedButton = new JButton("Assistido");
    private JButton listWatchButton = new JButton("Assistir");
    private JButton reviewsButton = new JButton("Reviews");

    // variables regarding the search engine
    private TmdbApi api;
    private SearchEngine se;

    // variables to store the results of search
    public MovieDb movies[];
    public TvSeries series[];

    // variables related to file storage and manipulation
    MediaList ml;
    FileManager fm;

    ClassesFilm cf = new ClassesFilm();

    // List "Assistido"
    ClassesFilm.ListaFilm list = cf.new ListaFilm("Assistido\n");

    // List "Assistir"
    ClassesFilm.ListaFilm list2 = cf.new ListaFilm("Assistir\n");

    /**
     * updates the frame to display the current elements
     */
    private void updateFrame() {
        frame.invalidate();
        frame.validate();
        frame.repaint();
        SwingUtilities.updateComponentTreeUI(frame);
    }

    /**
     * cleans the frame.
     */
    private void cleanFrame() {
        frame.getContentPane().removeAll();
        updateFrame();
    }

    /**
     * menu for showing serie details
     * 
     * @param serie to see details
     */
    public void mediaScreen(TvSeries serie) {
        JPanel serieTitle = new JPanel();
        JPanel serieInfos = new JPanel();
        JButton addToList = new JButton("Assistido");
        JButton addToList2 = new JButton("Assistir");
        JButton addReview = new JButton("Review");

        cleanFrame();

        JScrollPane scrollPane = new JScrollPane(serieInfos, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        serieTitle.setLayout(new GridLayout(1, 3));
        serieInfos.setLayout(new BoxLayout(serieInfos, BoxLayout.X_AXIS));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        // cover
        TvSeries[] tmp = new TvSeries[1];
        tmp[0] = serie;
        BufferedImage cover[] = se.getCovers(tmp, "200");
        serieInfos.add(new JLabel(new ImageIcon(cover[0])));

        // title
        JTextArea t = new JTextArea(serie.getName());
        t.setEditable(false);
        serieTitle.add(t);

        // overview
        JTextArea o = new JTextArea(serie.getOverview());
        o.setEditable(false);
        o.setLineWrap(true);
        o.setWrapStyleWord(true);
        textPanel.add(o);

        // date
        JTextArea d = new JTextArea(serie.getFirstAirDate());
        d.setEditable(false);
        textPanel.add(d);

        // rating
        JTextArea r = new JTextArea();
        r.setText(serie.getVoteAverage() + " (" + serie.getVoteCount() + " votes)");
        textPanel.add(r);

        serieTitle.add(addToList);
        serieTitle.add(addToList2);
        serieTitle.add(addReview);
        serieTitle.add(searchMenuButton);

        serieInfos.add(textPanel);
        frame.add(serieTitle, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        updateFrame();
        addToList2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // adicionando filme à lista
                JOptionPane.showMessageDialog(frame, "Série adicionada à lista \"Assistir\"");
                ml.addSerie(serie, serie.getId());
                list2.ids_midia_str = ml.idListToString();
                list2.filme_ou_serie = new Vector<String>();
                for (int i = 0; i < list2.ids_midia_str.size(); i++) {
                    list2.filme_ou_serie.add("1\n");
                }
                ml.removeSerie(serie, serie.getId());

                fm.saveIdsList(list2);
            }
        });
        addToList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // adicionando filme à lista
                JOptionPane.showMessageDialog(frame, "Série adicionada à lista \"Assistido\"");
                ml.addSerie(serie, serie.getId());
                list.ids_midia_str = ml.idListToString();
                list.filme_ou_serie = new Vector<String>();
                for (int i = 0; i < list.ids_midia_str.size(); i++) {
                    list.filme_ou_serie.add("1\n");
                }
                ml.removeSerie(serie, serie.getId());

                fm.saveIdsList(list);
            }
        });

        searchMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cleanFrame();
                searchMenu();
            }
        });
    }

    /**
     * menu for showing movie details
     * 
     * @param movie to see details
     */
    public void mediaScreen(MovieDb movie) {
        JPanel movieTitle = new JPanel();
        JPanel movieInfos = new JPanel();
        JButton addToList = new JButton("Assistido");
        JButton addToList2 = new JButton("Assistir");
        JButton addReview = new JButton("Review");

        cleanFrame();

        JScrollPane scrollPane = new JScrollPane(movieInfos, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        movieTitle.setLayout(new GridLayout(1, 3));
        movieInfos.setLayout(new BoxLayout(movieInfos, BoxLayout.X_AXIS));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        // cover
        MovieDb[] tmp = new MovieDb[1];
        tmp[0] = movie;
        BufferedImage cover[] = se.getCovers(tmp, "200");
        movieInfos.add(new JLabel(new ImageIcon(cover[0])));

        // title
        JTextArea t = new JTextArea(movie.getTitle());
        t.setEditable(false);
        movieTitle.add(t);

        // overview
        JTextArea o = new JTextArea(movie.getOverview());
        o.setEditable(false);
        o.setLineWrap(true);
        o.setWrapStyleWord(true);
        textPanel.add(o);

        // date
        JTextArea d = new JTextArea(movie.getReleaseDate());
        d.setEditable(false);
        textPanel.add(d);

        // rating
        JTextArea r = new JTextArea();
        r.setText(movie.getVoteAverage() + " (" + movie.getVoteCount() + " votes)");
        textPanel.add(r);

        movieTitle.add(addToList);
        movieTitle.add(addToList2);
        movieTitle.add(addReview);
        movieTitle.add(searchMenuButton);

        movieInfos.add(textPanel);
        frame.add(movieTitle, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        updateFrame();
        addToList2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // adicionando filme à lista
                JOptionPane.showMessageDialog(frame, "Filme adicionado à lista \"Assistir\"");
                ml.addMovie(movie, movie.getId());

                list2.ids_midia_str = ml.idListToString();
                list2.filme_ou_serie = new Vector<String>();
                for (int i = 0; i < list2.ids_midia_str.size(); i++) {
                    list2.filme_ou_serie.add("0\n");
                }
                ml.removeMovie(movie, movie.getId());

                fm.saveIdsList(list2);
            }
        });

        addToList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // adicionando filme à lista
                JOptionPane.showMessageDialog(frame, "Filme adicionado à lista \"Assistido\"");
                ml.addMovie(movie, movie.getId());
                list.ids_midia_str = ml.idListToString();
                list.filme_ou_serie = new Vector<String>();
                for (int i = 0; i < list.ids_midia_str.size(); i++) {
                    list.filme_ou_serie.add("0\n");
                }
                ml.removeMovie(movie, movie.getId());

                fm.saveIdsList(list);
            }
        });

        searchMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cleanFrame();
                searchMenu();
            }
        });
    }

    /**
     * menu for searching movies
     */
    public void searchMenu() {
        // setting layout of elements in frame
        JScrollPane scrollPane = new JScrollPane(resultsPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
        bottomMenu.setLayout(new GridLayout(1, 2));
        frame.setLayout(new BorderLayout());

        bottomMenu.add(listWatchedButton);
        bottomMenu.add(listWatchButton);
        bottomMenu.add(reviewsButton);
        searchPanel.add(searchText);
        searchPanel.add(searchButton);
        frame.add(searchPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottomMenu, BorderLayout.SOUTH);

        frame.setVisible(true);

        // event for clicking "Pesquisar"
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                search(searchText.getText());
            }
        });

        // event for clicking "Assistido"
        listWatchedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ListMenu(list);
            }
        });

        // event for clicking "Assistir"
        listWatchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ListMenu(list2);
            }
        });

        // event for clicking "Reviews"
        reviewsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("reviews");
            }
        });

        updateFrame();
    }

    public void ListMenu(ListaFilm l) {
        // reiniciando o frame
        resultsPanel.removeAll();

        updateFrame();

        // pegando a lsita dos ids salvos
        ClassesFilm.ListaFilm list = fm.getSaveIdsList(l);
        if (list == null) {
            // se o primeiro elemento for nulo, então a lista está vazia
            BoxLayout lay = new BoxLayout(resultsPanel, BoxLayout.Y_AXIS);
            resultsPanel.setLayout(lay);
            resultsPanel.add(new JTextArea());
            JTextArea text = (JTextArea) resultsPanel.getComponent(0);
            text.setText("Não há filmes nessa lista.");
            text.setEditable(false);
            updateFrame();
        }

        else {
            // se houver elementos
            // iteramos pelos ids, adicionando filmes e séries
            Vector<MovieDb> m = new Vector<MovieDb>();
            Vector<TvSeries> s = new Vector<TvSeries>();
            for (int i = 0; i < list.ids_midia_int.size(); i++) {
                System.out.println(list.filme_ou_serie.get(i));
                if (list.filme_ou_serie.get(i).equals("0"))
                    m.add(se.FindMovieByID(list.ids_midia_int.get(i)));
                else
                    s.add(se.FindSeriesByID(list.ids_midia_int.get(i)));

            }

            // convertendo para array
            movies = new MovieDb[m.size()];
            series = new TvSeries[s.size()];
            for (int i = 0; i < m.size(); i++) {
                movies[i] = m.get(i);
            }
            for (int i = 0; i < s.size(); i++) {
                series[i] = s.get(i);
            }

            // pegando covers
            BufferedImage[] covers = se.getCovers(movies, "45");
            covers = ArrayUtils.addAll(covers, se.getCovers(series, "45"));

            // criando um layout
            BoxLayout lay = new BoxLayout(resultsPanel, BoxLayout.Y_AXIS);
            resultsPanel.setLayout(lay);
            for (int i = 0, k = 0; i < covers.length; i++) {
                // setando o layout de cada filme
                resultsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                JPanel resultBox = new JPanel();
                resultBox.setLayout(new BoxLayout(resultBox, BoxLayout.X_AXIS));
                // criando cover
                ImageIcon cover = new ImageIcon(covers[i]);
                resultBox.add(new JLabel(cover));
                resultBox.add(new JPanel());
                // criando textos
                JPanel textBox = (JPanel) resultBox.getComponent(1);
                textBox.setLayout(new BoxLayout(textBox, BoxLayout.Y_AXIS));
                textBox.add(new JTextArea());
                textBox.add(new JTextArea());
                JTextArea title = (JTextArea) textBox.getComponent(0);
                JTextArea date = (JTextArea) textBox.getComponent(1);
                if (i < movies.length) {
                    title.setText(movies[i].getTitle());
                    date.setText(movies[i].getReleaseDate());
                } else {
                    title.setText(series[k].getName());
                    date.setText(series[k].getFirstAirDate());
                    k++;
                }
                // criando botão de remover
                resultBox.add(new JPanel());
                JPanel buttonBox = (JPanel) resultBox.getComponent(2);
                JButton removeButton = new JButton("Remover");
                buttonBox.add(removeButton);

                title.setEditable(false);
                date.setEditable(false);
                resultsPanel.add(resultBox);
                updateFrame();

                int j = i;
                int n = k - 1;

                // evento para botão de remover
                removeButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (j < movies.length) {
                            System.out.println("removendo: " + movies[j].getId());
                            fm.removeFromList(movies[j].getId(), l);
                        } else {
                            System.out.println("removendo: " + series[n].getId());
                            fm.removeFromList(series[n].getId(), l);
                        }
                        ListMenu(l);
                    }
                });
            }
        }
    }

    /**
     * searches for a movie on the db.
     * 
     * @param text to be searched in db.
     */
    public void search(String text) {
        // volta se o campo de texto está vazio
        if (text.isBlank())
            return;

        // Buscar filmes e reiniciar o frame
        resultsPanel.removeAll();
        bottomMenu.add(listWatchedButton, 0);
        bottomMenu.add(listWatchButton, 1);
        movies = se.searchMovies(text);
        series = se.searchSeries(text);

        System.out.println(movies.length);
        System.out.println(series.length);

        BufferedImage[] covers = se.getCovers(movies, "45");
        covers = ArrayUtils.addAll(covers, se.getCovers(series, "45"));
        System.out.println(covers.length);

        resultsPanel.removeAll();
        BoxLayout lay = new BoxLayout(resultsPanel, BoxLayout.Y_AXIS);
        resultsPanel.setLayout(lay);

        for (int i = 0, k = 0, m = -1; i < covers.length; i++, k++) {
            // setando o layout de cada filme
            resultsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            JPanel resultBox = new JPanel();
            resultBox.setLayout(new BoxLayout(resultBox, BoxLayout.X_AXIS));
            ImageIcon cover = new ImageIcon(covers[i]);
            resultBox.add(new JLabel(cover));
            resultBox.add(new JPanel());
            JPanel textBox = (JPanel) resultBox.getComponent(1);
            textBox.setLayout(new BoxLayout(textBox, BoxLayout.Y_AXIS));
            textBox.add(new JTextArea());
            textBox.add(new JTextArea());
            textBox.add(new JTextArea());
            JTextArea title = (JTextArea) textBox.getComponent(0);
            JTextArea rating = (JTextArea) textBox.getComponent(1);
            JTextArea date = (JTextArea) textBox.getComponent(2);

            if (k < movies.length) {
                title.setText(movies[i].getTitle());
                rating.setText(movies[i].getVoteAverage() + " (" + movies[i].getVoteCount() + " votes)");
                date.setText(movies[i].getReleaseDate());
            }

            else {
                m++;
                title.setText(series[m].getName());
                rating.setText(series[m].getVoteAverage() + " (" + series[m].getVoteCount() + " votes)");
                date.setText(series[m].getFirstAirDate());
            }

            title.setEditable(false);
            rating.setEditable(false);
            date.setEditable(false);
            resultsPanel.add(resultBox);

            int j = i;
            int n = m;

            System.out.println("n " + n);

            // event for clicking movie cover
            resultBox.addMouseListener(new MouseInputListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.out.println(title.getText());
                    if (j < movies.length) {
                        mediaScreen(movies[j]);
                    }

                    else {
                        mediaScreen(series[n]);
                    }
                }

                @Override
                public void mouseEntered(MouseEvent arg0) {
                }

                @Override
                public void mouseExited(MouseEvent arg0) {
                }

                @Override
                public void mousePressed(MouseEvent arg0) {
                }

                @Override
                public void mouseReleased(MouseEvent arg0) {
                }

                @Override
                public void mouseDragged(MouseEvent arg0) {
                }

                @Override
                public void mouseMoved(MouseEvent arg0) {
                }
            });

            // event for clicking movie title
            title.addMouseListener(new MouseInputListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.out.println(title.getText());
                    if (j < movies.length) {
                        mediaScreen(movies[j]);
                    }

                    else {
                        mediaScreen(series[n]);
                    }
                }

                @Override
                public void mouseEntered(MouseEvent arg0) {
                }

                @Override
                public void mouseExited(MouseEvent arg0) {
                }

                @Override
                public void mousePressed(MouseEvent arg0) {
                }

                @Override
                public void mouseReleased(MouseEvent arg0) {
                }

                @Override
                public void mouseDragged(MouseEvent arg0) {
                }

                @Override
                public void mouseMoved(MouseEvent arg0) {
                }
            });

            updateFrame();

        }
    }

    public App() {
        frame = new JFrame("Trabalho");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        // initializing search engine
        api = new TmdbApi("813da1ea51be04e7c3cecf6e417f67dc");
        se = new SearchEngine(api);

        // initializing file manager
        fm = new FileManager();
        ml = new MediaList("assitindo", null, null, null, null);

        searchMenu();
    }

    public static void main(String[] args) {
        new App();
    }
}