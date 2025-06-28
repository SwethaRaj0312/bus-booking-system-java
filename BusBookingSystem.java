package Bus;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import java.util.Map;
import java.util.HashMap;
import javax.swing.JComboBox;
import java.text.DateFormatSymbols;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.time.LocalDateTime;



public class BusBookingSystem extends JFrame {
    CardLayout cardLayout = new CardLayout();
    JPanel mainPanel = new JPanel(cardLayout);
    String customerName = "";
    String usernameGlobal = "";

    public BusBookingSystem() {
        setTitle("Bus Booking System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        mainPanel.add(frontPage(), "Front");
        mainPanel.add(signInPage(), "SignIn");
        mainPanel.add(forgotPasswordPage(), "Forgot");
        mainPanel.add(bookTicketPage(), "Book");
        

        add(mainPanel);
        cardLayout.show(mainPanel, "Front");
        
        setVisible(true);
    }


    private JPanel frontPage() {
        JPanel panel = new JPanel(null);
        JLabel title = new JLabel("🚌 Welcome to Bus Booking System");
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setBounds(400, 50, 600, 40);
        panel.add(title);

        JLabel u = new JLabel("Username:"), p = new JLabel("Password:");
        JTextField ut = new JTextField();
        JPasswordField pt = new JPasswordField();
        JButton loginBtn = new JButton("Login");
        JButton forgotBtn = new JButton("Forgot Password");
        JButton signInBtn = new JButton("Sign In");
        //JButton adminLoginBtn = new JButton("Admin Login");

        u.setBounds(400, 150, 120, 30); ut.setBounds(550, 150, 180, 30);
        p.setBounds(400, 200, 120, 30); pt.setBounds(550, 200, 180, 30);
        loginBtn.setBounds(550, 250, 120, 30);
        forgotBtn.setBounds(550, 290, 160, 30);
        signInBtn.setBounds(20, 20, 120, 30);
       // adminLoginBtn.setBounds(1100, 20, 120, 30);
       // adminLoginBtn.setBackground(Color.ORANGE);

        loginBtn.addActionListener(e -> {
            String username = ut.getText().trim();
            String password = new String(pt.getPassword()).trim();
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement("SELECT full_name FROM users WHERE username=? AND password=?")) {
                ps.setString(1, username);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    customerName = rs.getString("full_name");
                    usernameGlobal = username;
                    showDashboardPanel();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials!");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error during login.");
            }
        });

        forgotBtn.addActionListener(e -> cardLayout.show(mainPanel, "Forgot"));
        signInBtn.addActionListener(e -> cardLayout.show(mainPanel, "SignIn"));
       /* adminLoginBtn.addActionListener(e -> {
            if (ut.getText().equals("admin") && new String(pt.getPassword()).equals("admin123")) {
                JOptionPane.showMessageDialog(this, "Welcome Admin!");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Admin Credentials");
            }
        });*/

        panel.add(u); panel.add(ut); panel.add(p); panel.add(pt);
       panel.add(loginBtn); 
       panel.add(forgotBtn); panel.add(signInBtn); 
       //panel.add(adminLoginBtn);

