import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;

public class PhonebookApp {
    private Frame frame;
    private Panel mainPanel;
    private TextField nameTextField;
    private TextField numberTextField;
    private TextField editNameTextField;
    private TextField searchTextField;
    private TextField newEditNameTextField;
    private TextField newNumberTextField;
    private Button startButton;
    private boolean startButtonClicked = false;
    private ArrayList<Contact> contacts = new ArrayList<>();

    public PhonebookApp() {
        frame = new Frame("Phonebook App");
        mainPanel = new Panel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        frame.setSize(400, 200);
        frame.setLayout(new FlowLayout());
        frame.setVisible(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - frame.getWidth()) / 2;
        int y = (screenSize.height - frame.getHeight()) / 2;
        frame.setLocation(x, y);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
        startButton = new Button("Start");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!startButtonClicked) {
                    showWelcomeScreen();
                    startButtonClicked = true;
                }
            }
        });
        Label phonebookLabel = new Label("Hi :) Phonebook");
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(phonebookLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(startButton, gbc);
        frame.add(mainPanel);
    }

    private void showWelcomeScreen() {
        mainPanel.removeAll();
        Panel welcomePanel = new Panel(new GridLayout(7, 1));
        Label welcomeLabel = new Label("Welcome");
        welcomePanel.add(welcomeLabel);
        String[] buttonLabels = {"Add Contact", "Delete Contact", "Edit Contact",
                "Display Contact", "Search Contact", "Thank you"};
        for (String label : buttonLabels) {
            Button button = new Button(label);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    handleButtonClick(label);
                }
            });
            welcomePanel.add(button);
        }
        mainPanel.add(welcomePanel);
        frame.revalidate();
        frame.repaint();
    }


private void handleButtonClick(String buttonLabel) {
    mainPanel.removeAll();
    if (buttonLabel.equals("Add Contact")) {
        showNameAndNumberFields();
    } else if (buttonLabel.equals("Delete Contact")) {
        showDeleteContactField();
    } else if (buttonLabel.equals("Edit Contact")) {
        showEditContactField();
    } else if (buttonLabel.equals("Display Contact")) {
        displayContacts();
    } else if (buttonLabel.equals("Search Contact")) {
        showSearchContactField();
    } else if (buttonLabel.equals("Thank you")) {
        System.out.println("Thank you action");
        frame.dispose();
        return;
    } else if (buttonLabel.equals("Phonebook Menu")) {
        showWelcomeScreen();
    }
    frame.revalidate();
    frame.repaint();
}

