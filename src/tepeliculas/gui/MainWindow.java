package tepeliculas.gui;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableColumn;
import javax.swing.text.NumberFormatter;
import tepeliculas.dao.ClassificationDAO;
import tepeliculas.dao.CountryDAO;
import tepeliculas.dao.DirectorDAO;
import tepeliculas.dao.GenreDAO;
import tepeliculas.dao.MovieDAO;
import tepeliculas.dto.Classification;
import tepeliculas.dto.Country;
import tepeliculas.dto.Director;
import tepeliculas.dto.Gender;
import tepeliculas.dto.Genre;
import tepeliculas.dto.Movie;
import tepeliculas.gui.tablecelleditors.ComboBoxCellEditor;
import tepeliculas.gui.tablecelleditors.SpinnerDateCellEditor;
import tepeliculas.tmodels.TModelDirectors;
import tepeliculas.tmodels.TModelMovies;
import tepeliculas.tmodels.TModelSimple;

/**
 *
 * @author igor
 */
public class MainWindow extends javax.swing.JFrame {
    private static final String TITLE_OF_MAIN_WINDOW = "TeMovieManager";
    private static final String TITLE_DIRECTOR_REGISTER_FORM = "Director registration";
    private static final String TITLE_CLASSIFICATION_REGISTER_FORM = "Classification registration";
    private static final String TITLE_GENRE_REGISTER_FORM = "Genre registration";
    private static final String TITLE_COUNTRY_REGISTER_FORM = "Country registration";
    private static final String TITLE_DIRECTOR_LISTING_WINDOW = "Director listing";
    private static final String TITLE_CLASSIFICATION_LISTING_WINDOW = "Classifiaction listing";
    private static final String TITLE_GENRE_LISTING_WINDOW = "Genre listing";
    private static final String TITLE_COUNTRY_LISTING_WINDOW = "Country listing";

    private static final int ROW_HEIGHT = 30;
    private static final int DURATION_MIN = 1;
    private static final int DURATION_MAX = 3*31*24*60;
    private static final int RATING_MAX = 100;
    private static final String NO_PHOTO_PATH = "img/noPhoto.png";
    private static final Renderer renderer = new Renderer();
    private static final List<Gender> genders = new ArrayList<>();
    private final ComboBoxCellEditor<Gender> comboEditorGender;
    private final MovieDAO daoMovie;
    private final ClassificationDAO daoClassification;
    private final GenreDAO daoGenre;
    private final DirectorDAO daoDirector;
    private final CountryDAO daoCountry;
    private String imagePath = null;

    public MainWindow() {
        genders.add(Gender.Male);
        genders.add(Gender.Female);
        comboEditorGender = new ComboBoxCellEditor<>(Gender.class, genders);
        daoMovie = new MovieDAO();
        daoClassification = new ClassificationDAO();
        daoGenre = new GenreDAO();
        daoDirector = new DirectorDAO();
        daoCountry = new CountryDAO();
        initComponents();
        adjustGUI();
        adjustSpinners();
        loadCombos();
        List<Movie> list = daoMovie.readAll();
        updateTableOfMovies(list);
        updateTableOfDirectors();
        Calendar cal = Calendar.getInstance();
        txtDirectorDOB.setDate(cal.getTime());
        updateTableOfCountries();
        updateTableOfClassifications();
        updateTableOfGenres();
    }
    
    private void adjustGUI(){
        this.setTitle(TITLE_OF_MAIN_WINDOW);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        tableOfMovies.setRowHeight(ROW_HEIGHT);
        tableOfMovies.setAutoCreateRowSorter(true);
        txtID.setEditable(false);
        resetInputForm();
        resetSearchForm();
        //Registration forms
        formRegisterDirector.setTitle(TITLE_DIRECTOR_REGISTER_FORM);
        formRegisterDirector.setResizable(false);
        formRegisterDirector.setSize(288, 240);
        formRegisterDirector.setLocationRelativeTo(null);
        //--------------------------------------
        formRegisterCountry.setTitle(TITLE_COUNTRY_REGISTER_FORM);
        formRegisterCountry.setResizable(false);
        formRegisterCountry.setSize(230, 120);
        formRegisterCountry.setLocationRelativeTo(null);
        //--------------------------------------
        formRegisterClassification.setTitle(TITLE_CLASSIFICATION_REGISTER_FORM);
        formRegisterClassification.setResizable(false);
        formRegisterClassification.setSize(230, 120);
        formRegisterClassification.setLocationRelativeTo(null);
        //--------------------------------------
        formRegisterGenre.setTitle(TITLE_GENRE_REGISTER_FORM);
        formRegisterGenre.setResizable(false);
        formRegisterGenre.setSize(250, 120);
        formRegisterGenre.setLocationRelativeTo(null);
        //Windows with lists
        windowListOfDirectors.setTitle(TITLE_DIRECTOR_LISTING_WINDOW);
        windowListOfDirectors.setResizable(false);
        windowListOfDirectors.setSize(800, 480);
        windowListOfDirectors.setLocationRelativeTo(null);
        tableOfDirectors.setDefaultRenderer(Object.class, renderer);
        tableOfDirectors.setRowHeight(ROW_HEIGHT);
        tableOfDirectors.setAutoCreateRowSorter(true);
        //--------------------------------------------
        windowListOfCountries.setTitle(TITLE_COUNTRY_LISTING_WINDOW);
        windowListOfCountries.setResizable(false);
        windowListOfCountries.setSize(460, 470);
        windowListOfCountries.setLocationRelativeTo(null);
        tableOfCountries.setDefaultRenderer(Object.class, renderer);
        tableOfCountries.setRowHeight(ROW_HEIGHT);
        tableOfCountries.setAutoCreateRowSorter(true);
        //--------------------------------------------
        windowListOfClassifications.setTitle(TITLE_CLASSIFICATION_LISTING_WINDOW);
        windowListOfClassifications.setResizable(false);
        windowListOfClassifications.setSize(450, 470);
        windowListOfClassifications.setLocationRelativeTo(null);
        tableOfClassifications.setDefaultRenderer(Object.class, renderer);
        tableOfClassifications.setRowHeight(ROW_HEIGHT);
        tableOfClassifications.setAutoCreateRowSorter(true);
        //--------------------------------------------
        windowListOfGenres.setTitle(TITLE_GENRE_LISTING_WINDOW);
        windowListOfGenres.setResizable(false);
        windowListOfGenres.setSize(480, 470);
        windowListOfGenres.setLocationRelativeTo(null);
        tableOfGenres.setDefaultRenderer(Object.class, renderer);
        tableOfGenres.setRowHeight(ROW_HEIGHT);
        tableOfGenres.setAutoCreateRowSorter(true);
     }
    
    private void loadCombos(){
        reloadComboClassifications();
        reloadComboGenres();
        reloadComboDirectors();
        reloadComboCountries();
    }
    
    private void reloadComboClassifications(){
        comboClassification.removeAllItems();
        comboSearchClassification.removeAllItems();
        List<Classification> list_class = daoClassification.readAll();
        for(Classification c : list_class){
            comboClassification.addItem(c);
            comboSearchClassification.addItem(c);
        }
    }
    
    private void reloadComboGenres(){
        comboGenre.removeAllItems();
        comboSearchGenre.removeAllItems();
        List<Genre> list = daoGenre.readAll();
        for(Genre g : list){
            comboGenre.addItem(g);
            comboSearchGenre.addItem(g);
        }
    }
    
    private void reloadComboDirectors(){
        comboDirector.removeAllItems();
        comboSearchDirector.removeAllItems();
        List<Director> list = daoDirector.readAll();
        for(Director d : list){
            comboDirector.addItem(d);
            comboSearchDirector.addItem(d);
        }
    }
    
