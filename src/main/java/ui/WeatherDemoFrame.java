package ui;

import GeolocationsAPIs.GeocodingService;
import interfaceadapter.notes.AddNoteToStopController;
import interfaceadapter.notes.NotesViewModel;
import interfaceadapter.view_weather_adapt.ViewWeatherController;
import interfaceadapter.view_weather_adapt.WeatherViewModel;
import interfaceadapter.IteneraryViewModel;
import interfaceadapter.add_multiple_stops.AddStopController;
import interfaceadapter.set_start_date.SetStartDateController;
import data_access.RouteDataAccess;
import entity.Itinerary;
import entity.ItineraryStop;
import entity.RouteInfo;
import usecase.ItineraryRepository;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Refactored UI for a modern look
 */
public class WeatherDemoFrame extends JFrame implements PropertyChangeListener {

    // --- Dependencies ---
    private final GeocodingService geocodingService;
    private final ViewWeatherController weatherController;
    private final WeatherViewModel weatherViewModel;
    private final IteneraryViewModel itineraryViewModel;
    private final AddStopController addStopController;
    private final AddNoteToStopController addNoteController;
    private final NotesViewModel notesViewModel;
    private final ItineraryRepository itineraryRepository;
    private final String itineraryId;
    private final RouteDataAccess routeDataAccess = new RouteDataAccess();
    private final SetStartDateController setStartDateController;