        return panel;
    }

    private JPanel signInPage() {
        JPanel panel = new JPanel(null);
         JLabel title = new JLabel("🚌 Bus Booking - Sign In");
         title.setFont(new Font("Arial", Font.BOLD, 30));
         title.setBounds(480, 30, 400, 40);
         panel.add(title);

         JLabel u = new JLabel("Username:"), p = new JLabel("Password:");
         JLabel f = new JLabel("Full Name:"), m = new JLabel("Phone:");
         JLabel qLabel = new JLabel("Select a safety question:");

         JTextField ut = new JTextField(), ft = new JTextField(), mt = new JTextField();
         JPasswordField pt = new JPasswordField();
         JTextField answerField = new JTextField();

         JCheckBox cb1 = new JCheckBox("What is your favorite color?");
         JCheckBox cb2 = new JCheckBox("What is your pet’s name?");
         JCheckBox cb3 = new JCheckBox("What is your birth city?");
         ButtonGroup group = new ButtonGroup();
         group.add(cb1); group.add(cb2); group.add(cb3);

         JLabel a = new JLabel("Answer:");

         u.setBounds(400, 100, 120, 30); ut.setBounds(550, 100, 180, 30);
         p.setBounds(400, 140, 120, 30); pt.setBounds(550, 140, 180, 30);
         f.setBounds(400, 180, 120, 30); ft.setBounds(550, 180, 180, 30);
         m.setBounds(400, 220, 120, 30); mt.setBounds(550, 220, 180, 30);
         qLabel.setBounds(400, 260, 200, 30);
         cb1.setBounds(420, 290, 300, 25); cb2.setBounds(420, 320, 300, 25); cb3.setBounds(420, 350, 300, 25);
         a.setBounds(400, 390, 120, 30); answerField.setBounds(550, 390, 180, 30);

         JButton register = new JButton("Register");
         register.setBounds(540, 440, 120, 30);
         JButton loginBtn = new JButton("Login →");
         loginBtn.setBounds(1100, 20, 100, 30);

         ItemListener singleCheck = e -> {
             AbstractButton source = (AbstractButton) e.getItemSelectable();
             if (e.getStateChange() == ItemEvent.SELECTED) {
                 if (source != cb1) cb1.setSelected(false);
                 if (source != cb2) cb2.setSelected(false);
                 if (source != cb3) cb3.setSelected(false);
             }
         };
         cb1.addItemListener(singleCheck);
         cb2.addItemListener(singleCheck);
         cb3.addItemListener(singleCheck);

         register.addActionListener(e -> {
             String question = cb1.isSelected() ? cb1.getText() :
                               cb2.isSelected() ? cb2.getText() :
                               cb3.isSelected() ? cb3.getText() : "";

             if (question.isEmpty()) {
                 JOptionPane.showMessageDialog(this, "Please select a safety question.");
                 return;
             }

             String sql = "INSERT INTO users (username, password, full_name, phone, security_qn, answer) VALUES (?, ?, ?, ?, ?, ?)";
             try (Connection conn =  DBConnection.getConnection();
                  PreparedStatement ps = conn.prepareStatement(sql)) {
                 ps.setString(1, ut.getText());
                 ps.setString(2, new String(pt.getPassword()));
                 ps.setString(3, ft.getText());
                 ps.setString(4, mt.getText());
                 ps.setString(5, question);
                 ps.setString(6, answerField.getText());
                 ps.executeUpdate();
                 JOptionPane.showMessageDialog(this, "Registered Successfully!");
                 cardLayout.show(mainPanel, "Front");
             } catch (Exception ex) {
                 ex.printStackTrace();
                 JOptionPane.showMessageDialog(this, "Registration failed.");
             }
         });

         loginBtn.addActionListener(e -> cardLayout.show(mainPanel, "Front"));

         panel.add(u); panel.add(ut); panel.add(p); panel.add(pt);
         panel.add(f); panel.add(ft); panel.add(m); panel.add(mt);
         panel.add(qLabel); panel.add(cb1); panel.add(cb2); panel.add(cb3);
         panel.add(a); panel.add(answerField); panel.add(register); panel.add(loginBtn);
         return panel;
         
     }
    private JPanel forgotPasswordPage() {
        JPanel panel = new JPanel(null);
       JLabel title = new JLabel("🔒 Forgot Password?");
       title.setFont(new Font("Arial", Font.BOLD, 30));
       title.setBounds(480, 60, 400, 40);
       panel.add(title);

       JLabel u = new JLabel("Username:"), q = new JLabel("Answer:"), p = new JLabel("New Password:");
       JTextField ut = new JTextField(), qt = new JTextField();
       JPasswordField pt = new JPasswordField();
       JButton reset = new JButton("Reset Password");
       JButton loginBtn = new JButton("Login →");
       loginBtn.setBounds(1100, 20, 100, 30);

       u.setBounds(400, 140, 120, 30); ut.setBounds(550, 140, 180, 30);
       q.setBounds(400, 180, 120, 30); qt.setBounds(550, 180, 180, 30);
       p.setBounds(400, 220, 120, 30); pt.setBounds(550, 220, 180, 30);
       reset.setBounds(550, 270, 160, 30);

       reset.addActionListener(e -> {
           String sql = "UPDATE users SET password = ? WHERE username = ? AND answer = ?";
           try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
               ps.setString(1, new String(pt.getPassword()));
               ps.setString(2, ut.getText());
               ps.setString(3, qt.getText());
               int updated = ps.executeUpdate();
               if (updated > 0) {
                   JOptionPane.showMessageDialog(this, "Password Reset!");
                   cardLayout.show(mainPanel, "Front");
               } else {
                   JOptionPane.showMessageDialog(this, "Invalid information!");
               }
           } catch (Exception ex) {
               ex.printStackTrace();
               JOptionPane.showMessageDialog(this, "Error updating password.");
           }
       });

       loginBtn.addActionListener(e -> cardLayout.show(mainPanel, "Front"));
       panel.add(u); panel.add(ut); panel.add(q); panel.add(qt);
       panel.add(p); panel.add(pt); panel.add(reset);
       panel.add(loginBtn);
       return panel;
   }
     private void showDashboardPanel() {
         JPanel dashboard = new JPanel();
         dashboard.setLayout(new BorderLayout());

         JPanel topBar = new JPanel(new BorderLayout());
         topBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

         JLabel titleLabel = new JLabel("🚌 Dashboard");
         titleLabel.setFont(new Font("Serif", Font.BOLD, 32));
         topBar.add(titleLabel, BorderLayout.WEST);

         JButton logoutBtn = new JButton("Logout");
         logoutBtn.setFont(new Font("SansSerif", Font.PLAIN, 14));
         logoutBtn.setBackground(new Color(255, 204, 204));
         logoutBtn.setFocusPainted(false);
         logoutBtn.addActionListener(e -> cardLayout.show(mainPanel, "Front"));
         topBar.add(logoutBtn, BorderLayout.EAST);

         dashboard.add(topBar, BorderLayout.NORTH);

         JPanel centerPanel = new JPanel();
         centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
         centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

         JPanel leftPanel = new JPanel();
         leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

         JLabel welcomeLabel = new JLabel("Welcome, " + customerName + "!");
         welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));
         leftPanel.add(welcomeLabel);
         leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));

         String[] buttonNames = {
             "My Profile ⚙️", "Book Ticket 🚌", "Cancel Ticket ❌",
             "View Buses 🔍", "My Bookings 🗓️"
         };

         Color[] buttonColors = {
             new Color(255, 153, 153), new Color(153, 204, 255), new Color(255, 204, 153),
             new Color(204, 255, 153), new Color(204, 153, 255)
         };
         
         for (int i = 0; i < buttonNames.length; i++) {
        	 String name = buttonNames[i];
             JButton btn = new JButton(name);
        	    
        	    btn.setMaximumSize(new Dimension(250, 50));
        	    btn.setFont(new Font("SansSerif", Font.BOLD, 16));
        	    btn.setBackground(buttonColors[i]);
        	    btn.setFocusPainted(false);

        	    if (name.startsWith("My Profile")) {
        	        btn.addActionListener(e -> {
        	            mainPanel.add(myProfilePage(), "MyProfile");
        	            cardLayout.show(mainPanel, "MyProfile");
        	        });
        	    } else if (name.startsWith("View Buses")) {
        	        btn.addActionListener(e -> {
        	        	mainPanel.add(viewBusesPage(),"ViewBuses");
        	            cardLayout.show(mainPanel, "ViewBuses");
        	        });
        	    } else if (name.startsWith("Book Ticket")) {
        	        btn.addActionListener(e -> {
        	        	mainPanel.add(bookTicketPage(),"BookTicket");
        	        	cardLayout.show(mainPanel, "BookTicket");
        	        });
        	    } else if (name.startsWith("My Bookings")) {
        	    	btn.addActionListener(e -> {
        	            mainPanel.add(myBookingsPage(), "MyBookings");
        	            cardLayout.show(mainPanel, "MyBookings");
        	    	});
         		} else if (name.startsWith("Cancel Ticket")) {
        	    	btn.addActionListener(e -> {
        	            mainPanel.add(cancelBookingPage(), "CancelTicket");
        	            cardLayout.show(mainPanel, "CancelTicket");
        	    	});
         		} /*else {
        	        btn.addActionListener(e -> 
        	            JOptionPane.showMessageDialog(this, "You clicked: " + name)
         			);
        	    }*/

             leftPanel.add(btn);
             leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
         }

         JPanel rightPanel = new JPanel();
         rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
         rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 0));

         String[] greetings = {
             "• Welcome to our service!",
             "• Book tickets easily and safely!",
             "• Enjoy your journey with us!"
         };

         for (String line : greetings) {
             JLabel lbl = new JLabel(line);
             lbl.setFont(new Font("SansSerif", Font.ITALIC, 18));
             lbl.setForeground(new Color(0, 102, 153));
             rightPanel.add(lbl);
             rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));
         }

         centerPanel.add(leftPanel);
         centerPanel.add(Box.createRigidArea(new Dimension(80, 0)));
         centerPanel.add(rightPanel);

         dashboard.add(centerPanel, BorderLayout.CENTER);
         mainPanel.add(dashboard, "Dashboard");
         cardLayout.show(mainPanel, "Dashboard");
     }  
     private JPanel myProfilePage() {
    	    JPanel panel = new JPanel(null);
    	    panel.setPreferredSize(new Dimension(1000, 700));

    	    JLabel title = new JLabel("👤 My Profile");
    	    title.setFont(new Font("Arial", Font.BOLD, 26));
    	    title.setBounds(420, 30, 300, 40);
    	    panel.add(title);

    	    JLabel nameLbl = new JLabel("Full Name:"), phoneLbl = new JLabel("Phone:"), userLbl = new JLabel("Username:"),
    	           emailLbl = new JLabel("Email:"), addressLbl = new JLabel("Address:"),
    	           dobLbl = new JLabel("Date of Birth:"), questionLbl = new JLabel("Security Question:"), answerLbl = new JLabel("Answer:");

    	    JTextField nameField = new JTextField(), phoneField = new JTextField(), userField = new JTextField(),
    	               emailField = new JTextField(), addressField = new JTextField(), dobField = new JTextField(),
    	               questionField = new JTextField(), answerField = new JTextField();

    	    JButton editBtn = new JButton("Edit"), saveBtn = new JButton("Save Changes"), changePassBtn = new JButton("Change Password"), backBtn = new JButton("← Back");

    	    nameLbl.setBounds(250, 100, 150, 30); nameField.setBounds(400, 100, 250, 30);
    	    phoneLbl.setBounds(250, 150, 150, 30); phoneField.setBounds(400, 150, 250, 30);
    	    userLbl.setBounds(250, 200, 150, 30); userField.setBounds(400, 200, 250, 30);
    	    emailLbl.setBounds(250, 250, 150, 30); emailField.setBounds(400, 250, 250, 30);
    	    addressLbl.setBounds(250, 300, 150, 30); addressField.setBounds(400, 300, 250, 30);
    	    dobLbl.setBounds(250, 350, 150, 30); dobField.setBounds(400, 350, 250, 30);
    	    questionLbl.setBounds(250, 400, 150, 30); questionField.setBounds(400, 400, 300, 30);
    	    answerLbl.setBounds(250, 450, 150, 30); answerField.setBounds(400, 450, 250, 30);

    	    editBtn.setBounds(250, 500, 120, 30);
    	    saveBtn.setBounds(400, 500, 150, 30);
    	    changePassBtn.setBounds(570, 500, 180, 30);
    	    backBtn.setBounds(20, 20, 100, 30);

    	    userField.setEditable(false);
    	    nameField.setEditable(false);
    	    phoneField.setEditable(false);
    	    emailField.setEditable(false);
    	    addressField.setEditable(false);
    	    dobField.setEditable(false);
    	    questionField.setEditable(false);
    	    answerField.setEditable(false);

    	    try (Connection conn = DBConnection.getConnection();
    	         PreparedStatement stmt = conn.prepareStatement("SELECT full_name, phone, email, address, dob, security_qn, answer FROM users WHERE username = ?")) {
    	        stmt.setString(1, usernameGlobal);
    	        ResultSet rs = stmt.executeQuery();
    	        if (rs.next()) {
    	            nameField.setText(rs.getString("full_name"));
    	            phoneField.setText(rs.getString("phone"));
    	            userField.setText(usernameGlobal);
    	            emailField.setText(rs.getString("email"));
    	            addressField.setText(rs.getString("address"));
    	            dobField.setText(rs.getString("dob"));
    	            questionField.setText(rs.getString("security_qn"));
    	            answerField.setText(rs.getString("answer"));
    	        }
    	    } catch (SQLException e) {
    	        e.printStackTrace();
    	    }

    	    editBtn.addActionListener(e -> {
    	        nameField.setEditable(true);
    	        phoneField.setEditable(true);
    	        emailField.setEditable(true);
    	        addressField.setEditable(true);
    	        dobField.setEditable(true);
    	        answerField.setEditable(true);
    	    });

    	    saveBtn.addActionListener(e -> {
    	        String name = nameField.getText().trim();
    	        String phone = phoneField.getText().trim();
    	        String email = emailField.getText().trim();
    	        String address = addressField.getText().trim();
    	        String dob = dobField.getText().trim();
    	        String answer = answerField.getText().trim();

    	        if (!phone.isEmpty() && !phone.matches("\\d{10}")) {
    	            JOptionPane.showMessageDialog(panel, "Phone must be 10 digits");
    	            return;
    	        }
    	        if (!email.isEmpty() && !email.matches("^.+@.+\\..+$")) {
    	            JOptionPane.showMessageDialog(panel, "Invalid email format");
    	            return;
    	        }

    	        StringBuilder queryBuilder = new StringBuilder("UPDATE users SET ");
    	        List<String> updates = new ArrayList<>();
    	        List<Object> params = new ArrayList<>();

    	        if (!name.isEmpty()) { updates.add("full_name = ?"); params.add(name); }
    	        if (!phone.isEmpty()) { updates.add("phone = ?"); params.add(phone); }
    	        if (!email.isEmpty()) { updates.add("email = ?"); params.add(email); }
    	        if (!address.isEmpty()) { updates.add("address = ?"); params.add(address); }
    	        if (!dob.isEmpty()) { updates.add("dob = ?"); params.add(dob); }
    	        if (!answer.isEmpty()) { updates.add("answer = ?"); params.add(answer); }

    	        if (updates.isEmpty()) {
    	            JOptionPane.showMessageDialog(panel, "No fields to update.");
    	            return;
    	        }

    	        queryBuilder.append(String.join(", ", updates)).append(" WHERE username = ?");
    	        params.add(usernameGlobal);

    	        try (Connection conn = DBConnection.getConnection();
    	             PreparedStatement ps = conn.prepareStatement(queryBuilder.toString())) {

    	            for (int i = 0; i < params.size(); i++) {
    	                ps.setObject(i + 1, params.get(i));
    	            }

    	            int rows = ps.executeUpdate();
    	            if (rows > 0) {
    	                JOptionPane.showMessageDialog(panel, "Profile updated successfully.");
    	                nameField.setEditable(false);
    	                phoneField.setEditable(false);
    	                emailField.setEditable(false);
    	                addressField.setEditable(false);
    	                dobField.setEditable(false);
    	                answerField.setEditable(false);
    	            }
    	        } catch (SQLException ex) {
    	            ex.printStackTrace();
    	            JOptionPane.showMessageDialog(panel, "Error updating profile.");
    	        }
    	    });

    	    changePassBtn.addActionListener(e -> {
    	        String newPassword = JOptionPane.showInputDialog(panel, "Enter New Password:");
    	        if (newPassword != null && !newPassword.trim().isEmpty()) {
    	            try (Connection conn = DBConnection.getConnection();
    	                 PreparedStatement ps = conn.prepareStatement("UPDATE users SET password=? WHERE username=?")) {
    	                ps.setString(1, newPassword);
    	                ps.setString(2, usernameGlobal);
    	                ps.executeUpdate();
    	                JOptionPane.showMessageDialog(panel, "Password changed successfully.");
    	            } catch (SQLException ex) {
    	                ex.printStackTrace();
    	                JOptionPane.showMessageDialog(panel, "Error changing password.");
    	            }
    	        }
    	    });

    	    backBtn.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));

    	    panel.add(nameLbl); panel.add(nameField);
    	    panel.add(phoneLbl); panel.add(phoneField);
    	    panel.add(userLbl); panel.add(userField);
    	    panel.add(emailLbl); panel.add(emailField);
    	    panel.add(addressLbl); panel.add(addressField);
    	    panel.add(dobLbl); panel.add(dobField);
    	    panel.add(questionLbl); panel.add(questionField);
    	    panel.add(answerLbl); panel.add(answerField);
    	    panel.add(editBtn); panel.add(saveBtn); panel.add(changePassBtn); panel.add(backBtn);

    	    return panel;
    	}

	private JPanel viewBusesPage() {
	    JPanel panel = new JPanel(new BorderLayout());

	    JLabel title = new JLabel("🚌 All Available Buses", JLabel.CENTER);
	    title.setFont(new Font("Arial", Font.BOLD, 28));
	    title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
	    panel.add(title, BorderLayout.NORTH);

	    // 🔽 Filter Controls Panel
	    JPanel filterPanel = new JPanel();

	    JComboBox<String> fromCombo = new JComboBox<>(new String[]{"All", "Chennai", "Mumbai", "Hyderabad", "Bangalore", "Delhi", "Kolkata"});
	    JComboBox<String> toCombo = new JComboBox<>(new String[]{"All", "Chennai", "Mumbai", "Hyderabad", "Bangalore", "Delhi", "Kolkata"});
	    JComboBox<String> classCombo = new JComboBox<>(new String[]{"All", "AC", "Non-AC", "Sleeper"});
	    JButton filterBtn = new JButton("Apply Filter");

	    filterPanel.add(new JLabel("From:"));
	    filterPanel.add(fromCombo);
	    filterPanel.add(new JLabel("To:"));
	    filterPanel.add(toCombo);
	    filterPanel.add(new JLabel("Class:"));
	    filterPanel.add(classCombo);
	    filterPanel.add(filterBtn);

	    panel.add(filterPanel, BorderLayout.NORTH);

	    String[] cols = {"Bus Name", "From", "To", "Class", "Departure", "Seats", "Distance", "Fare"};
	    JTable table = new JTable(new String[0][0], cols);
	    table.setFont(new Font("SansSerif", Font.PLAIN, 14));
	    table.setRowHeight(28);

	    JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    scrollPane.setPreferredSize(new Dimension(1100, 450));
	    panel.add(scrollPane, BorderLayout.CENTER);

	    // 🔄 Method to Load Data
	    Runnable loadBuses = () -> {
	        List<String[]> rows = new ArrayList<>();
	        String from = (String) fromCombo.getSelectedItem();
	        String to = (String) toCombo.getSelectedItem();
	        String seatClass = (String) classCombo.getSelectedItem();

	        StringBuilder query = new StringBuilder("SELECT bus_name, from_city, to_city, seat_class, departure_time, available_seats, distance_km, amount_per_seat FROM buses WHERE 1=1");

	        if (!from.equals("All")) query.append(" AND from_city = '").append(from).append("'");
	        if (!to.equals("All")) query.append(" AND to_city = '").append(to).append("'");
	        if (!seatClass.equals("All")) query.append(" AND seat_class = '").append(seatClass).append("'");

	        try (Connection conn = DBConnection.getConnection();
	             Statement st = conn.createStatement();
	             ResultSet rs = st.executeQuery(query.toString())) {
	            while (rs.next()) {
	                double baseFare = rs.getDouble("amount_per_seat");
	                double multiplier = 1.0;
	                if ("AC".equalsIgnoreCase(rs.getString("seat_class"))) multiplier = 1.5;
	                else if ("Sleeper".equalsIgnoreCase(rs.getString("seat_class"))) multiplier = 1.2;

	                double adjustedFare = baseFare * multiplier;

	                rows.add(new String[]{
	                        rs.getString("bus_name"),
	                        rs.getString("from_city"),
	                        rs.getString("to_city"),
	                        rs.getString("seat_class"),
	                        rs.getString("departure_time"),
	                        String.valueOf(rs.getInt("available_seats")),
	                        String.valueOf(rs.getInt("distance_km")),
	                        String.format("₹%.2f", adjustedFare)
	                });
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            JOptionPane.showMessageDialog(panel, "Error loading bus data.");
	        }

	        String[][] data = rows.toArray(new String[0][]);
	        table.setModel(new javax.swing.table.DefaultTableModel(data, cols));
	    };

	    filterBtn.addActionListener(e -> loadBuses.run());
	    loadBuses.run(); // Load all initially

	    // Back Button
	    JButton backBtn = new JButton("← Back");
	    backBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
	    backBtn.setBackground(new Color(204, 229, 255));
	    backBtn.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));

	    JPanel bottomPanel = new JPanel();
	    bottomPanel.add(backBtn);
	    panel.add(bottomPanel, BorderLayout.SOUTH);

	    return panel;
	}
	private JPanel myBookingsPage() {
	    JPanel mainPanel = new JPanel(new BorderLayout());
	    mainPanel.setBackground(Color.WHITE);

	    JLabel title = new JLabel("📄 My Bookings", JLabel.CENTER);
	    title.setFont(new Font("Arial", Font.BOLD, 36));
	    title.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 10));
	    mainPanel.add(title, BorderLayout.NORTH);

	    String[] columnNames = {
	        "Booking ID", "From", "To", "Date", "Seat Class", "Seats",
	        "Bus Name", "Departure", "Distance (km)", "Total Fare"
	    };
	    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
	    JTable table = new JTable(tableModel);
	    table.setFont(new Font("Serif", Font.PLAIN, 20));
	    table.setRowHeight(36);
	    table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 22));
	    table.getTableHeader().setBackground(new Color(230, 230, 230));
	    table.setGridColor(Color.GRAY);
	    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

	    // Adjusted column widths for better fit and to remove horizontal scroll gap
	    int[] columnWidths = {150, 130, 130, 120, 150, 100, 180, 140, 170, 190};
	    for (int i = 0; i < columnWidths.length; i++) {
	        table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
	    }

	    JScrollPane scrollPane = new JScrollPane(
	        table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    scrollPane.getViewport().setBackground(Color.WHITE);
	    scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));
	    mainPanel.add(scrollPane, BorderLayout.CENTER);

	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(
	             "SELECT b.booking_id, b.from_place, b.to_place, b.journey_date, b.class, b.seats, " +
	             "bu.bus_name, bu.departure_time, bu.distance_km, b.total_fare " +
	             "FROM bookings b " +
	             "JOIN buses bu ON b.bus_id = bu.bus_id " +
	             "WHERE b.username = ?")) {

	        ps.setString(1, usernameGlobal);
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            Object[] row = {
	                rs.getInt("booking_id"),
	                rs.getString("from_place"),
	                rs.getString("to_place"),
	                rs.getString("journey_date"),
	                rs.getString("class"),
	                rs.getInt("seats"),
	                rs.getString("bus_name"),
	                rs.getString("departure_time"),
	                rs.getInt("distance_km"),
	                String.format("₹%.2f", rs.getDouble("total_fare"))
	            };
	            tableModel.addRow(row);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(this, "⚠️ Error loading bookings.");
	    }

	    JButton backBtn = new JButton("← Back");
	    backBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
	    backBtn.setBackground(new Color(255, 182, 193));
	    backBtn.setPreferredSize(new Dimension(120, 40));
	    backBtn.addActionListener(e -> cardLayout.show(BusBookingSystem.this.mainPanel, "Dashboard"));

	    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
	    bottomPanel.setBackground(Color.WHITE);
	    bottomPanel.add(backBtn);

	    mainPanel.add(bottomPanel, BorderLayout.SOUTH);

	    return mainPanel;
	}
	private JPanel cancelBookingPage() {
	    JPanel panel = new JPanel(new BorderLayout());
	    panel.setPreferredSize(new Dimension(1400, 900));
	    panel.setBackground(Color.WHITE);

	    JLabel title = new JLabel("❌ Cancel Booking", JLabel.CENTER);
	    title.setFont(new Font("Arial", Font.BOLD, 36));
	    title.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 10));
	    panel.add(title, BorderLayout.NORTH);

	    String[] columns = {"Booking ID", "From", "To", "Date", "Seat Class", "Seats", "Bus Name", "Total Fare"};
	    DefaultTableModel model = new DefaultTableModel(columns, 0);
	    JTable table = new JTable(model);
	    table.setFont(new Font("Serif", Font.PLAIN, 18));
	    table.setRowHeight(30);
	    table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 20));

	    JScrollPane scrollPane = new JScrollPane(table);
	    panel.add(scrollPane, BorderLayout.CENTER);

	    // Load user bookings
	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(
	             "SELECT b.booking_id, b.from_place, b.to_place, b.journey_date, b.class, b.seats, bu.bus_name, b.total_fare " +
	             "FROM bookings b JOIN buses bu ON b.bus_id = bu.bus_id WHERE b.username = ?")) {

	        ps.setString(1, usernameGlobal);
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            Object[] row = {
	                rs.getInt("booking_id"),
	                rs.getString("from_place"),
	                rs.getString("to_place"),
	                rs.getString("journey_date"),
	                rs.getString("class"),
	                rs.getInt("seats"),
	                rs.getString("bus_name"),
	                String.format("₹%.2f", rs.getDouble("total_fare"))
	            };
	            model.addRow(row);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(panel, "⚠️ Error loading bookings.");
	    }

	    JButton cancelBtn = new JButton("Cancel Selected Booking");
	    cancelBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
	    cancelBtn.setBackground(new Color(255, 160, 122));

	    cancelBtn.addActionListener(e -> {
	        int selectedRow = table.getSelectedRow();
	        if (selectedRow == -1) {
	            JOptionPane.showMessageDialog(panel, "Please select a booking to cancel.");
	            return;
	        }

	        int bookingId = (int) model.getValueAt(selectedRow, 0);
	        int seatsToRestore = (int) model.getValueAt(selectedRow, 5);

	        int confirm = JOptionPane.showConfirmDialog(panel, "Are you sure you want to cancel Booking ID: " + bookingId + "?", "Confirm", JOptionPane.YES_NO_OPTION);
	        if (confirm != JOptionPane.YES_OPTION) return;

	        try (Connection conn = DBConnection.getConnection()) {
	            conn.setAutoCommit(false);

	            int busId = -1;
	            java.sql.Date journeyDate;
	            double totalFare = 0;

	            try (PreparedStatement ps = conn.prepareStatement("SELECT bus_id, journey_date, total_fare FROM bookings WHERE booking_id = ?")) {
	                ps.setInt(1, bookingId);
	                ResultSet rs = ps.executeQuery();
	                if (rs.next()) {
	                    busId = rs.getInt("bus_id");
	                    journeyDate = rs.getDate("journey_date");
	                    totalFare = rs.getDouble("total_fare");
	                } else {
	                    JOptionPane.showMessageDialog(panel, "⚠️ Booking not found.");
	                    conn.rollback();
	                    return;
	                }
	            }

	            LocalDate today = LocalDate.now();
	            LocalDate journeyDay = journeyDate.toLocalDate();
	           

	            long daysBefore = ChronoUnit.DAYS.between(today, journeyDay);

	            double refundAmount = 0;
	            if (daysBefore >= 2) refundAmount = totalFare;
	            else if (daysBefore >= 1) refundAmount = totalFare * 0.5;
	            else refundAmount = 0;

	            // Delete booking and passengers using CASCADE or correct order
	            try (PreparedStatement ps1 = conn.prepareStatement("DELETE FROM passengers WHERE booking_id = ?");
	                 PreparedStatement ps2 = conn.prepareStatement("DELETE FROM bookings WHERE booking_id = ?");
	                 PreparedStatement ps3 = conn.prepareStatement("UPDATE buses SET available_seats = available_seats + ? WHERE bus_id = ?")) {

	                ps1.setInt(1, bookingId);
	                ps1.executeUpdate();

	                ps2.setInt(1, bookingId);
	                int rowsDeleted = ps2.executeUpdate();

	                ps3.setInt(1, seatsToRestore);
	                ps3.setInt(2, busId);
	                ps3.executeUpdate();

	                if (rowsDeleted == 0) {
	                    JOptionPane.showMessageDialog(panel, "❌ Booking could not be deleted. It may not exist.");
	                    conn.rollback();
	                    return;
	                }
	            }

	            conn.commit();
	            model.removeRow(selectedRow);

	            String message = String.format(
	                "<html><b>Booking ID:</b> %d<br>" +
	                "<b>Journey Date:</b> %s<br>" +
	                "<b>Cancellation Date:</b> %s<br>" +
	                "<b>Total Paid:</b> ₹%.2f<br>" +
	                "<b>Refund Amount:</b> ₹%.2f<br><br>" +
	                "Your refund will be processed shortly.</html>",
	                bookingId,
	                journeyDay.toString(),
	                today.toString(),
	                totalFare,
	                refundAmount
	            );

	            JLabel label = new JLabel(message);
	            JOptionPane optionPane = new JOptionPane(label, JOptionPane.INFORMATION_MESSAGE);
	            JDialog dialog = optionPane.createDialog(panel, "Refund Summary");
	            dialog.setPreferredSize(new Dimension(500, 300));
	            dialog.setResizable(true);
	            dialog.pack();
	            dialog.setLocationRelativeTo(panel);
	            dialog.setVisible(true);

	        } catch (Exception ex) {
	            ex.printStackTrace();
	            JOptionPane.showMessageDialog(panel, "❌ Failed to cancel booking.");
	        }
	    });

	    JButton backBtn = new JButton("← Back");
	    backBtn.setFont(new Font("SansSerif", Font.PLAIN, 16));
	    backBtn.setBackground(new Color(255, 182, 193));
	    backBtn.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));

	    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    bottomPanel.setBackground(Color.WHITE);
	    bottomPanel.add(backBtn);
	    bottomPanel.add(cancelBtn);

	    panel.add(bottomPanel, BorderLayout.SOUTH);

	    return panel;
	}

    private JPanel paymentPage(String from, String to, String date, int seatCount, String seatClass, int busId, List<Passenger> passengerList) {
        JPanel panel = new JPanel(null);
        panel.setPreferredSize(new Dimension(1200, 800));

        JLabel title = new JLabel("💳 Payment Details");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setBounds(450, 30, 400, 40);
        panel.add(title);

        JLabel fromLbl = new JLabel("From: " + from);
        JLabel toLbl = new JLabel("To: " + to);
        JLabel dateLbl = new JLabel("Date: " + date);
        JLabel seatLbl = new JLabel("Seats: " + seatCount);
        JLabel classLbl = new JLabel("Class: " + seatClass);
        JLabel kmLbl = new JLabel();
        JLabel amtLbl = new JLabel();
        JLabel totalLbl = new JLabel();

        fromLbl.setBounds(100, 100, 400, 30);
        toLbl.setBounds(100, 140, 400, 30);
        dateLbl.setBounds(100, 180, 400, 30);
        seatLbl.setBounds(100, 220, 400, 30);
        classLbl.setBounds(100, 260, 400, 30);
        kmLbl.setBounds(100, 300, 400, 30);
        amtLbl.setBounds(100, 340, 400, 30);
        totalLbl.setBounds(100, 380, 400, 30);

        JButton payBtn = new JButton("Pay and Confirm Booking");
        JButton backBtn = new JButton("← Back");
        payBtn.setBounds(450, 450, 250, 40);
        backBtn.setBounds(20, 20, 100, 30);

        panel.add(fromLbl);
        panel.add(toLbl);
        panel.add(dateLbl);
        panel.add(seatLbl);
        panel.add(classLbl);
        panel.add(kmLbl);
        panel.add(amtLbl);
        panel.add(totalLbl);
        panel.add(payBtn);
        panel.add(backBtn);

        final double[] totalFare = new double[1]; // for lambda access

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT distance_km, amount_per_seat FROM buses WHERE bus_id = ?")) {

            ps.setInt(1, busId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int distance = rs.getInt("distance_km");
                double baseFare = rs.getDouble("amount_per_seat");

                double multiplier = 1.0;
                if ("AC".equalsIgnoreCase(seatClass)) multiplier = 1.5;
                else if ("Sleeper".equalsIgnoreCase(seatClass)) multiplier = 1.2;

                double adjustedFare = baseFare * multiplier;
                totalFare[0] = adjustedFare * seatCount;

                kmLbl.setText("Distance: " + distance + " km");
                amtLbl.setText(String.format("Fare per Seat: ₹%.2f", adjustedFare));
                totalLbl.setText(String.format("Total Fare: ₹%.2f", totalFare[0]));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(panel, "Error fetching fare and distance from database.");
        }

        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "BookTicket"));

        payBtn.addActionListener(e -> {
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                         "INSERT INTO bookings (username, from_place, to_place, journey_date, seats, class, bus_id, total_fare) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                         Statement.RETURN_GENERATED_KEYS)) {

                // ✅ Check seat availability
                PreparedStatement seatCheck = conn.prepareStatement("SELECT available_seats FROM buses WHERE bus_id = ?");
                seatCheck.setInt(1, busId);
                ResultSet seatRs = seatCheck.executeQuery();
                if (seatRs.next()) {
                    int available = seatRs.getInt("available_seats");
                    if (available < seatCount) {
                        JOptionPane.showMessageDialog(panel, "⚠️ Not enough seats available. Try booking fewer seats.");
                        return;
                    }
                }

                // ✅ Insert booking
                ps.setString(1, usernameGlobal);
                ps.setString(2, from);
                ps.setString(3, to);
                ps.setString(4, date);
                ps.setInt(5, seatCount);
                ps.setString(6, seatClass);
                ps.setInt(7, busId);
                ps.setDouble(8, totalFare[0]);
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int bookingId = rs.getInt(1);

                    try (PreparedStatement pps = conn.prepareStatement(
                            "INSERT INTO passengers (booking_id, name, age, gender, category) VALUES (?, ?, ?, ?, ?)")) {
                        for (Passenger p : passengerList) {
                            pps.setInt(1, bookingId);
                            pps.setString(2, p.name);
                            pps.setInt(3, p.age);
                            pps.setString(4, p.gender);
                            pps.setString(5, p.category);
                            pps.addBatch();
                        }
                        pps.executeBatch();
                    }

                    // ✅ Update available seats
                    try (PreparedStatement updateSeats = conn.prepareStatement(
                            "UPDATE buses SET available_seats = available_seats - ? WHERE bus_id = ?")) {
                        updateSeats.setInt(1, seatCount);
                        updateSeats.setInt(2, busId);
                        updateSeats.executeUpdate();
                    }

                    passengerList.clear();
                    JOptionPane.showMessageDialog(panel, "✅ Payment Successful! Booking Confirmed.");
                    cardLayout.show(mainPanel, "Dashboard");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(panel, "⚠️ Payment Failed. Please try again.");
            }
        });

        return panel;
    }
    private JPanel bookTicketPage() {
        JPanel panel = new JPanel(null);
        panel.setPreferredSize(new Dimension(1200, 800));

        JLabel title = new JLabel("🚌 Book Your Ticket");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setBounds(480, 30, 400, 40);
        panel.add(title);

        JLabel fromLbl = new JLabel("From:"), toLbl = new JLabel("To:"),
               dateLbl = new JLabel("Date:"), seatLbl = new JLabel("No. of Persons:"),
               classLbl = new JLabel("Seat Class:"), busLbl = new JLabel("Select Bus:");

        JComboBox<String> fromCombo = new JComboBox<>(new String[]{"Chennai", "Bangalore", "Hyderabad", "Delhi", "Mumbai", "Kolkata"});
        JComboBox<String> toCombo = new JComboBox<>(new String[]{"Mumbai", "Delhi", "Kolkata", "Chennai", "Bangalore", "Hyderabad"});
        JComboBox<String> dayCombo = new JComboBox<>();
        JComboBox<String> monthCombo = new JComboBox<>();
        JComboBox<String> yearCombo = new JComboBox<>();
        JTextField seatsField = new JTextField();
        JComboBox<String> classCombo = new JComboBox<>(new String[]{"Sleeper", "AC", "Non-AC"});
        JComboBox<String> busList = new JComboBox<>();
        Map<String, Integer> busMap = new HashMap<>();

        for (int i = 1; i <= 31; i++) dayCombo.addItem(String.format("%02d", i));
        String[] monthNames = new DateFormatSymbols().getMonths();
        for (int i = 0; i < 12; i++) monthCombo.addItem(monthNames[i]);
        int currentYear = LocalDate.now().getYear();
        for (int i = 0; i < 10; i++) yearCombo.addItem(String.valueOf(currentYear + i));

        JButton searchBusesBtn = new JButton("Search Buses");
        JButton enterPassengerBtn = new JButton("Enter Passenger Details");
        JButton bookBtn = new JButton("Make Payment");
        JButton backBtn = new JButton("← Back");

        fromLbl.setBounds(300, 100, 150, 30); fromCombo.setBounds(450, 100, 200, 30);
        toLbl.setBounds(300, 150, 150, 30); toCombo.setBounds(450, 150, 200, 30);
        dateLbl.setBounds(300, 200, 150, 30);
        dayCombo.setBounds(450, 200, 60, 30); monthCombo.setBounds(520, 200, 60, 30); yearCombo.setBounds(590, 200, 80, 30);
        classLbl.setBounds(300, 250, 150, 30); classCombo.setBounds(450, 250, 200, 30);
        seatLbl.setBounds(300, 300, 150, 30); seatsField.setBounds(450, 300, 200, 30);
        busLbl.setBounds(300, 350, 150, 30); busList.setBounds(450, 350, 500, 30);

        searchBusesBtn.setBounds(700, 250, 160, 30);
        enterPassengerBtn.setBounds(450, 400, 200, 30);
        bookBtn.setBounds(450, 450, 200, 40);
        backBtn.setBounds(20, 20, 100, 30);

        searchBusesBtn.setBackground(new Color(135, 206, 250));
        enterPassengerBtn.setBackground(new Color(255, 204, 102));
        bookBtn.setBackground(new Color(144, 238, 144));
        backBtn.setBackground(new Color(255, 182, 193));

        searchBusesBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        enterPassengerBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        bookBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        backBtn.setFont(new Font("SansSerif", Font.PLAIN, 14));

        panel.add(fromLbl); panel.add(fromCombo);
        panel.add(toLbl); panel.add(toCombo);
        panel.add(dateLbl); panel.add(dayCombo); panel.add(monthCombo); panel.add(yearCombo);
        panel.add(classLbl); panel.add(classCombo);
        panel.add(seatLbl); panel.add(seatsField);
        panel.add(busLbl); panel.add(busList);
        panel.add(searchBusesBtn); panel.add(enterPassengerBtn); panel.add(bookBtn); panel.add(backBtn);

        List<Passenger> passengerList = new ArrayList<>();

        searchBusesBtn.addActionListener(e -> {
            busList.removeAllItems();
            busMap.clear();

            String from = (String) fromCombo.getSelectedItem();
            String to = (String) toCombo.getSelectedItem();

            if (from.equals(to)) {
                JOptionPane.showMessageDialog(panel, "From and To cities cannot be the same.");
                return;
            }

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                     "SELECT bus_id, bus_name, seat_class, departure_time, available_seats FROM buses WHERE from_city = ? AND to_city = ?")) {

                ps.setString(1, from);
                ps.setString(2, to);

                ResultSet rs = ps.executeQuery();
                boolean found = false;

                while (rs.next()) {
                    int id = rs.getInt("bus_id");
                    String name = rs.getString("bus_name");
                    String cls = rs.getString("seat_class");
                    String dep = rs.getString("departure_time");
                    int seats = rs.getInt("available_seats");
                   

                    String display = name + " (Class: " + cls + ", Departure: " + dep + ", Seats: " + seats + ")";

                    busList.addItem(display);
                    busMap.put(display, id);
                    found = true;
                }

                if (!found) busList.addItem("No buses available.");

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(panel, "Error loading buses.");
            }
        });
        enterPassengerBtn.addActionListener(e -> {
            try {
                int count = Integer.parseInt(seatsField.getText());
                if (count <= 0) throw new NumberFormatException();

                JPanel passengerPanel = new JPanel(new GridLayout(count + 1, 4, 10, 10));
                passengerPanel.setPreferredSize(new Dimension(700, 50 + count * 40));

                passengerPanel.add(new JLabel("Name"));
                passengerPanel.add(new JLabel("Age"));
                passengerPanel.add(new JLabel("Gender"));
                passengerPanel.add(new JLabel("Category"));

                passengerList.clear();

                JTextField[] nameFields = new JTextField[count];
                JTextField[] ageFields = new JTextField[count];
                JComboBox<String>[] genderBoxes = new JComboBox[count];
                JTextField[] categoryFields = new JTextField[count];

                for (int i = 0; i < count; i++) {
                    nameFields[i] = new JTextField();
                    ageFields[i] = new JTextField();
                    genderBoxes[i] = new JComboBox<>(new String[]{"Male", "Female", "Other"});
                    categoryFields[i] = new JTextField();
                    categoryFields[i].setEditable(false);

                    passengerPanel.add(nameFields[i]);
                    passengerPanel.add(ageFields[i]);
                    passengerPanel.add(genderBoxes[i]);
                    passengerPanel.add(categoryFields[i]);

                    int index = i;
                    ageFields[i].addKeyListener(new KeyAdapter() {
                        public void keyReleased(KeyEvent e) {
                            try {
                                int age = Integer.parseInt(ageFields[index].getText());
                                categoryFields[index].setText(age < 10 ? "Child" : "Adult");
                            } catch (Exception ignored) {}
                        }
                    });
                }

                JScrollPane scrollPane = new JScrollPane(passengerPanel);
                scrollPane.setPreferredSize(new Dimension(750, 400));

                int result = JOptionPane.showConfirmDialog(panel, scrollPane, "Enter Passenger Details", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    passengerList.clear();
                    for (int i = 0; i < count; i++) {
                        String name = nameFields[i].getText().trim();
                        String ageStr = ageFields[i].getText().trim();
                        String gender = (String) genderBoxes[i].getSelectedItem();
                        String category = categoryFields[i].getText().trim();

                        if (name.isEmpty() || ageStr.isEmpty() || category.isEmpty()) {
                            JOptionPane.showMessageDialog(panel, "Please enter valid details for all passengers.");
                            return;
                        }

                        try {
                            int age = Integer.parseInt(ageStr);
                            passengerList.add(new Passenger(name, age, gender, category));
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(panel, "Invalid age provided.");
                            return;
                        }
                    }

                    JOptionPane.showMessageDialog(panel, "Passenger details saved.");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Enter a valid number of passengers.");
            }
        });

        bookBtn.addActionListener(e -> {
            try {
                String from = (String) fromCombo.getSelectedItem();
                String to = (String) toCombo.getSelectedItem();
                String day = (String) dayCombo.getSelectedItem();
                int month = monthCombo.getSelectedIndex() + 1;
                String year = (String) yearCombo.getSelectedItem();
                String seatClass = (String) classCombo.getSelectedItem();
                int seatCount = Integer.parseInt(seatsField.getText().trim());
                String date = String.format("%s-%02d-%02d", year, month, Integer.parseInt(day));
                LocalDate journeyDate = LocalDate.parse(date);
                LocalDate today = LocalDate.now();

                if (!journeyDate.isAfter(today)) {
                    JOptionPane.showMessageDialog(panel, "Journey date must be in the future.");
                    return;
                }

                if (passengerList.size() != seatCount) {
                    JOptionPane.showMessageDialog(panel, "Passenger details not filled or mismatch with seat count.");
                    return;
                }

                String selectedBus = (String) busList.getSelectedItem();
                if (selectedBus == null || !busMap.containsKey(selectedBus)) {
                    JOptionPane.showMessageDialog(panel, "Please select a valid bus.");
                    return;
                }

                int busId = busMap.get(selectedBus);

                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement ps = conn.prepareStatement("SELECT available_seats, seat_class FROM buses WHERE bus_id = ?")) {
                    ps.setInt(1, busId);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        int availableSeats = rs.getInt("available_seats");
                        String busClass = rs.getString("seat_class");

                        if (availableSeats < seatCount) {
                            JOptionPane.showMessageDialog(panel, "Not enough seats available.");
                            return;
                        }

                        if (!seatClass.equalsIgnoreCase(busClass)) {
                            JOptionPane.showMessageDialog(panel, "Selected seat class does not match bus class.");
                            return;
                        }
                    }
                }

                JPanel paymentPanel = paymentPage(from, to, date, seatCount, seatClass, busId, passengerList);
                mainPanel.add(paymentPanel, "Payment");
                cardLayout.show(mainPanel, "Payment");

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(panel, "Error processing ticket booking.");
            }
        });

        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));

        return panel;
    }

       class Passenger {
        String name;
        int age;
        String gender;
        String category;

        public Passenger(String name, int age, String gender, String category) {
            this.name = name;
            this.age = age;
            this.gender = gender;
            this.category = category;
        }
    }

   
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
        new BusBookingSystem();
    }
}