    private void reloadComboCountries(){
        comboSearchCountry.removeAllItems();
        comboCountryOfDirector.removeAllItems();
        List<Country> list = daoCountry.readAll();
        for(Country c : list){
            comboSearchCountry.addItem(c);
            comboCountryOfDirector.addItem(c);
        }
    }
    
    private void adjustSpinners(){
        setNumericValidatorForSpinner(spinnerDuration, 1, DURATION_MIN,
                DURATION_MAX, 1);
        setNumericValidatorForSpinner(spinnerRating, 1, 1, RATING_MAX, 1);
        setNumericValidatorForSpinner(spinnerDurationMIN, 1, 1, DURATION_MIN, 1);
        setNumericValidatorForSpinner(spinnerDurationMAX, 1, 1, DURATION_MAX, 1);
        setNumericValidatorForSpinner(spinnerRatingMIN, 1, 1, RATING_MAX, 1);
        setNumericValidatorForSpinner(spinnerRatingMAX, 1, 1, RATING_MAX, 1);
    }
    
    private void setNumericValidatorForSpinner(JSpinner spinner, int val,
            int min, int max, int step){
        SpinnerNumberModel model = new SpinnerNumberModel(val, min, max, step);
        spinner.setModel(model);
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinner,
                "##########");
        NumberFormatter formatter = (NumberFormatter)editor.getTextField()
                .getFormatter();
        formatter.setAllowsInvalid(false);
        formatter.setOverwriteMode(true);
        spinner.setEditor(editor);
    }
    
    private void updateTableOfMovies(List<Movie> list){        
        tableOfMovies.setModel(new TModelMovies(list));
    }
    
    private void updateTableOfDirectors(){
        tableOfDirectors.setModel(new TModelDirectors(daoDirector.readAll()));
        TableColumn columnGender = tableOfDirectors.getColumnModel()
                .getColumn(TModelDirectors.COLUMN_GENDER);
        columnGender.setCellEditor(comboEditorGender);  
        ComboBoxCellEditor comboCountries = new ComboBoxCellEditor(
                Country.class, daoCountry.readAll());
        tableOfDirectors.setDefaultEditor(Country.class, comboCountries);
        TableColumn tc_date = tableOfDirectors.getColumnModel().getColumn(
                TModelDirectors.COLUMN_DATE);
        tc_date.setCellEditor(new SpinnerDateCellEditor());
    }
    
    private void updateTableOfClassifications(){
        tableOfClassifications.setModel(
                new TModelSimple(daoClassification.readAll()));
    }
    
    private void updateTableOfCountries(){
        tableOfCountries.setModel(new TModelSimple(daoCountry.readAll()));
    }
    
    private void updateTableOfGenres(){
        tableOfGenres.setModel(new TModelSimple(daoGenre.readAll()));
    }
    
    private ImageIcon getScaledImage(String path, byte[] pic){
        ImageIcon icn;
        if(path != null){
            icn = new ImageIcon(path);
        } else if(pic != null){
            icn = new ImageIcon(pic);
        } else {
            icn = new ImageIcon(MainWindow.class.getResource(NO_PHOTO_PATH));
        }
        Image scaledImage = icn.getImage().getScaledInstance(
            lblPoster.getWidth(),
            lblPoster.getHeight(),
            Image.SCALE_SMOOTH
        );
        return new ImageIcon(scaledImage);
    }
    
    private boolean checkInput(){
        return !txtName.getText().isEmpty() &&
               !txtAreaDesc.getText().isEmpty() &&
               ((int)spinnerDuration.getValue() != 0);
    }
    
    private Movie createMovieFromUserInput(){
        Movie movie = new Movie();
        String name = txtName.getText();
        movie.setName(name);
        String description = txtAreaDesc.getText();
        movie.setDescription(description);
        Classification c = (Classification)comboClassification.getSelectedItem();
        int fk_classification = c.getId();
        movie.setClassification(fk_classification);
        Genre g = (Genre)comboGenre.getSelectedItem();
        int fk_genre = g.getId();
        movie.setGenre(fk_genre);
        int duration = (int)spinnerDuration.getValue();
        movie.setDuration(duration);
        java.sql.Date rd = new java.sql.Date(txtReleaseDate.getDate().getTime());
        movie.setDate(rd);
        Director d = (Director)comboDirector.getSelectedItem();
        int fk_director = d.getId();
        movie.setDirector(fk_director);
        int rating = (int)spinnerRating.getValue();
        movie.setRating(rating);
        try(InputStream is = (imagePath == null) ?
            MainWindow.class.getResourceAsStream(NO_PHOTO_PATH) :
            new FileInputStream(new File(imagePath))){
            byte[] bytes = new byte[is.available()];
            is.read(bytes);
            movie.setPicture(bytes);
        } catch (IOException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        return movie;
    }
    
    private void resetInputForm(){
        txtID.setText(null);
        txtName.setText(null);
        txtAreaDesc.setText(null);
        if(comboClassification.getItemCount() > 0){
            comboClassification.setSelectedIndex(0);
        }
        if(comboGenre.getItemCount() > 0){
            comboGenre.setSelectedIndex(0);
        }
        spinnerDuration.setValue(DURATION_MIN);
        Calendar cal = Calendar.getInstance();
        txtReleaseDate.setDate(cal.getTime());
        if(comboDirector.getItemCount() > 0){
            comboDirector.setSelectedIndex(0);
        }
        spinnerRating.setValue(0);
        lblPoster.setIcon(null);
    }
    
    private void showSelectedMovie(){
        int row = tableOfMovies.getSelectedRow();
        Movie selectedMovie = ((TModelMovies)tableOfMovies.getModel()).getMovie(row);
        txtID.setText(String.valueOf(selectedMovie.getId()));
        txtName.setText(selectedMovie.getName());
        txtAreaDesc.setText(selectedMovie.getDescription());
        int id_classification = selectedMovie.getClassification();
        Classification c = daoClassification.read(id_classification);
        for(int i = 0; i < comboClassification.getItemCount(); ++i){
            if(comboClassification.getItemAt(i).equals(c)){
                comboClassification.setSelectedIndex(i);
                break;
            }
        }
        int id_genre = selectedMovie.getGenre();
        Genre g = daoGenre.read(id_genre);
        for(int i = 0; i < comboGenre.getItemCount(); ++i){
            if(comboGenre.getItemAt(i).equals(g)){
                comboGenre.setSelectedIndex(i);
                break;
            }
        }
        spinnerDuration.setValue(selectedMovie.getDuration());
        txtReleaseDate.setDate(new java.util.Date(selectedMovie.getDate().getTime()));
        int id_director = selectedMovie.getDirector();
        Director d = daoDirector.read(id_director);
        for(int i = 0; i < comboDirector.getItemCount(); ++i){
            if(comboDirector.getItemAt(i).equals(d)){
                comboDirector.setSelectedIndex(i);
                break;
            }
        }
        spinnerRating.setValue(selectedMovie.getRating());
        lblPoster.setIcon(getScaledImage(null, selectedMovie.getPicture()));       
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        formRegisterDirector = new javax.swing.JFrame();
        btnRegisterDirector = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        rbtnMale = new javax.swing.JRadioButton();
        rbtnFemale = new javax.swing.JRadioButton();
        txtDirectorNameReg = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        comboCountryOfDirector = new javax.swing.JComboBox();
        txtDirectorDOB = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        btnCloseDirectorRegisterWindow = new javax.swing.JToggleButton();
        buttonGroup1 = new javax.swing.ButtonGroup();
        formRegisterCountry = new javax.swing.JFrame();
        txtCountryNameRegister = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        btnRegisterCountry = new javax.swing.JToggleButton();
        btnCloseCountryRegisterForm = new javax.swing.JToggleButton();
        formRegisterClassification = new javax.swing.JFrame();
        txtClassificationNameRegister = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        btnRegisterClassification = new javax.swing.JToggleButton();
        btnCloseClassificationRegisterForm = new javax.swing.JToggleButton();
        formRegisterGenre = new javax.swing.JFrame();
        txtGenreNameRegister = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        btnRegisterGenre = new javax.swing.JToggleButton();
        btnCloseGenreRegisterForm = new javax.swing.JToggleButton();
        windowListOfCountries = new javax.swing.JFrame();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableOfCountries = new javax.swing.JTable();
        jLabel20 = new javax.swing.JLabel();
        windowListOfDirectors = new javax.swing.JFrame();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableOfDirectors = new javax.swing.JTable();
        jLabel23 = new javax.swing.JLabel();
        windowListOfClassifications = new javax.swing.JFrame();
        jScrollPane5 = new javax.swing.JScrollPane();
        tableOfClassifications = new javax.swing.JTable();
        jLabel24 = new javax.swing.JLabel();
        windowListOfGenres = new javax.swing.JFrame();
        jScrollPane6 = new javax.swing.JScrollPane();
        tableOfGenres = new javax.swing.JTable();
        jLabel25 = new javax.swing.JLabel();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableOfMovies = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnInsertMovie = new javax.swing.JButton();
        btnUpdateMovie = new javax.swing.JButton();
        btnDeleteMovie = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnPrevious = new javax.swing.JButton();
        btnFirst = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        btnSearch = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        checkBoxClassification = new javax.swing.JCheckBox();
        txtMovieNameSearch = new javax.swing.JTextField();
        comboSearchClassification = new javax.swing.JComboBox();
        comboSearchGenre = new javax.swing.JComboBox();
        checkBoxGenre = new javax.swing.JCheckBox();
        checkBoxDuration = new javax.swing.JCheckBox();
        spinnerDurationMIN = new javax.swing.JSpinner();
        jLabel14 = new javax.swing.JLabel();
        spinnerDurationMAX = new javax.swing.JSpinner();
        jLabel15 = new javax.swing.JLabel();
        txtMovieDateMAX = new com.toedter.calendar.JDateChooser();
        txtMovieDateMIN = new com.toedter.calendar.JDateChooser();
        checkBoxDate = new javax.swing.JCheckBox();
        checkBoxDirector = new javax.swing.JCheckBox();
        comboSearchDirector = new javax.swing.JComboBox();
        spinnerRatingMAX = new javax.swing.JSpinner();
        spinnerRatingMIN = new javax.swing.JSpinner();
        checkBoxRating = new javax.swing.JCheckBox();
        btnFiveBestMovies = new javax.swing.JButton();
        checkBoxCountry = new javax.swing.JCheckBox();
        comboSearchCountry = new javax.swing.JComboBox();
        jLabel21 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtID = new javax.swing.JTextField();
        txtName = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAreaDesc = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        comboClassification = new javax.swing.JComboBox();
        comboGenre = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        txtReleaseDate = new com.toedter.calendar.JDateChooser();
        spinnerDuration = new javax.swing.JSpinner();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        comboDirector = new javax.swing.JComboBox();
        lblPoster = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        spinnerRating = new javax.swing.JSpinner();
        btnChooseImage = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuRegister = new javax.swing.JMenu();
        menuItemRegisterCountry = new javax.swing.JMenuItem();
        menuItemRegisterClssification = new javax.swing.JMenuItem();
        menuItemRegisterGenre = new javax.swing.JMenuItem();
        menuItemRegisterDirector = new javax.swing.JMenuItem();
        menuList = new javax.swing.JMenu();
        menuItemListOfCountries = new javax.swing.JMenuItem();
        menuItemListOfClassifications = new javax.swing.JMenuItem();
        menuItemListOfGenres = new javax.swing.JMenuItem();
        menuItemListOfDirectors = new javax.swing.JMenuItem();
        menuExit = new javax.swing.JMenu();
        menuItemExit = new javax.swing.JMenuItem();

        formRegisterDirector.setTitle("Director register form");
        formRegisterDirector.setAlwaysOnTop(true);
        formRegisterDirector.setResizable(false);

        btnRegisterDirector.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnRegisterDirector.setText("Register");
        btnRegisterDirector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterDirectorActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Name:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Gender:");

        buttonGroup2.add(rbtnMale);
        rbtnMale.setSelected(true);
        rbtnMale.setText("Male");

        buttonGroup2.add(rbtnFemale);
        rbtnFemale.setText("Female");

        txtDirectorNameReg.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Country:");

        comboCountryOfDirector.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        txtDirectorDOB.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Date of birth:");

        btnCloseDirectorRegisterWindow.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnCloseDirectorRegisterWindow.setText("Close");
        btnCloseDirectorRegisterWindow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseDirectorRegisterWindowActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout formRegisterDirectorLayout = new javax.swing.GroupLayout(formRegisterDirector.getContentPane());
        formRegisterDirector.getContentPane().setLayout(formRegisterDirectorLayout);
        formRegisterDirectorLayout.setHorizontalGroup(
            formRegisterDirectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formRegisterDirectorLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGap(75, 75, 75)
                .addComponent(txtDirectorNameReg, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(formRegisterDirectorLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel2)
                .addGap(55, 55, 55)
                .addComponent(rbtnMale)
                .addGap(1, 1, 1)
                .addComponent(rbtnFemale))
            .addGroup(formRegisterDirectorLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel3)
                .addGap(57, 57, 57)
                .addComponent(comboCountryOfDirector, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(formRegisterDirectorLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel4)
                .addGap(28, 28, 28)
                .addComponent(txtDirectorDOB, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(formRegisterDirectorLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(btnRegisterDirector)
                .addGap(4, 4, 4)
                .addComponent(btnCloseDirectorRegisterWindow, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        formRegisterDirectorLayout.setVerticalGroup(
            formRegisterDirectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formRegisterDirectorLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(formRegisterDirectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(formRegisterDirectorLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel1))
                    .addComponent(txtDirectorNameReg, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(formRegisterDirectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(rbtnMale)
                    .addComponent(rbtnFemale))
                .addGap(17, 17, 17)
                .addGroup(formRegisterDirectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(comboCountryOfDirector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(formRegisterDirectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(formRegisterDirectorLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel4))
                    .addComponent(txtDirectorDOB, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(formRegisterDirectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRegisterDirector)
                    .addComponent(btnCloseDirectorRegisterWindow)))
        );

        formRegisterCountry.setLocationByPlatform(true);
        formRegisterCountry.setResizable(false);
        formRegisterCountry.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        formRegisterCountry.getContentPane().add(txtCountryNameRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(69, 12, 157, -1));

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel17.setText("Name:");
        formRegisterCountry.getContentPane().add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 12, -1, -1));

        btnRegisterCountry.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnRegisterCountry.setText("Register");
        btnRegisterCountry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterCountryActionPerformed(evt);
            }
        });
        formRegisterCountry.getContentPane().add(btnRegisterCountry, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 49, -1, -1));

        btnCloseCountryRegisterForm.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnCloseCountryRegisterForm.setText("Close");
        btnCloseCountryRegisterForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseCountryRegisterFormActionPerformed(evt);
            }
        });
        formRegisterCountry.getContentPane().add(btnCloseCountryRegisterForm, new org.netbeans.lib.awtextra.AbsoluteConstraints(118, 49, 108, -1));

        formRegisterClassification.setLocationByPlatform(true);
        formRegisterClassification.setResizable(false);
        formRegisterClassification.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        formRegisterClassification.getContentPane().add(txtClassificationNameRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(69, 12, 157, -1));

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel26.setText("Name:");
        formRegisterClassification.getContentPane().add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 12, -1, -1));

        btnRegisterClassification.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnRegisterClassification.setText("Register");
        btnRegisterClassification.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterClassificationActionPerformed(evt);
            }
        });
        formRegisterClassification.getContentPane().add(btnRegisterClassification, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 49, -1, -1));

        btnCloseClassificationRegisterForm.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnCloseClassificationRegisterForm.setText("Close");
        btnCloseClassificationRegisterForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseClassificationRegisterFormActionPerformed(evt);
            }
        });
        formRegisterClassification.getContentPane().add(btnCloseClassificationRegisterForm, new org.netbeans.lib.awtextra.AbsoluteConstraints(118, 49, 108, -1));

        formRegisterGenre.setLocationByPlatform(true);
        formRegisterGenre.setResizable(false);
        formRegisterGenre.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        formRegisterGenre.getContentPane().add(txtGenreNameRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(69, 12, 157, -1));

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel27.setText("Name:");
        formRegisterGenre.getContentPane().add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 12, -1, -1));

        btnRegisterGenre.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnRegisterGenre.setText("Register");
        btnRegisterGenre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterGenreActionPerformed(evt);
            }
        });
        formRegisterGenre.getContentPane().add(btnRegisterGenre, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 49, -1, -1));

        btnCloseGenreRegisterForm.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnCloseGenreRegisterForm.setText("Close");
        btnCloseGenreRegisterForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseGenreRegisterFormActionPerformed(evt);
            }
        });
        formRegisterGenre.getContentPane().add(btnCloseGenreRegisterForm, new org.netbeans.lib.awtextra.AbsoluteConstraints(118, 49, 108, -1));

        windowListOfCountries.setTitle("List of countries");
        windowListOfCountries.setResizable(false);
        windowListOfCountries.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tableOfCountries.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableOfCountries.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tableOfCountriesMouseReleased(evt);
            }
        });
        jScrollPane3.setViewportView(tableOfCountries);

        windowListOfCountries.getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 440, 400));

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel20.setText("List of countries");
        windowListOfCountries.getContentPane().add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 10, -1, -1));

        tableOfDirectors.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tableOfDirectors.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tableOfDirectors.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tableOfDirectorsMouseReleased(evt);
            }
        });
        jScrollPane4.setViewportView(tableOfDirectors);

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel23.setText("List of directors");

        javax.swing.GroupLayout windowListOfDirectorsLayout = new javax.swing.GroupLayout(windowListOfDirectors.getContentPane());
        windowListOfDirectors.getContentPane().setLayout(windowListOfDirectorsLayout);
        windowListOfDirectorsLayout.setHorizontalGroup(
            windowListOfDirectorsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(windowListOfDirectorsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 777, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(windowListOfDirectorsLayout.createSequentialGroup()
                .addGap(322, 322, 322)
                .addComponent(jLabel23)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        windowListOfDirectorsLayout.setVerticalGroup(
            windowListOfDirectorsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, windowListOfDirectorsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        windowListOfClassifications.setTitle("List of countries");
        windowListOfClassifications.setResizable(false);
        windowListOfClassifications.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tableOfClassifications.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tableOfClassifications.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tableOfClassifications.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tableOfClassificationsMouseReleased(evt);
            }
        });
        jScrollPane5.setViewportView(tableOfClassifications);

        windowListOfClassifications.getContentPane().add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 430, 390));

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel24.setText("List of classifications");
        windowListOfClassifications.getContentPane().add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 10, -1, -1));

        windowListOfGenres.setTitle("List of countries");
        windowListOfGenres.setResizable(false);
        windowListOfGenres.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tableOfGenres.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tableOfGenres.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tableOfGenres.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tableOfGenresMouseReleased(evt);
            }
        });
        jScrollPane6.setViewportView(tableOfGenres);

        windowListOfGenres.getContentPane().add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, 400));

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel25.setText("List of genres");
        windowListOfGenres.getContentPane().add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 10, -1, -1));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tableOfMovies.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableOfMovies.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tableOfMoviesMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tableOfMovies);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setText("List of movies");

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnInsertMovie.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnInsertMovie.setText("Insert");
        btnInsertMovie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertMovieActionPerformed(evt);
            }
        });
        jPanel2.add(btnInsertMovie, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 230, 111, -1));

        btnUpdateMovie.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnUpdateMovie.setText("update");
        btnUpdateMovie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateMovieActionPerformed(evt);
            }
        });
        jPanel2.add(btnUpdateMovie, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 230, 109, -1));

        btnDeleteMovie.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnDeleteMovie.setText("delete");
        btnDeleteMovie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteMovieActionPerformed(evt);
            }
        });
        jPanel2.add(btnDeleteMovie, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 230, 103, -1));

        btnNext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnNext.setText("Next");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });
        jPanel2.add(btnNext, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 10, 110, -1));

        btnPrevious.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnPrevious.setText("Previous");
        btnPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviousActionPerformed(evt);
            }
        });
        jPanel2.add(btnPrevious, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 10, 110, -1));

        btnFirst.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnFirst.setText("First");
        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });
        jPanel2.add(btnFirst, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 110, -1));

        btnLast.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnLast.setText("Last");
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });
        jPanel2.add(btnLast, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 10, 110, -1));

        btnExit.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnExit.setText("Exit");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });
        jPanel2.add(btnExit, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 230, 100, -1));

        btnSearch.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnSearch.setText("Search");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });
        jPanel2.add(btnSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 190, 90, -1));

        btnReset.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnReset.setText("Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });
        jPanel2.add(btnReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 190, 110, -1));

        checkBoxClassification.setText("classification");
        jPanel2.add(checkBoxClassification, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, -1, -1));

        txtMovieNameSearch.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jPanel2.add(txtMovieNameSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 50, 470, -1));

        comboSearchClassification.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jPanel2.add(comboSearchClassification, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 100, 120, -1));

        comboSearchGenre.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jPanel2.add(comboSearchGenre, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 130, 120, -1));

        checkBoxGenre.setText("genre");
        jPanel2.add(checkBoxGenre, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, -1, -1));

        checkBoxDuration.setText("duration");
        jPanel2.add(checkBoxDuration, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 100, -1, -1));

        spinnerDurationMIN.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jPanel2.add(spinnerDurationMIN, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 100, 110, -1));

        jLabel14.setText("min");
        jPanel2.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 80, -1, -1));

        spinnerDurationMAX.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jPanel2.add(spinnerDurationMAX, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 100, 110, -1));

        jLabel15.setText("max");
        jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 80, -1, -1));
        jPanel2.add(txtMovieDateMAX, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 130, 110, -1));
        jPanel2.add(txtMovieDateMIN, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 130, 110, -1));

        checkBoxDate.setText("date");
        jPanel2.add(checkBoxDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 130, -1, -1));

        checkBoxDirector.setText("director");
        jPanel2.add(checkBoxDirector, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, -1, -1));

        comboSearchDirector.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jPanel2.add(comboSearchDirector, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 160, 120, -1));

        spinnerRatingMAX.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jPanel2.add(spinnerRatingMAX, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 160, 110, -1));

        spinnerRatingMIN.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jPanel2.add(spinnerRatingMIN, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 160, 110, -1));

        checkBoxRating.setText("rating");
        jPanel2.add(checkBoxRating, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 160, -1, 22));

        btnFiveBestMovies.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnFiveBestMovies.setText("5 best");
        btnFiveBestMovies.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFiveBestMoviesActionPerformed(evt);
            }
        });
        jPanel2.add(btnFiveBestMovies, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 190, 110, -1));

        checkBoxCountry.setText("country");
        jPanel2.add(checkBoxCountry, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, -1, -1));

        comboSearchCountry.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jPanel2.add(comboSearchCountry, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 190, 120, -1));

        jLabel21.setText("Name:");
        jPanel2.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, 20));

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("ID:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Name:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setText("Description:");

        txtID.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        txtName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        txtAreaDesc.setColumns(20);
        txtAreaDesc.setRows(5);
        jScrollPane2.setViewportView(txtAreaDesc);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("Genre:");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setText("Classification:");

        comboClassification.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        comboGenre.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setText("Date:");

        txtReleaseDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        spinnerDuration.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setText("Duration:");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setText("Director:");

        comboDirector.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        lblPoster.setBackground(new java.awt.Color(153, 204, 255));
        lblPoster.setOpaque(true);

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel16.setText("Rating:");

        spinnerRating.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        btnChooseImage.setText("Choose image ...");
        btnChooseImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChooseImageActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel6)
                        .addGap(30, 30, 30)
                        .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel7)
                        .addGap(12, 12, 12)
                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel8))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(lblPoster, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(btnChooseImage, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel16)
                        .addGap(136, 136, 136)
                        .addComponent(spinnerRating, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(jLabel11))
                        .addGap(71, 71, 71)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(spinnerDuration, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtReleaseDate, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jLabel10))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel9))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jLabel13)))
                        .addGap(43, 43, 43)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(comboDirector, 0, 172, Short.MAX_VALUE)
                            .addComponent(comboGenre, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(comboClassification, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(11, 11, 11))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))))
                .addGap(6, 6, 6)
                .addComponent(jLabel8)
                .addGap(12, 12, 12)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel10))
                    .addComponent(comboClassification, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel12)
                        .addGap(4, 4, 4))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(comboGenre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(spinnerDuration, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel11))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(txtReleaseDate, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(comboDirector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addComponent(lblPoster, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(btnChooseImage)
                .addGap(6, 6, 6)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel16))
                    .addComponent(spinnerRating, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel22.setText("Information");

        menuRegister.setText("Register");

        menuItemRegisterCountry.setText("Country");
        menuItemRegisterCountry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemRegisterCountryActionPerformed(evt);
            }
        });
        menuRegister.add(menuItemRegisterCountry);

        menuItemRegisterClssification.setText("Classification");
        menuItemRegisterClssification.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemRegisterClssificationActionPerformed(evt);
            }
        });
        menuRegister.add(menuItemRegisterClssification);

        menuItemRegisterGenre.setText("Genre");
        menuItemRegisterGenre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemRegisterGenreActionPerformed(evt);
            }
        });
        menuRegister.add(menuItemRegisterGenre);

        menuItemRegisterDirector.setText("Director");
        menuItemRegisterDirector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemRegisterDirectorActionPerformed(evt);
            }
        });
        menuRegister.add(menuItemRegisterDirector);

        jMenuBar1.add(menuRegister);

        menuList.setText("List");

        menuItemListOfCountries.setText("Countries");
        menuItemListOfCountries.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemListOfCountriesActionPerformed(evt);
            }
        });
        menuList.add(menuItemListOfCountries);

        menuItemListOfClassifications.setText("Classifications");
        menuItemListOfClassifications.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemListOfClassificationsActionPerformed(evt);
            }
        });
        menuList.add(menuItemListOfClassifications);

        menuItemListOfGenres.setText("Genres");
        menuItemListOfGenres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemListOfGenresActionPerformed(evt);
            }
        });
        menuList.add(menuItemListOfGenres);

        menuItemListOfDirectors.setText("Directors");
        menuItemListOfDirectors.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemListOfDirectorsActionPerformed(evt);
            }
        });
        menuList.add(menuItemListOfDirectors);

        jMenuBar1.add(menuList);

        menuExit.setText("Exit");

        menuItemExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        menuItemExit.setText("exit");
        menuItemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemExitActionPerformed(evt);
            }
        });
        menuExit.add(menuItemExit);

        jMenuBar1.add(menuExit);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 615, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(116, 116, 116)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addGap(256, 256, 256))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuItemRegisterCountryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemRegisterCountryActionPerformed
        formRegisterCountry.setVisible(true);
    }//GEN-LAST:event_menuItemRegisterCountryActionPerformed

    private void btnChooseImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChooseImageActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("images",
                "jpg", "jpeg", "png", "bmp");
        fileChooser.addChoosableFileFilter(filter);
        if(fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
            File selectedFile = fileChooser.getSelectedFile();
            String path = selectedFile.getAbsolutePath();
            lblPoster.setIcon(getScaledImage(path, null));
            imagePath = path;
        } else {
            System.out.println("No file selected");
        }
    }//GEN-LAST:event_btnChooseImageActionPerformed

    private void btnInsertMovieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertMovieActionPerformed
        if(checkInput()){
            Movie movie = createMovieFromUserInput();
            daoMovie.insert(movie);
            updateTableOfMovies(daoMovie.repeatLastSearch());
            resetInputForm();
        } else {
            JOptionPane.showMessageDialog(
               null,
               "One or more input fields are empty",
               "Please, fill the form",
               JOptionPane.WARNING_MESSAGE
            );
        }
    }//GEN-LAST:event_btnInsertMovieActionPerformed

    private void btnUpdateMovieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateMovieActionPerformed
        if(!txtID.getText().isEmpty() && checkInput()){
            Movie movie = createMovieFromUserInput();
            movie.setId(Integer.parseInt(txtID.getText()));
            if(JOptionPane.showConfirmDialog
                (
                    null,
                    "Do you really want to update this movie?",
                    imagePath, JOptionPane.YES_NO_OPTION
                ) == JOptionPane.YES_OPTION
            ){
                daoMovie.update(movie);
                resetInputForm();
            }
            updateTableOfMovies(daoMovie.repeatLastSearch());
        } else {
            JOptionPane.showMessageDialog(
               null,
               "One or more input fields are empty",
               "Please, fill the form",
               JOptionPane.WARNING_MESSAGE
            );
        }
    }//GEN-LAST:event_btnUpdateMovieActionPerformed

    private void btnDeleteMovieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteMovieActionPerformed
        if(!txtID.getText().isEmpty()){
            int id = Integer.parseInt(txtID.getText());
            if(JOptionPane.showConfirmDialog
                (
                    null,
                    "Do you really want to delete this movie?",
                    imagePath, JOptionPane.YES_NO_OPTION
                ) == JOptionPane.YES_OPTION
            ){
                daoMovie.delete(id);
                resetInputForm();
            }
            updateTableOfMovies(daoMovie.repeatLastSearch());
        } else {
            JOptionPane.showMessageDialog(
               null,
               "ID field is empty",
               "Please, put the movie ID in",
               JOptionPane.WARNING_MESSAGE
            );
        }
    }//GEN-LAST:event_btnDeleteMovieActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        int rowCount = tableOfMovies.getRowCount();
        if(rowCount > 0){
            int row = tableOfMovies.getSelectedRow();
            ++row;
            if(row >= rowCount){
                row = 0;
            }
            tableOfMovies.setRowSelectionInterval(row, row);
            showSelectedMovie();
        }
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviousActionPerformed
        int rowCount = tableOfMovies.getRowCount();
        if(rowCount > 0){
            int row = tableOfMovies.getSelectedRow();
            --row;
            if(row < 0){
                row = rowCount - 1;
            }
            tableOfMovies.setRowSelectionInterval(row, row);
            showSelectedMovie();
        }
    }//GEN-LAST:event_btnPreviousActionPerformed

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
        int rowCount = tableOfMovies.getRowCount();
        if(rowCount > 0){
            tableOfMovies.setRowSelectionInterval(0, 0);
            showSelectedMovie();
        }
    }//GEN-LAST:event_btnFirstActionPerformed

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
        int rowCount = tableOfMovies.getRowCount();
        if(rowCount > 0){
            tableOfMovies.setRowSelectionInterval(rowCount - 1, rowCount - 1);
            showSelectedMovie();
        }
    }//GEN-LAST:event_btnLastActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        int res = JOptionPane.showConfirmDialog(
            null,
            "Do you really want to quit?",
            "Confirm application quit please",
            JOptionPane.YES_NO_OPTION
        );
        if(res == JOptionPane.YES_OPTION){
            System.exit(0);
        }
    }//GEN-LAST:event_btnExitActionPerformed

    private void tableOfMoviesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableOfMoviesMouseReleased
        if(evt.getClickCount() == 2){
            showSelectedMovie();
        }
    }//GEN-LAST:event_tableOfMoviesMouseReleased

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        String regExp = txtMovieNameSearch.getText();
        boolean considerClassification = checkBoxClassification.isSelected();
        Classification classification = 
                (Classification)comboSearchClassification.getSelectedItem();        
        int id_classification = classification.getId();
        boolean considerGenre = checkBoxGenre.isSelected();
        Genre genre = (Genre)comboSearchGenre.getSelectedItem();
        int id_genre = genre.getId();
        boolean considerDuration = checkBoxDuration.isSelected();
        int dur_min = (int)spinnerDurationMIN.getValue();
        int dur_max = (int)spinnerDurationMAX.getValue();
        if(dur_max < dur_min){
            dur_max = dur_min;
            spinnerDurationMAX.setValue(dur_max);
        }
        boolean considerDate = checkBoxDate.isSelected();
        long mls1 = txtMovieDateMIN.getDate().getTime();
        long mls2 = txtMovieDateMAX.getDate().getTime();
        if(mls2 < mls1){
            mls2 = mls1;
            txtMovieDateMAX.setDate(txtMovieDateMIN.getDate());
        }        
        java.sql.Date date_min = new java.sql.Date(mls1);
        java.sql.Date date_max = new java.sql.Date(mls2);
        boolean considerDirector = checkBoxDirector.isSelected();
        Director dir = (Director)comboSearchDirector.getSelectedItem();
        int id_director = dir.getId();
        boolean considerCountry = checkBoxCountry.isSelected();
        Country c = (Country)comboSearchCountry.getSelectedItem();
        int id_country = c.getId();
        boolean considerRating = checkBoxRating.isSelected();
        int rating_min = (int)spinnerRatingMIN.getValue();
        int rating_max = (int)spinnerRatingMAX.getValue();
        if(rating_max < rating_min){
            rating_max = rating_min;
            spinnerRatingMAX.setValue(rating_max);
        }
        List<Movie> list = daoMovie.search(
                regExp,
                considerClassification,
                id_classification,
                considerGenre,
                id_genre,
                considerDuration,
                dur_min,
                dur_max,
                considerDate,
                date_min,
                date_max,
                considerDirector,
                id_director,
                considerCountry,
                id_country,
                considerRating,
                rating_min,
                rating_max
        );
        updateTableOfMovies(list);
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        resetSearchForm();
        resetInputForm();
        updateTableOfMovies(daoMovie.readAll());
        tableOfMovies.getSelectionModel().clearSelection();
    }//GEN-LAST:event_btnResetActionPerformed

    private void resetSearchForm(){
        txtMovieNameSearch.setText(null);
        checkBoxClassification.setSelected(false);
        if(comboSearchClassification.getItemCount() > 0){
            comboSearchClassification.setSelectedIndex(0);
        }
        checkBoxGenre.setSelected(false);
        if(comboSearchGenre.getItemCount() > 0){
            comboSearchGenre.setSelectedIndex(0);
        }
        checkBoxDuration.setSelected(false);
        spinnerDurationMIN.setValue(0);
        spinnerDurationMAX.setValue(0);
        checkBoxDate.setSelected(false);
        Calendar cal = Calendar.getInstance();
        txtMovieDateMIN.setDate(cal.getTime());
        txtMovieDateMAX.setDate(cal.getTime());
        checkBoxDirector.setSelected(false);
        if(comboSearchDirector.getItemCount() > 0){
            comboSearchDirector.setSelectedIndex(0);
        }
        checkBoxCountry.setSelected(false);
        if(comboSearchCountry.getItemCount() > 0){
            comboSearchCountry.setSelectedIndex(0);
        }
        checkBoxRating.setSelected(false);
        spinnerRatingMIN.setValue(0);
        spinnerRatingMAX.setValue(0);
    }
    
    private void btnFiveBestMoviesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiveBestMoviesActionPerformed
        updateTableOfMovies(daoMovie.getFiveBestMovies());
        resetInputForm();
    }//GEN-LAST:event_btnFiveBestMoviesActionPerformed

    private void tableOfDirectorsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableOfDirectorsMouseReleased
        if(evt.getClickCount() == 1){
            int row = evt.getY() / tableOfDirectors.getRowHeight();
            int col = tableOfDirectors.getColumnModel().getColumnIndexAtX(evt.getX());
            TModelDirectors model = (TModelDirectors)tableOfDirectors.getModel();
            Director d = model.getDirector(row);
            if(row >= 0 && row < tableOfDirectors.getRowCount() &&
               col >= 0 && col < tableOfDirectors.getColumnCount()){
                Object val = tableOfDirectors.getValueAt(row, col);
                if(val instanceof JButton){
                    JButton clickedButton = (JButton)val;
                    if(clickedButton.getName().equalsIgnoreCase("edit")){
                        int res = JOptionPane.showConfirmDialog(
                            null,
                            "Do you really want to update selected director"
                                 + " information?",
                            "Confirm update, please",
                            JOptionPane.YES_NO_OPTION
                        );
                        if(res == JOptionPane.YES_OPTION){
                            String newName = (String)model.getValueAt(row,
                                TModelDirectors.COLUMN_NAME);
                            Gender newGender = (Gender)model.getValueAt(row,
                                TModelDirectors.COLUMN_GENDER);
                            java.util.Date dob = (java.util.Date)model.getValueAt(
                                row, TModelDirectors.COLUMN_DATE);
                            java.sql.Date newDob = 
                                    new java.sql.Date(dob.getTime());
                            Country newCountry = (Country)model.getValueAt(row,
                                    TModelDirectors.COLUMN_COUNTRY);
                            Director newDirector = new Director(newName, newGender, newDob,
                            newCountry.getId());
                            newDirector.setId(d.getId());
                            daoDirector.update(newDirector);
                            reloadComboDirectors();
                            updateTableOfDirectors();
                        }
                    } else if(clickedButton.getName().equalsIgnoreCase("delete")){
                        int res = JOptionPane.showConfirmDialog(
                            null,
                            "Do you really want to delete selected director"
                             + " from database?",
                            "Confirm deleting, please",
                            JOptionPane.YES_NO_OPTION
                        );
                        if(res == JOptionPane.YES_OPTION){
                            daoDirector.delete(d.getId());
                            reloadComboDirectors();
                            updateTableOfDirectors();
                        }
                    }
                }
            }
        }
    }//GEN-LAST:event_tableOfDirectorsMouseReleased

    private void menuItemListOfDirectorsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemListOfDirectorsActionPerformed
        windowListOfDirectors.setVisible(true);
    }//GEN-LAST:event_menuItemListOfDirectorsActionPerformed

    private void menuItemRegisterDirectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemRegisterDirectorActionPerformed
        formRegisterDirector.setVisible(true);
    }//GEN-LAST:event_menuItemRegisterDirectorActionPerformed

    private void btnRegisterDirectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterDirectorActionPerformed
        if(!txtDirectorNameReg.getText().isEmpty()){
            String name = txtDirectorNameReg.getText();
            Gender gender = rbtnMale.isSelected() ? Gender.Male : Gender.Female;
            java.sql.Date dob = new java.sql.Date(txtDirectorDOB.getDate().getTime());
            Country c = (Country)comboCountryOfDirector.getSelectedItem();
            int id_country = c.getId();
            Director dr = new Director(name, gender, dob, id_country);
            daoDirector.insert(dr);
            updateTableOfDirectors();
            reloadComboDirectors();
            resetDirectorRegisterForm();
        } else {
            JOptionPane.showMessageDialog(
                null,
                "Please, type the name of director",
                "Please, type the name of director",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }//GEN-LAST:event_btnRegisterDirectorActionPerformed
    
    private void resetDirectorRegisterForm(){
        txtDirectorNameReg.setText(null);
        rbtnMale.setSelected(true);
        Calendar cal = Calendar.getInstance();
        txtDirectorDOB.setDate(cal.getTime());
        if(comboCountryOfDirector.getItemCount() > 0) {
            comboCountryOfDirector.setSelectedIndex(0);
        }
    }
    
    private void btnCloseDirectorRegisterWindowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseDirectorRegisterWindowActionPerformed
        formRegisterDirector.setVisible(false);
    }//GEN-LAST:event_btnCloseDirectorRegisterWindowActionPerformed

    private void menuItemListOfCountriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemListOfCountriesActionPerformed
        windowListOfCountries.setVisible(true);
    }//GEN-LAST:event_menuItemListOfCountriesActionPerformed

    private void menuItemRegisterClssificationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemRegisterClssificationActionPerformed
        formRegisterClassification.setVisible(true);
    }//GEN-LAST:event_menuItemRegisterClssificationActionPerformed

    private void menuItemRegisterGenreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemRegisterGenreActionPerformed
        formRegisterGenre.setVisible(true);
    }//GEN-LAST:event_menuItemRegisterGenreActionPerformed

    private void menuItemListOfClassificationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemListOfClassificationsActionPerformed
        windowListOfClassifications.setVisible(true);
    }//GEN-LAST:event_menuItemListOfClassificationsActionPerformed

    private void menuItemListOfGenresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemListOfGenresActionPerformed
        windowListOfGenres.setVisible(true);
    }//GEN-LAST:event_menuItemListOfGenresActionPerformed

    private void menuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemExitActionPerformed
        btnExit.doClick();
    }//GEN-LAST:event_menuItemExitActionPerformed

    private void btnRegisterCountryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterCountryActionPerformed
        if(!txtCountryNameRegister.getText().isEmpty()){
            Country country = new Country(txtCountryNameRegister.getText());
            daoCountry.insert(country);
            reloadComboCountries();
            updateTableOfCountries();
            txtCountryNameRegister.setText(null);
        } else {
            JOptionPane.showMessageDialog(
                null,
                "Please, type the name of country",
                "Please, type the name of country",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }//GEN-LAST:event_btnRegisterCountryActionPerformed

    private void btnCloseCountryRegisterFormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseCountryRegisterFormActionPerformed
        formRegisterCountry.setVisible(false);
    }//GEN-LAST:event_btnCloseCountryRegisterFormActionPerformed

    private void btnRegisterClassificationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterClassificationActionPerformed
        if(!txtClassificationNameRegister.getText().isEmpty()){
            Classification cl = new Classification(
                txtClassificationNameRegister.getText()
            );
            daoClassification.insert(cl);
            reloadComboClassifications();
            updateTableOfClassifications();
            txtClassificationNameRegister.setText(null);
        } else {
            JOptionPane.showMessageDialog(
                null,
                "Please, type the name of classification",
                "Please, type the name of classification",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }//GEN-LAST:event_btnRegisterClassificationActionPerformed

    private void btnCloseClassificationRegisterFormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseClassificationRegisterFormActionPerformed
       formRegisterClassification.setVisible(false);
    }//GEN-LAST:event_btnCloseClassificationRegisterFormActionPerformed

    private void btnRegisterGenreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterGenreActionPerformed
        if(!txtGenreNameRegister.getText().isEmpty()){
            Genre genre = new Genre(txtGenreNameRegister.getText());
            daoGenre.insert(genre);
            reloadComboGenres();
            updateTableOfGenres();
            txtGenreNameRegister.setText(null);
        } else {
            JOptionPane.showMessageDialog(
                null,
                "Please, type the name of genre",
                "Please, type the name of genre",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }//GEN-LAST:event_btnRegisterGenreActionPerformed

    private void btnCloseGenreRegisterFormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseGenreRegisterFormActionPerformed
        formRegisterGenre.setVisible(false);
    }//GEN-LAST:event_btnCloseGenreRegisterFormActionPerformed

    private void tableOfCountriesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableOfCountriesMouseReleased
        if(evt.getClickCount() == 1){
            int row = evt.getY() / tableOfCountries.getRowHeight();
            int col = tableOfCountries.getColumnModel().getColumnIndexAtX(evt.getX());
            TModelSimple model = (TModelSimple)tableOfCountries.getModel();
            if(row >= 0 && row < tableOfCountries.getRowCount() &&
               col >= 0 && col < tableOfCountries.getColumnCount()){
                Object val = tableOfCountries.getValueAt(row, col);
                if(val instanceof JButton){
                    JButton btn = (JButton)val;
                    if(btn.getName().equalsIgnoreCase("edit")){
                        int res = JOptionPane.showConfirmDialog(
                            null,
                            "Do you really want to change selected country?",
                            "Confirm changes, please",
                            JOptionPane.YES_NO_OPTION
                        );
                        if(res == JOptionPane.YES_OPTION){
                            int id_country = (int)tableOfCountries.getValueAt(row, 
                                    TModelSimple.COLUMN_ID);
                            String name = (String)tableOfCountries.getValueAt(row,
                                    TModelSimple.COLUMN_NAME);
                            Country c = new Country(id_country, name);
                            daoCountry.update(c);
                            updateTableOfCountries();
                            reloadComboCountries();
                        }
                    } else if(btn.getName().equalsIgnoreCase("delete")){
                        int res = JOptionPane.showConfirmDialog(
                            null,
                            "Do you really want to delete selected country?",
                            "Confirm deleting, please",
                            JOptionPane.YES_NO_OPTION
                        );
                        if(res == JOptionPane.YES_OPTION){
                            int id_country = (int)tableOfCountries.getValueAt(row, 
                                    TModelSimple.COLUMN_ID);
                            daoCountry.delete(id_country);
                            updateTableOfCountries();
                            reloadComboCountries();
                        }
                    }
                }
            }
        }
    }//GEN-LAST:event_tableOfCountriesMouseReleased

    private void tableOfClassificationsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableOfClassificationsMouseReleased
        if(evt.getClickCount() == 1){
            int row = evt.getY() / tableOfClassifications.getRowHeight();
            int col = tableOfClassifications.getColumnModel()
                    .getColumnIndexAtX(evt.getX());
            TModelSimple model = (TModelSimple)tableOfClassifications.getModel();
            if(row >= 0 && row < tableOfClassifications.getRowCount() &&
               col >= 0 && col < tableOfClassifications.getColumnCount()){
                Object val = tableOfClassifications.getValueAt(row, col);
                if(val instanceof JButton){
                    JButton btn = (JButton)val;
                    if(btn.getName().equalsIgnoreCase("edit")){
                        int res = JOptionPane.showConfirmDialog(
                            null,
                            "Do you really want to change selected classification?",
                            "Confirm changes, please",
                            JOptionPane.YES_NO_OPTION
                        );
                        if(res == JOptionPane.YES_OPTION){
                            int id_classification =
                                (int)tableOfClassifications
                                 .getValueAt(row, TModelSimple.COLUMN_ID);
                            String name = (String)tableOfClassifications
                                    .getValueAt(row, TModelSimple.COLUMN_NAME);
                            Classification c = new Classification(
                                    id_classification, name);
                            daoClassification.update(c);
                            updateTableOfClassifications();
                            reloadComboClassifications();
                        }
                    } else if(btn.getName().equalsIgnoreCase("delete")){
                        int res = JOptionPane.showConfirmDialog(
                            null,
                            "Do you really want to delete selected country?",
                            "Confirm deleting, please",
                            JOptionPane.YES_NO_OPTION
                        );
                        if(res == JOptionPane.YES_OPTION){
                            int id_classification = (int)tableOfClassifications
                                    .getValueAt(row, TModelSimple.COLUMN_ID);
                            daoClassification.delete(id_classification);
                            updateTableOfClassifications();
                            reloadComboClassifications();
                        }
                    }
                }
            }
        }
    }//GEN-LAST:event_tableOfClassificationsMouseReleased

    private void tableOfGenresMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableOfGenresMouseReleased
        if(evt.getClickCount() == 1){
            int row = evt.getY() / tableOfGenres.getRowHeight();
            int col = tableOfGenres.getColumnModel().getColumnIndexAtX(evt.getX());
            TModelSimple model = (TModelSimple)tableOfGenres.getModel();
            if(row >= 0 && row < tableOfGenres.getRowCount() &&
               col >= 0 && col < tableOfGenres.getColumnCount()){
                Object val = tableOfGenres.getValueAt(row, col);
                if(val instanceof JButton){
                    JButton btn = (JButton)val;
                    if(btn.getName().equalsIgnoreCase("edit")){
                        int res = JOptionPane.showConfirmDialog(
                            null,
                            "Do you really want to change selected genre?",
                            "Confirm changes, please",
                            JOptionPane.YES_NO_OPTION
                        );
                        if(res == JOptionPane.YES_OPTION){
                            int id_genre = (int)tableOfGenres.getValueAt(row, 
                                    TModelSimple.COLUMN_ID);
                            String name = (String)tableOfGenres.getValueAt(row,
                                    TModelSimple.COLUMN_NAME);
                            Genre g = new Genre(id_genre, name);
                            daoGenre.update(g);
                            updateTableOfGenres();
                            reloadComboGenres();
                        }
                    } else if(btn.getName().equalsIgnoreCase("delete")){
                        int res = JOptionPane.showConfirmDialog(
                            null,
                            "Do you really want to delete selected genre?",
                            "Confirm deleting, please",
                            JOptionPane.YES_NO_OPTION
                        );
                        if(res == JOptionPane.YES_OPTION){
                            int id_genre = (int)tableOfGenres.getValueAt(row, 
                                    TModelSimple.COLUMN_ID);
                            daoGenre.delete(id_genre);
                            updateTableOfGenres();
                            reloadComboGenres();
                        }
                    }
                }
            }
        }
    }//GEN-LAST:event_tableOfGenresMouseReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChooseImage;
    private javax.swing.JToggleButton btnCloseClassificationRegisterForm;
    private javax.swing.JToggleButton btnCloseCountryRegisterForm;
    private javax.swing.JToggleButton btnCloseDirectorRegisterWindow;
    private javax.swing.JToggleButton btnCloseGenreRegisterForm;
    private javax.swing.JButton btnDeleteMovie;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnFirst;
    private javax.swing.JButton btnFiveBestMovies;
    private javax.swing.JButton btnInsertMovie;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrevious;
    private javax.swing.JToggleButton btnRegisterClassification;
    private javax.swing.JToggleButton btnRegisterCountry;
    private javax.swing.JToggleButton btnRegisterDirector;
    private javax.swing.JToggleButton btnRegisterGenre;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnUpdateMovie;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JCheckBox checkBoxClassification;
    private javax.swing.JCheckBox checkBoxCountry;
    private javax.swing.JCheckBox checkBoxDate;
    private javax.swing.JCheckBox checkBoxDirector;
    private javax.swing.JCheckBox checkBoxDuration;
    private javax.swing.JCheckBox checkBoxGenre;
    private javax.swing.JCheckBox checkBoxRating;
    private javax.swing.JComboBox comboClassification;
    private javax.swing.JComboBox comboCountryOfDirector;
    private javax.swing.JComboBox comboDirector;
    private javax.swing.JComboBox comboGenre;
    private javax.swing.JComboBox comboSearchClassification;
    private javax.swing.JComboBox comboSearchCountry;
    private javax.swing.JComboBox comboSearchDirector;
    private javax.swing.JComboBox comboSearchGenre;
    private javax.swing.JFrame formRegisterClassification;
    private javax.swing.JFrame formRegisterCountry;
    private javax.swing.JFrame formRegisterDirector;
    private javax.swing.JFrame formRegisterGenre;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JLabel lblPoster;
    private javax.swing.JMenu menuExit;
    private javax.swing.JMenuItem menuItemExit;
    private javax.swing.JMenuItem menuItemListOfClassifications;
    private javax.swing.JMenuItem menuItemListOfCountries;
    private javax.swing.JMenuItem menuItemListOfDirectors;
    private javax.swing.JMenuItem menuItemListOfGenres;
    private javax.swing.JMenuItem menuItemRegisterClssification;
    private javax.swing.JMenuItem menuItemRegisterCountry;
    private javax.swing.JMenuItem menuItemRegisterDirector;
    private javax.swing.JMenuItem menuItemRegisterGenre;
    private javax.swing.JMenu menuList;
    private javax.swing.JMenu menuRegister;
    private javax.swing.JRadioButton rbtnFemale;
    private javax.swing.JRadioButton rbtnMale;
    private javax.swing.JSpinner spinnerDuration;
    private javax.swing.JSpinner spinnerDurationMAX;
    private javax.swing.JSpinner spinnerDurationMIN;
    private javax.swing.JSpinner spinnerRating;
    private javax.swing.JSpinner spinnerRatingMAX;
    private javax.swing.JSpinner spinnerRatingMIN;
    private javax.swing.JTable tableOfClassifications;
    private javax.swing.JTable tableOfCountries;
    private javax.swing.JTable tableOfDirectors;
    private javax.swing.JTable tableOfGenres;
    private javax.swing.JTable tableOfMovies;
    private javax.swing.JTextArea txtAreaDesc;
    private javax.swing.JTextField txtClassificationNameRegister;
    private javax.swing.JTextField txtCountryNameRegister;
    private com.toedter.calendar.JDateChooser txtDirectorDOB;
    private javax.swing.JTextField txtDirectorNameReg;
    private javax.swing.JTextField txtGenreNameRegister;
    private javax.swing.JTextField txtID;
    private com.toedter.calendar.JDateChooser txtMovieDateMAX;
    private com.toedter.calendar.JDateChooser txtMovieDateMIN;
    private javax.swing.JTextField txtMovieNameSearch;
    private javax.swing.JTextField txtName;
    private com.toedter.calendar.JDateChooser txtReleaseDate;
    private javax.swing.JFrame windowListOfClassifications;
    private javax.swing.JFrame windowListOfCountries;
    private javax.swing.JFrame windowListOfDirectors;
    private javax.swing.JFrame windowListOfGenres;
    // End of variables declaration//GEN-END:variables
}