    // --- UI Components ---
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);

    // Styling Constants
    private final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 24);
    private final Font HEADER_FONT = new Font("SansSerif", Font.BOLD, 14);
    private final Font NORMAL_FONT = new Font("SansSerif", Font.PLAIN, 12);
    private final Color PRIMARY_COLOR = new Color(70, 130, 180); // Steel Blue
    private final Color BG_COLOR = new Color(245, 245, 250); // Light Gray-Blue

    // Login
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel loginErrorLabel;

    // Main Page Inputs
    private JLabel welcomeLabel;
    private JTextField originField;
    private JTextField destinationField;
    private JTextField startDateField;
    private JTextField stopField;

    // Main Page Outputs
    private JTextArea currentWeatherArea;
    private JTextArea tipsArea;
    private JTextArea forecastArea;
    private JTextArea noteArea;
    private JLabel errorLabel;
    private final java.util.Map<String, String> cityWeatherMap = new java.util.LinkedHashMap<>();

    // Lists & Info
    private JLabel travelDistanceValueLabel;
    private JLabel travelTimeValueLabel;
    private DefaultListModel<String> stopListModel;
    private JList<String> stopList;

    // History
    private final DefaultListModel<String> historyModel = new DefaultListModel<>();
    private JList<String> historyList;

    private String currentUser = "";
    private String mainDestination = null;

    public WeatherDemoFrame(GeocodingService geocoding,
                            ViewWeatherController weatherControl,
                            WeatherViewModel weatherView,
                            IteneraryViewModel itineraryView,
                            AddStopController addStopControl,
                            AddNoteToStopController addNoteControl,
                            NotesViewModel notesView,
                            ItineraryRepository itineraryRepo,
                            String itId,
                            SetStartDateController setStartDateControl) {

        super("TravelPath");
        // Dependencies assignment
        geocodingService = geocoding;
        weatherController = weatherControl;
        weatherViewModel = weatherView;
        itineraryViewModel = itineraryView;
        addStopController = addStopControl;
        addNoteController = addNoteControl;
        notesViewModel = notesView;
        itineraryRepository = itineraryRepo;
        itineraryId = itId;
        this.setStartDateController = setStartDateControl;

        // Listeners
        this.weatherViewModel.addPropertyChangeListener(this);
        this.itineraryViewModel.addPropertyChangeListener(this);

        // Frame Setup
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700); // Slightly larger for better spacing
        setLocationRelativeTo(null);

        buildUi();
        setContentPane(cards);
    }

    private void buildUi() {
        cards.add(buildLoginPanel(), "login");
        cards.add(buildMainPanel(), "main");
        cards.add(buildHistoryPanel(), "history");
        cardLayout.show(cards, "login");
    }

    // --- LOGIN PANEL --- UI
    private JPanel buildLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG_COLOR);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(40, 60, 40, 60)
        ));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel title = new JLabel("TravelPath Login");
        title.setFont(TITLE_FONT);
        title.setForeground(PRIMARY_COLOR);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        card.add(title, c);

        // Inputs
        c.gridwidth = 1; c.gridy = 1;
        card.add(new JLabel("Username:"), c);
        usernameField = new JTextField(15);
        c.gridx = 1;
        card.add(usernameField, c);

        c.gridx = 0; c.gridy = 2;
        card.add(new JLabel("Password:"), c);
        passwordField = new JPasswordField(15);
        c.gridx = 1;
        card.add(passwordField, c);

        // Button
        JButton loginButton = createStyledButton("Log In");
        loginButton.addActionListener(e -> onLogin());
        c.gridx = 0; c.gridy = 3; c.gridwidth = 2; c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        card.add(loginButton, c);

        // Error
        loginErrorLabel = new JLabel(" ");
        loginErrorLabel.setForeground(Color.RED);
        loginErrorLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        c.gridy = 4;
        card.add(loginErrorLabel, c);

        panel.add(card);
        return panel;
    }

    // --- MAIN PANEL ---
    private JPanel buildMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // 1. Header (Welcome + History Button)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BG_COLOR);
        welcomeLabel = new JLabel("Welcome!", SwingConstants.LEFT);
        welcomeLabel.setFont(TITLE_FONT);

        JButton historyButton = createStyledButton("View History");
        historyButton.addActionListener(e -> cardLayout.show(cards, "history"));

        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(historyButton, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // 2. Center Content (Split into Left Controls and Right Results)
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0)); // 2 Columns
        contentPanel.setBackground(BG_COLOR);

        // --- LEFT COLUMN: INPUTS ---
        JPanel leftColumn = new JPanel();
        leftColumn.setLayout(new BoxLayout(leftColumn, BoxLayout.Y_AXIS));
        leftColumn.setBackground(BG_COLOR);

        // A. Trip Details Panel
        JPanel tripPanel = createSectionPanel("Trip Details");
        tripPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Row 1: Origin
        gbc.gridx = 0; gbc.gridy = 0;
        tripPanel.add(new JLabel("Origin:"), gbc);
        originField = new JTextField();
        gbc.gridx = 1;
        tripPanel.add(originField, gbc);

        // Row 2: Destination
        gbc.gridx = 0; gbc.gridy = 1;
        tripPanel.add(new JLabel("Destination:"), gbc);
        destinationField = new JTextField();
        gbc.gridx = 1;
        tripPanel.add(destinationField, gbc);

        // Row 3: Start Date
        gbc.gridx = 0; gbc.gridy = 2;
        tripPanel.add(new JLabel("Start Date (YYYY-MM-DD):"), gbc);
        startDateField = new JTextField();
        gbc.gridx = 1;
        tripPanel.add(startDateField, gbc);

        // Row 4: Get Weather Button
        JButton getWeatherButton = createStyledButton("Get Forecast & Route");
        getWeatherButton.addActionListener(e -> onGetWeather());
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER;
        tripPanel.add(getWeatherButton, gbc);

        // B. Stops Panel
        JPanel stopsPanel = createSectionPanel("Manage Stops");
        stopsPanel.setLayout(new BorderLayout(5, 5));

        JPanel addStopSubPanel = new JPanel(new BorderLayout(5, 0));
        addStopSubPanel.setOpaque(false);
        stopField = new JTextField();
        JButton addStopButton = new JButton("+");
        addStopButton.addActionListener(e -> onAddStop());
        addStopSubPanel.add(stopField, BorderLayout.CENTER);
        addStopSubPanel.add(addStopButton, BorderLayout.EAST);
        addStopSubPanel.add(new JLabel("Add City: "), BorderLayout.WEST);

        stopListModel = new DefaultListModel<>();
        stopList = new JList<>(stopListModel);
        stopList.setVisibleRowCount(6);
        stopList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) onStopSelected();
        });

        stopsPanel.add(addStopSubPanel, BorderLayout.NORTH);
        stopsPanel.add(new JScrollPane(stopList), BorderLayout.CENTER);

        JButton removeStopButton = new JButton("Remove Selected Stop");
        removeStopButton.addActionListener(e -> onRemoveSelected());
        stopsPanel.add(removeStopButton, BorderLayout.SOUTH);

        // C. Notes Panel
        JPanel notePanel = createSectionPanel("Notes");
        notePanel.setLayout(new BorderLayout(5, 5));
        noteArea = new JTextArea(4, 20);
        noteArea.setLineWrap(true);
        notePanel.add(new JScrollPane(noteArea), BorderLayout.CENTER);
        JButton saveNoteButton = new JButton("Save Note to Selected Stop");
        saveNoteButton.addActionListener(e -> onSaveNote());
        notePanel.add(saveNoteButton, BorderLayout.SOUTH);

        // Add sections to left column
        leftColumn.add(tripPanel);
        leftColumn.add(Box.createVerticalStrut(10));
        leftColumn.add(stopsPanel);
        leftColumn.add(Box.createVerticalStrut(10));
        leftColumn.add(notePanel);

        // --- RIGHT COLUMN: RESULTS ---
        JPanel rightColumn = new JPanel();
        rightColumn.setLayout(new BoxLayout(rightColumn, BoxLayout.Y_AXIS));
        rightColumn.setBackground(BG_COLOR);

        // D. Weather Display
        JPanel weatherPanel = createSectionPanel("Weather & Forecast");
        weatherPanel.setLayout(new GridLayout(3, 1, 5, 5));

        currentWeatherArea = createInfoArea();
        tipsArea = createInfoArea();
        forecastArea = createInfoArea();

        weatherPanel.add(wrapWithLabel("Current Weather:", currentWeatherArea));
        weatherPanel.add(wrapWithLabel("Clothing Tips:", tipsArea));
        weatherPanel.add(wrapWithLabel("7-Day Forecast:", forecastArea));

        // E. Travel Info Panel
        JPanel infoPanel = createSectionPanel("Travel Summary");
        infoPanel.setLayout(new GridLayout(2, 2, 10, 10));

        infoPanel.add(new JLabel("Total Distance:"));
        travelDistanceValueLabel = new JLabel("0.0 km");
        travelDistanceValueLabel.setFont(HEADER_FONT);
        infoPanel.add(travelDistanceValueLabel);

        infoPanel.add(new JLabel("Total Time:"));
        travelTimeValueLabel = new JLabel("0 min");
        travelTimeValueLabel.setFont(HEADER_FONT);
        infoPanel.add(travelTimeValueLabel);

        // Add sections to right column
        rightColumn.add(weatherPanel);
        rightColumn.add(Box.createVerticalStrut(10));
        rightColumn.add(infoPanel);

        // Add columns to content panel
        contentPanel.add(leftColumn);
        contentPanel.add(rightColumn);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Error Message Bar
        errorLabel = new JLabel(" ");
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(errorLabel, BorderLayout.SOUTH);

        return mainPanel;
    }
