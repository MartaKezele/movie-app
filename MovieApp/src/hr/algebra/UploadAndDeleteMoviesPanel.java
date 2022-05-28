/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;

import hr.algebra.dal.EntityRepository;
import hr.algebra.dal.RepositoryFactory;
import hr.algebra.model.Movie;
import hr.algebra.model.MovieArchive;
import hr.algebra.parsers.rss.MovieParser;
import hr.algebra.utils.JAXBUtils;
import hr.algebra.utils.MessageUtils;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author kezel
 */
public class UploadAndDeleteMoviesPanel extends javax.swing.JPanel {

    private DefaultListModel<Movie> moviesModel;
    private EntityRepository<Movie> movieRepository;

    private static final String FILENAME = "moviearchive.xml";

    /**
     * Creates new form UploadAndDeleteMoviesPanel
     */
    public UploadAndDeleteMoviesPanel() {
        initComponents();
        init();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        lsMovies = new javax.swing.JList<>();
        jSplitPane1 = new javax.swing.JSplitPane();
        btnUpload = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();

        setMinimumSize(new java.awt.Dimension(500, 500));
        setPreferredSize(new java.awt.Dimension(1000, 800));

        jScrollPane1.setViewportView(lsMovies);

        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        btnUpload.setBackground(new java.awt.Color(51, 153, 255));
        btnUpload.setText("Upload movies");
        btnUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploadActionPerformed(evt);
            }
        });
        jSplitPane1.setTopComponent(btnUpload);

        btnDelete.setBackground(new java.awt.Color(255, 51, 51));
        btnDelete.setText("Delete movies");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        jSplitPane1.setBottomComponent(btnDelete);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 988, Short.MAX_VALUE)
                    .addComponent(jSplitPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed

        try {
            List<Movie> movies = movieRepository.selectAll();

            if (movies.isEmpty()) {
                MessageUtils.showInformationMessage("No movies", "There are no movies.");
            } else {
                if (MessageUtils.showConfirmDialog(
                        "Delete movies",
                        "Do you really want to delete all movies?") == JOptionPane.YES_OPTION) {
                    try {
                        for (Movie movie : movies) {
                            if (movie.getPoster() != null) {
                                Files.deleteIfExists(Paths.get(movie.getPoster()));
                            }
                            movieRepository.delete(movie.getId());
                        }

                        Files.deleteIfExists(Paths.get(FILENAME));
                        loadModel();
                    } catch (Exception ex) {
                        Logger.getLogger(UploadAndDeleteMoviesPanel.class.getName()).log(Level.SEVERE, null, ex);
                        MessageUtils.showErrorMessage("Error", "Unable to delete movies.");
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(UploadAndDeleteMoviesPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUploadActionPerformed

        if (moviesModel.isEmpty()) {
            try {
                movieRepository.create(MovieParser.parse());
                JAXBUtils.save(new MovieArchive(movieRepository.selectAll()), FILENAME);
                loadModel();
                MessageUtils.showInformationMessage("File saved", "Movies saved in file " + FILENAME + ".");
            } catch (Exception ex) {
                Logger.getLogger(UploadAndDeleteMoviesPanel.class.getName()).log(Level.SEVERE, null, ex);
                MessageUtils.showErrorMessage("Unrecoverable error", ex.getMessage());
                System.exit(1);
            }
        } else {
            MessageUtils.showInformationMessage("Delete existing movies", "Delete existing movies before uploading again.");
        }
    }//GEN-LAST:event_btnUploadActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnUpload;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JList<Movie> lsMovies;
    // End of variables declaration//GEN-END:variables

    private void init() {
        try {
            movieRepository = RepositoryFactory.getMovieRepository();
            moviesModel = new DefaultListModel<>();
            if (movieRepository.selectAll().isEmpty()) {
                Files.deleteIfExists(Paths.get(FILENAME));

            } else {
                loadModel();
            }
        } catch (Exception ex) {
            Logger.getLogger(UploadAndDeleteMoviesPanel.class.getName()).log(Level.SEVERE, null, ex);
            MessageUtils.showErrorMessage("Unrecoverable error", "Cannot initiate the form.");
            System.exit(1);
        }
    }

    private void loadModel() throws Exception {
        moviesModel.clear();
        if (Files.exists(Paths.get(FILENAME))) {

            MovieArchive movieArchive = (MovieArchive) JAXBUtils.load(MovieArchive.class, FILENAME);
            List<Movie> movies = movieArchive.getMovies();
            movies.forEach(moviesModel::addElement);
        }
        lsMovies.setModel(moviesModel);
    }
}