private void showNameAndNumberFields() {
        mainPanel.removeAll();
        Panel fieldsPanel = new Panel(new FlowLayout());
        nameTextField = new TextField();
        nameTextField.setPreferredSize(new Dimension(200, 30));
        numberTextField = new TextField();
        numberTextField.setPreferredSize(new Dimension(200, 30));
        Label nameLabel = new Label("Enter the name:");
        Label numberLabel = new Label("Enter the number:");
        Button submitButton = new Button("Submit");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String enteredName = nameTextField.getText();
                String enteredNumber = numberTextField.getText();
                System.out.println("Entered Name: " + enteredName);
                System.out.println("Entered Number: " + enteredNumber);
                saveContactToFile(enteredName, enteredNumber);
                showPhonebookMenuButton();
            }
        });
        fieldsPanel.add(nameLabel);
        fieldsPanel.add(nameTextField);
        fieldsPanel.add(numberLabel);
        fieldsPanel.add(numberTextField);
        fieldsPanel.add(submitButton);
        mainPanel.add(fieldsPanel);
        frame.revalidate();
        frame.repaint();
    }

    private void displayContacts() {
        System.out.println("Display Contact action");
        readContactsFromFile();
        Dialog contactDialog = new Dialog(frame, "Contact Details", true);
        contactDialog.setSize(300, 150);
        contactDialog.setLayout(new FlowLayout());
        TextArea contactsTextArea = new TextArea();
        contactsTextArea.setEditable(false);
        for (Contact contact : contacts) {
            contactsTextArea.append(contact.toString() + "\n");
        }
        contactDialog.add(contactsTextArea);
        Button closeButton = new Button("Close");
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                contactDialog.dispose();
            }
        });
        contactDialog.add(closeButton);
        contactDialog.setLocationRelativeTo(frame);
        contactDialog.setVisible(true);
        showPhonebookMenuButton();
    }


    private void readContactsFromFile() {
        contacts.clear();
        Path filePath = Path.of("phonebook.txt");
        try {
            ArrayList<String> lines = new ArrayList<>(Files.readAllLines(filePath));
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String name = parts[0];
                    String number = parts[1];
                    contacts.add(new Contact(name, number));
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void saveContactToFile(String name, String number) {
        Path filePath = Path.of("phonebook.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.APPEND, StandardOpenOption.CREATE)) {
            writer.write(name + "," + number);
            writer.newLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void showPhonebookMenuButton() {
        mainPanel.removeAll();
        Panel menuPanel = new Panel(new FlowLayout());
        Button phonebookMenuButton = new Button("Phonebook Menu");
        phonebookMenuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showWelcomeScreen();
            }
        });
        menuPanel.add(phonebookMenuButton);
        mainPanel.add(menuPanel);
        frame.revalidate();
        frame.repaint();
    }

    private void showDeleteContactField() {
        mainPanel.removeAll();
        Panel deletePanel = new Panel(new FlowLayout());
        nameTextField = new TextField();
        nameTextField.setPreferredSize(new Dimension(200, 30));
        Label nameLabel = new Label("Enter the name to delete:");
        Button deleteButton = new Button("Delete");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String enteredName = nameTextField.getText();
                System.out.println("Entered Name to delete: " + enteredName);
                showPhonebookMenuButton();
            }
        });
        deletePanel.add(nameLabel);
        deletePanel.add(nameTextField);
        deletePanel.add(deleteButton);
        mainPanel.add(deletePanel);
        frame.revalidate();
        frame.repaint();
    }

    private void showEditContactField() {
        mainPanel.removeAll();
        Panel editPanel = new Panel(new FlowLayout());
        editNameTextField = new TextField();
        editNameTextField.setPreferredSize(new Dimension(200, 30));
        Label editNameLabel = new Label("Enter the name to edit:");
        Button editButton = new Button("Edit");
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String enteredName = editNameTextField.getText();
                showConfirmationButtons(enteredName);
            }
        });
        editPanel.add(editNameLabel);
        editPanel.add(editNameTextField);
        editPanel.add(editButton);
        mainPanel.add(editPanel);
        frame.revalidate();
        frame.repaint();
    }

    private void showSearchContactField() {
        mainPanel.removeAll();
        Panel searchPanel = new Panel(new FlowLayout());
        searchTextField = new TextField();
        searchTextField.setPreferredSize(new Dimension(200, 30));
        Label searchLabel = new Label("Enter the name to search:");
        Button searchButton = new Button("Search");
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String enteredName = searchTextField.getText();
                showSearchResultsButtons(enteredName);
            }
        });
        searchPanel.add(searchLabel);
        searchPanel.add(searchTextField);
        searchPanel.add(searchButton);
        mainPanel.add(searchPanel);
        frame.revalidate();
        frame.repaint();
    }

    private void showSearchResultsButtons(String enteredName) {
        mainPanel.removeAll();
        Panel resultsPanel = new Panel(new FlowLayout());
        Button phonebookMenuButton = new Button("Phonebook Menu");
        phonebookMenuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showWelcomeScreen();
            }
        });
        resultsPanel.add(phonebookMenuButton);
        mainPanel.add(resultsPanel);
        frame.revalidate();
        frame.repaint();
    }

    private void showConfirmationButtons(String enteredName) {
        mainPanel.removeAll();
        Panel confirmationPanel = new Panel(new FlowLayout());
        newEditNameTextField = new TextField();
        newEditNameTextField.setPreferredSize(new Dimension(200, 30));
        newNumberTextField = new TextField();
        newNumberTextField.setPreferredSize(new Dimension(200, 30));
        Label newEditNameLabel = new Label("Enter the new name:");
        Label newNumberLabel = new Label("Enter the new number:");
        Button confirmButton = new Button("Confirm");
        Button noButton = new Button("No");
        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newEditName = newEditNameTextField.getText();
                String newNumber = newNumberTextField.getText();
                System.out.println("Confirming edit for: " + enteredName);
                System.out.println("New Name: " + newEditName);
                System.out.println("New Number: " + newNumber);
                showPhonebookMenuButton();
            }
        });
        noButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Edit cancelled for: " + enteredName);
                showPhonebookMenuButton();
            }
        });
        confirmationPanel.add(newEditNameLabel);
        confirmationPanel.add(newEditNameTextField);
        confirmationPanel.add(newNumberLabel);
        confirmationPanel.add(newNumberTextField);
        confirmationPanel.add(confirmButton);
        confirmationPanel.add(noButton);
        mainPanel.add(confirmationPanel);
        frame.revalidate();
        frame.repaint();
    }

    public static void main(String[] args) {
        new PhonebookApp();
    }

    // Inner class to represent a Contact
    private static class Contact {
        private String name;
        private String number;

        public Contact(String name, String number) {
            this.name = name;
            this.number = number;
        }

        @Override
        public String toString() {
            return "Name: " + name + ", Number: " + number;
        }
    }
}
